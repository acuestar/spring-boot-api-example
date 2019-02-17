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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GetUserTest {


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
    public void getUser() {
        String resourceUrl = "/api/users/" + user.getId();
        ResponseEntity<User> responseEntity = restTemplate.exchange(resourceUrl, HttpMethod.GET, testHelper.getRequestHeaders(), User.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());

        User parsedUser = responseEntity.getBody();
        assertEquals(user.getId(), parsedUser.getId());
        assertEquals(user.getName(), parsedUser.getName());
        assertEquals(testHelper.formatDate(user.getBirthdate()), testHelper.formatDate(parsedUser.getBirthdate()));
    }


    @Test
    public void handleNotFound() {
        String resourceUrl = "/api/users/1345412";
        ResponseEntity<User> responseEntity = restTemplate.exchange(resourceUrl, HttpMethod.GET, testHelper.getRequestHeaders(), User.class);

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
    }

}
