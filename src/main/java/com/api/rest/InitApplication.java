package com.api.rest;

import com.api.rest.repository.CounterRepository;
import com.api.rest.repository.UserRepository;
import com.api.rest.service.CounterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InitApplication {


    public static void main(String[] args) {

        SpringApplication.run(InitApplication.class, args);


    }


}
