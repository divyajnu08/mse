package com.virtue.csr.service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Price;
import com.stripe.model.PriceCollection;
import com.stripe.model.Refund;
import com.stripe.model.Source;
import com.stripe.model.Subscription;
import com.stripe.param.SubscriptionCreateParams;
import com.virtue.csr.constants.TokenStatus;
import com.virtue.csr.model.ChargeRequest;
import com.virtue.csr.model.UserSubscription;
import com.virtue.csr.repository.UserSubscriptionRepository;

@Service
public class StripeService {
	
	@Autowired
	private UserSubscriptionRepository userSubscriptionRepository;
	
	@Value("${stripe.secret.key}")
    private String secretKey;
	
	@Value("${stripe.description}")
    private String description;
	
	@Value("${stripe.mse.product.id}")
    private String mseProductId;
    
    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }
    
    public Customer saveCustomer(UserSubscription userSubscription)  throws StripeException {
    	
    	Map<String, Object> sourceParams = new HashMap<>();
    	sourceParams.put("type", "ach_credit_transfer");
    	sourceParams.put("currency", com.virtue.csr.constants.Currency.USD);
    	Map<String, Object> ownerParams = new HashMap<>();
    	ownerParams.put("email", userSubscription.getEmailId());
    	ownerParams.put("name", userSubscription.getName());
    	sourceParams.put("owner", ownerParams);

    	Source source = Source.create(sourceParams);
    	
        Map<String, Object> customerParam = new HashMap<>();
        customerParam.put("name", userSubscription.getName());
        customerParam.put("email", userSubscription.getEmailId());
       // customerParam.put("invoice_settings.custom_fields.name", "Subscription Code");
       // customerParam.put("invoice_settings.custom_fields.value", userSubscription.getToken());
        customerParam.put("description", "MSE Customer");
        customerParam.put("source", source.getId());
        
        return Customer.create(customerParam);
    }
    
    public Charge charge(ChargeRequest chargeRequest)  throws StripeException {
    	
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", chargeRequest.getAmount());
        chargeParams.put("currency", chargeRequest.getCurrency().name());
        chargeParams.put("description", description);
        chargeParams.put("receipt_email", chargeRequest.getEmail());
        chargeParams.put("source", chargeRequest.getToken());
        
        return Charge.create(chargeParams);
    }
    
    public void updateCharge(UserSubscription userSubscription)  throws StripeException {
    	
    	Charge charge =  Charge.retrieve(userSubscription.getSubscriptionDetails().getId());

    	Map<String, Object> metadata = new HashMap<>();
    	metadata.put("mse_subscription_code", userSubscription.getToken());
    	
    	Map<String, Object> params = new HashMap<>();
    	params.put("metadata", metadata);
    	params.put("description", description + userSubscription.getToken());
    	params.put("customer", userSubscription.getSubscriptionDetails().getCustomerId());

    	charge.update(params);
    }
    
    public Refund refund(UserSubscription userSubscription) throws StripeException {
    	Map<String, Object> params = new HashMap<>();
    	params.put("charge", userSubscription.getSubscriptionDetails().getId());
    	return Refund.create(params);
    }
    
    
    public Subscription addSpripeSubscription(String customerId, String priceId)  throws StripeException {
    	
    	SubscriptionCreateParams params = SubscriptionCreateParams.builder()
		    .setCustomer(customerId).addItem(SubscriptionCreateParams.Item.builder()
		        .setPrice(priceId).build()).build();
    	
		Subscription subscription = Subscription.create(params);
		
		return subscription;

    }
    
    public PriceCollection getProductPrices() throws StripeException {
		
		Map<String, Object> params = new HashMap<>();
		params.put("product", mseProductId);

		PriceCollection prices = Price.list(params);
		
		return prices;

	}
    
    public UserSubscription addSubscription(UserSubscription userSubscription) throws StripeException {
    	
    	UserSubscription dbUserSubscription = userSubscriptionRepository.findByEmailIdAndStatus(
    			userSubscription.getEmailId(), TokenStatus.ACTIVE.name());
    	
    	if (dbUserSubscription != null) {
    		dbUserSubscription.setStatus(TokenStatus.INACTIVE);
    		userSubscription.setToken(dbUserSubscription.getToken());
    		userSubscription.getSubscriptionDetails().setCustomerId(dbUserSubscription.getSubscriptionDetails().getCustomerId());
    		userSubscription.getSubscriptionDetails().setSubscriptionId(dbUserSubscription.getSubscriptionDetails().getSubscriptionId());
    		userSubscriptionRepository.save(dbUserSubscription);
    	}
    	
    	if (StringUtils.isEmpty(userSubscription.getSubscriptionDetails().getCustomerId())) {
    		Customer customer = saveCustomer(userSubscription);
    		userSubscription.getSubscriptionDetails().setCustomerId(customer.getId());
    	}
    	
    	if (StringUtils.isEmpty(userSubscription.getToken())) {
    		userSubscription.setToken(extractToken(userSubscription.getName()));
    	}
    	
//    	if (StringUtils.isEmpty(userSubscription.getSubscriptionDetails().getSubscriptionId())) {
//    		Subscription subscription = addSpripeSubscription(
//    				userSubscription.getSubscriptionDetails().getCustomerId(),
//    				userSubscription.getSubscriptionDetails().getPriceId());
//    		userSubscription.getSubscriptionDetails().setSubscriptionId(subscription.getId());
//    	}
    	
    	UserSubscription response = userSubscriptionRepository.save(userSubscription);
    	
    	updateCharge(response);
		
    	return response;
	}
    
    
    public UserSubscription addFeeSubscription(UserSubscription userSubscription) throws RuntimeException, StripeException {
    	
    	UserSubscription dbUserSubscription = userSubscriptionRepository.findByEmailIdAndStatus(
    			userSubscription.getEmailId(), TokenStatus.ACTIVE.name());
    	
    	if (dbUserSubscription != null) {
    		throw new RuntimeException("You have already used your Free Subscription. Please change plan to get full access.");
    	}
    	
    	if (StringUtils.isEmpty(userSubscription.getToken())) {
    		userSubscription.setToken(extractToken(userSubscription.getName()));
    	}
    	
//    	if (StringUtils.isEmpty(userSubscription.getSubscriptionDetails().getCustomerId())) {
//    		Customer customer = saveCustomer(userSubscription);
//    		userSubscription.getSubscriptionDetails().setCustomerId(customer.getId());
//    	}
    	
    	    	
    	UserSubscription response = userSubscriptionRepository.save(userSubscription);
    	
    	//updateCharge(response);
    	
    	return response;
    	
    }
    
    public UserSubscription getSubscriptionByEmail(String email) {
    	
    	return userSubscriptionRepository.findByEmailIdAndStatus(email, TokenStatus.ACTIVE.name());
    	
    }
     
    
    public UserSubscription getSubscriptionByCode(String token) {
    	
    	return userSubscriptionRepository.findValidToken(token,LocalDateTime.now());
    }
    
    public UserSubscription findByEmailIdAndTokenAndStatus(String email, String token) {
    	
    	return userSubscriptionRepository.findByEmailIdAndTokenAndStatus(email, token, TokenStatus.ACTIVE.name());
    }
    
    public UserSubscription saveUpdate(UserSubscription userSubscription) {
    	
    	return userSubscriptionRepository.save(userSubscription);
    }


    
    private String extractToken(String name) {
		String[] nameArr = name.split(" ");
		StringBuilder sb = new StringBuilder(7); 
		if (nameArr.length > 1) {
			sb.append(String.valueOf(nameArr[0].charAt(0)).toUpperCase()); 
			sb.append(String.valueOf(nameArr[1].charAt(1)).toUpperCase()); 
		} else {
			sb.append(String.valueOf(nameArr[0].charAt(0)).toUpperCase()); 
			sb.append(String.valueOf(nameArr[0].charAt(1)).toUpperCase()); 
		}
		
        String alphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                                    + "0123456789"
                                    + "abcdefghijklmnopqrstuvxyz";
  
        for (int i = 0; i < 5; i++) { 
            int index = (int)(alphaNumericString.length() * Math.random()); 
            sb.append(alphaNumericString.charAt(index)); 
        } 
		
		return sb.toString() + LocalDateTime.now().getYear();
	}

}
