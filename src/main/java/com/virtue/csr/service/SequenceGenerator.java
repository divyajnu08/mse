package com.virtue.csr.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import com.virtue.csr.exception.ApplicationException;
import com.virtue.csr.model.DBSequence;

public class SequenceGenerator {
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	public long getNextSequenceId(String key) throws ApplicationException {

        //get sequence id
        Query query = new Query(Criteria.where("_id").is(key));

        //increase sequence id by 1
        Update update = new Update();
        update.inc("seq", 1);

        //return new increased id
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true).upsert(true);

        //this is the magic happened
        DBSequence sequenceId = mongoTemplate.findAndModify(query, update, options, DBSequence.class);

        //if no id, throws SequenceException
        //optional, just a way to tell user when the sequence id is failed to generate.
        if (sequenceId == null) {
            throw new ApplicationException("Unable to get sequence id for key " + key);
        }

        return sequenceId.getSeq();
    }

}
