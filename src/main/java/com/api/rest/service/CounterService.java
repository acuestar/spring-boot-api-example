package com.api.rest.service;

import com.api.rest.entities.Counter;
import com.api.rest.repository.CounterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import static org.springframework.data.mongodb.core.FindAndModifyOptions.options;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Service
public class CounterService {

    private final MongoOperations mongo;

    private final CounterRepository counterRepository;

    @Autowired
    public CounterService(CounterRepository counterRepository, MongoOperations mongo) {
        this.counterRepository = counterRepository;
        this.mongo = mongo;
    }


    public int getNextSequence(String collectionName) {

        Counter counter = mongo.findAndModify(
                query(where("_id").is(collectionName)),
                new Update().inc("seq", 1),
                options().returnNew(true),
                Counter.class);

        if (counter == null) counter = createNewCounter(collectionName);

        return counter.getSeq();
    }


    private Counter createNewCounter(String collectionName) {

        Counter counter = new Counter(collectionName, 1);
        return counterRepository.save(counter);


    }

}
