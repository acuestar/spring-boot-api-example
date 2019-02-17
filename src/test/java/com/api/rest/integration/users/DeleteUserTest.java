package com.api.rest.integration.users;


import com.api.rest.entities.User;
import com.api.rest.repository.UserRepository;
import com.api.rest.service.CounterService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static junit.framework.TestCase.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DeleteUserTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CounterService counterService;

    @Autowired
    private TestHelper testHelper;

    private User user;

    @Before
    public void setup() {
        user = userRepository.save(new User(counterService.getNextSequence(User.COLLECTION_NAME),
                "Alejandro", new GregorianCalendar(1996, Calendar.SEPTEMBER, 26).getTime()));
    }

    @After
    public void afterAllTests() {
        userRepository.delete(user);
    }

    @Test
    public void deleteUser() {
        String resourceUrl = "/api/users/" + user.getId();

        ResponseEntity<User> responseEntity = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, testHelper.getRequestHeaders(), User.class);

        assertEquals(204, responseEntity.getStatusCodeValue());

    }

    @Test
    public void deleteNonExistantUser() {
        String resourceUrl = "/api/users/548979";

        ResponseEntity<User> responseEntity = restTemplate.exchange(resourceUrl, HttpMethod.DELETE, testHelper.getRequestHeaders(), User.class);

        // Check for proper status
        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
    }

}
