package com.virtue.csr.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Price;
import com.stripe.model.PriceCollection;
import com.virtue.csr.constants.MESSAGE_TYPES;
import com.virtue.csr.constants.Subscription;
import com.virtue.csr.constants.TokenStatus;
import com.virtue.csr.dto.Status;
import com.virtue.csr.model.ChargeRequest;
import com.virtue.csr.model.UserSubscription;
import com.virtue.csr.model.stripe.Plan;
import com.virtue.csr.model.stripe.StripeChargeDetails;
import com.virtue.csr.service.StripeService;

@RestController
public class StripePaymentController {
	
	@Autowired
    private StripeService paymentsService;
	
	@Value("${stripe.mse.free.plan}")
    private String freePlanKey;
	
		
	@PostMapping("subscribe")
	public ResponseEntity<Status> payForSubscription(@RequestBody ChargeRequest chargeRequest) 
			throws StripeException, JsonMappingException, JsonProcessingException, RuntimeException {
		
		UserSubscription newUserSubscription = null;
		
		chargeRequest.setCurrency(com.virtue.csr.constants.Currency.USD);
        Charge charge = paymentsService.charge(chargeRequest);
        
        if (charge.getStatus().equalsIgnoreCase("succeeded")) {
        	
	        StripeChargeDetails subscriptionDetails = extractedSubscriptionDetails(chargeRequest, charge);
	              
	        UserSubscription userSubscription = createUserSubscription(chargeRequest);
	        userSubscription.setSubscriptionDetails(subscriptionDetails);
	        userSubscription.setSubscriptionMode(Subscription.STRIPE);
	        
	        newUserSubscription = paymentsService.addSubscription(userSubscription);
        } else {
        	return ResponseEntity.ok(new Status("We are unable to process your request. Please try again.", MESSAGE_TYPES.WARN));
        }
	
		return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Subscription has been saved.", newUserSubscription));
    }

	private StripeChargeDetails extractedSubscriptionDetails(ChargeRequest chargeRequest, Charge charge) {
		StripeChargeDetails subscriptionDetails = new StripeChargeDetails();
		subscriptionDetails.setCardTokenId(chargeRequest.getToken());
		subscriptionDetails.setPlan(chargeRequest.getPlan());
		subscriptionDetails.setPriceId(chargeRequest.getPlanId());
		subscriptionDetails.setInterval(chargeRequest.getInterval());
		subscriptionDetails.setIntervalCount(chargeRequest.getIntervalCount());
		subscriptionDetails.setAmount(charge.getAmount()/100);
		subscriptionDetails.setStatus(charge.getStatus());
		subscriptionDetails.setReceiptUrl(charge.getReceiptUrl());
		subscriptionDetails.setDescription(charge.getDescription());
		subscriptionDetails.setCurrency(charge.getCurrency());
		subscriptionDetails.setPaid(charge.getPaid());
		subscriptionDetails.setId(charge.getId());
		subscriptionDetails.setBalanceTransaction(charge.getBalanceTransaction());
		return subscriptionDetails;
	}

	private UserSubscription createUserSubscription(ChargeRequest chargeRequest) {
		UserSubscription userSubscription = new UserSubscription();
		userSubscription.setEmailId(chargeRequest.getEmail());
		userSubscription.setName(chargeRequest.getName());
		userSubscription.setStatus(TokenStatus.ACTIVE);
		userSubscription.setRefund(chargeRequest.getRefund());
		
		LocalDateTime currentDateTime = LocalDateTime.now();
		userSubscription.setStartDate(currentDateTime);
		LocalDateTime expiredDateTime = null;
		if (chargeRequest.getInterval().equalsIgnoreCase("month")) {
		   expiredDateTime = currentDateTime.plusMonths(chargeRequest.getIntervalCount());
		} else if (chargeRequest.getInterval().equalsIgnoreCase("year")) {
			expiredDateTime = currentDateTime.plusYears(chargeRequest.getIntervalCount());
		} else if (chargeRequest.getInterval().equalsIgnoreCase("week")){
			expiredDateTime = currentDateTime.plusDays(7);
		} else {
			expiredDateTime = currentDateTime.plusDays(chargeRequest.getIntervalCount());
		}
		userSubscription.setEndDate(expiredDateTime);
		
		return userSubscription;
	}
	
	@GetMapping("getPlanList")
	public ResponseEntity<Status> getProductPrices() throws StripeException {
		
		PriceCollection priceCollection = paymentsService.getProductPrices();
		
		List<Plan> list = priceCollection.getData().stream().sorted(Comparator.comparingLong(Price::getUnitAmount)).map(price -> new Plan(
			price.getId(), price.getType(), (price.getUnitAmount()/100), 
			price.getRecurring() == null ? price.getMetadata().get("interval") : price.getRecurring().getInterval(),
			price.getRecurring() == null ?	Long.parseLong(price.getMetadata().get("interval_count")) : price.getRecurring().getIntervalCount(),
			price.getMetadata().get("name"), price.getCurrency(), price.getActive(), price.getMetadata().get("details"), 
			Integer.parseInt(price.getMetadata().get("refund")))).collect(Collectors.toList());
		
		 return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Product Plan list.", list));

	}
	
	@GetMapping("getPlanByCode/{token}")
	public ResponseEntity<Status> getPlanByCode(@PathVariable("token") String token)  {
		
		 return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Subscription.", paymentsService.getSubscriptionByCode(token)));

	}
	
	@PostMapping("cancelSubscription")
	public ResponseEntity<Status> cancelSubscription(@RequestBody ChargeRequest chargeRequest) throws StripeException  {
		
		Map<String, Object> responseMap = checkRefund(chargeRequest);
		
		
		if ((boolean) responseMap.get("applicableforrefund")) {
			
			UserSubscription dbUserSubscription = (UserSubscription) responseMap.get("userSubscription");
    	
	    	dbUserSubscription.setStatus(TokenStatus.INACTIVE);
	    	dbUserSubscription.setEndDate(LocalDateTime.now());
	    	dbUserSubscription.setReason(chargeRequest.getReason());
	    	
	    	paymentsService.saveUpdate(dbUserSubscription);
	    	paymentsService.refund(dbUserSubscription);
	    	
	    	responseMap.remove("userSubscription");
			
	    	return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "Successfully unsubscribe account.", responseMap));
		} else {
			throw new RuntimeException((String) responseMap.get("message"));
		}
	}
	
	@PostMapping("validateRefund")
	public ResponseEntity<Status> validateRefund(@RequestBody ChargeRequest chargeRequest)  {
		
		Map<String, Object> responseMap = checkRefund(chargeRequest);
		responseMap.remove("userSubscription");
    	
    	return ResponseEntity.ok(new Status(true, MESSAGE_TYPES.INFO, "refund status.", responseMap));
	}

	private Map<String, Object> checkRefund(ChargeRequest chargeRequest) {
		
		UserSubscription dbUserSubscription = paymentsService.findByEmailIdAndTokenAndStatus(chargeRequest.getEmail(), chargeRequest.getToken());
		Map<String, Object> responseMap = new HashMap<String, Object>();
		responseMap.put("userSubscription", dbUserSubscription);
		if (dbUserSubscription == null) {
    		responseMap.put("applicableforrefund", false);
    		responseMap.put("message", "You are not eligible to apply for refund. Please try again or contact info@virtueanalytics.com for help.");
    		return responseMap;
    	}  	
		
		LocalDateTime startDateTime = dbUserSubscription.getStartDate();
		
		if (dbUserSubscription.getRefund() > 0) {
			LocalDateTime refundExpiredDateTime = startDateTime.plusDays(dbUserSubscription.getRefund());
			
			if (refundExpiredDateTime.isAfter(LocalDateTime.now())) {
				responseMap.put("applicableforrefund", true);				
			} else {
				responseMap.put("applicableforrefund", false);
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM, YYYY HH:mm");
				String message = "You are not eligible to apply for refund as you bought subscription on " + formatter.format(startDateTime) 
					+ " and refund valid till " + formatter.format(refundExpiredDateTime) + ". Please contact info@virtueanalytics.com for help.";
	    		responseMap.put("message", message);
			}   
		} else {
			responseMap.put("applicableforrefund", false);
    		responseMap.put("message", "You are not eligible to apply for refund as your plan is applicable for refund. Please try again or contact info@virtueanalytics.com for help.");
		}
		
		
		
		return responseMap;
	}

	@ExceptionHandler(StripeException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
    public Status handleError(StripeException ex) {
		return new Status(false, MESSAGE_TYPES.ERROR, ex.getMessage(), null);
    }
	
	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
    public Status alreadySubscriptionError(RuntimeException ex) {
		return new Status(false, MESSAGE_TYPES.ERROR, ex.getMessage(), null);
    }
	
	
}
