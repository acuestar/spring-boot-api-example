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
public class UpdateUserPatchTest {


    @Autowired
    private TestRestTemplate restTemplate;

    private RestTemplate patchRestTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CounterService counterService;

    @Autowired
    private TestHelper testHelper;

    private User user;
    private User user2;


    @Before
    public void setup() {
        user = userRepository.save(new User(counterService.getNextSequence(User.COLLECTION_NAME),
                "Alejandro", new GregorianCalendar(1996, Calendar.SEPTEMBER, 26).getTime()));

        user2 = userRepository.save(new User(counterService.getNextSequence(User.COLLECTION_NAME),
                "Test", new GregorianCalendar(1994, Calendar.APRIL, 6).getTime()));

        this.patchRestTemplate = restTemplate.getRestTemplate();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();
        this.patchRestTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
    }


    @After
    public void afterAllTests() {
        userRepository.delete(user);
        userRepository.delete(user2);
    }

    @Test
    public void updateUserOneWithPatch() throws Throwable {
        String resourceUrl = "/api/users/" + user.getId().toString();

        JSONObject updateBody = new JSONObject();
        updateBody.put("name", "Pedro");

        ResponseEntity<User> responseEntity =
                patchRestTemplate.exchange(resourceUrl, HttpMethod.PATCH, testHelper.getPostRequestHeaders(updateBody.toString()), User.class);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());

        User updatedUser = responseEntity.getBody();
        assertEquals("Pedro", updatedUser.getName());
        assertEquals(testHelper.formatDate(new GregorianCalendar(1996, Calendar.SEPTEMBER, 26).getTime()), testHelper.formatDate(updatedUser.getBirthdate()));
    }


    @Test
    public void updateUserWithPatchAndInvalidName() throws Throwable {
        String resourceUrl = "/api/users/" + user.getId();

        JSONObject updateBody = new JSONObject();
        updateBody.put("name", "");

        ResponseEntity<User> responseEntity =
                patchRestTemplate.exchange(resourceUrl, HttpMethod.PATCH, testHelper.getPostRequestHeaders(updateBody.toString()),User.class);
        assertEquals(422, responseEntity.getStatusCodeValue());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
    }


    @Test
    public void updateUserWithPatchAndInvalidBirthDate() throws Throwable {
        String resourceUrl = "/api/users/" + user.getId();

        JSONObject updateBody = new JSONObject();
        updateBody.put("birthdate", "25/04/2020");

        ResponseEntity<User> responseEntity =
                patchRestTemplate.exchange(resourceUrl, HttpMethod.PATCH, testHelper.getPostRequestHeaders(updateBody.toString()),User.class);




        assertEquals(422, responseEntity.getStatusCodeValue());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
    }

    @Test
    public void updateNonExistantUser() throws Throwable {
        String resourceUrl = "/api/users/456465";

        JSONObject updateBody = new JSONObject();
        updateBody.put("name", "Pedro");
        ResponseEntity<User> responseEntity =
                patchRestTemplate.exchange(resourceUrl, HttpMethod.PATCH, testHelper.getPostRequestHeaders(updateBody.toString()),User.class);

        assertEquals(404, responseEntity.getStatusCodeValue());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
    }


}
