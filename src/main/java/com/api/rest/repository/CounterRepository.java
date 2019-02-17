package com.api.rest.repository;

import com.api.rest.entities.Counter;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterRepository extends MongoRepository<Counter, Integer> {
}
