package com.api.rest.integration.users;

import com.api.rest.entities.User;
import com.api.rest.repository.UserRepository;
import com.api.rest.service.CounterService;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONObject;
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
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Calendar;
import java.util.GregorianCalendar;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UpdateUserPut {



    @Autowired
    private TestRestTemplate restTemplate;
    

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CounterService counterService;

    @Autowired
    private TestHelper testHelper;

    private User user;

    private RestTemplate putRestTemplate;


    @Before
    public void setup() {
        user = userRepository.save(new User(counterService.getNextSequence(User.COLLECTION_NAME),
                "Alejandro", new GregorianCalendar(1996, Calendar.SEPTEMBER, 26).getTime()));

        this.putRestTemplate = restTemplate.getRestTemplate();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        this.putRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        
    }


    @After
    public void afterAllTests() {
        userRepository.delete(user);
    }


    @Test
    public void updateUserWithPut() throws Throwable {
        String resourceUrl = "/api/users/" + user.getId();

        JSONObject updateBody = new JSONObject();
        updateBody.put("name", "Test");
        updateBody.put("birthdate", "01/05/1998");

        ResponseEntity<User> responseEntity =
                putRestTemplate.exchange(resourceUrl, HttpMethod.PUT, testHelper.getPostRequestHeaders(updateBody.toString()), User.class);

        // Check for proper status
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());

        // Check for proper body
        User updatedUser = responseEntity.getBody();
        assertEquals("Test", updatedUser.getName());
        assertEquals("01/05/1998", testHelper.formatDate(updatedUser.getBirthdate()));
    }


    @Test
    public void updateNonExistantUser() throws Throwable {
        String resourceUrl = "/api/users/456465";

        JSONObject updateBody = new JSONObject();
        updateBody.put("name", "Test");
        updateBody.put("birthdate", "01/05/1998");

        ResponseEntity<User> responseEntity =
                putRestTemplate.exchange(resourceUrl, HttpMethod.PUT, testHelper.getPostRequestHeaders(updateBody.toString()), User.class);

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
    }


}
