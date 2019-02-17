package com.api.rest.integration.users;

import com.api.rest.entities.User;
import com.api.rest.repository.UserRepository;

import org.json.JSONObject;
import org.junit.After;
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

import java.text.SimpleDateFormat;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddUserTest {


    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestHelper testHelper;

    private User createdUser;

    @After
    public void cleanup() {
        if(null != createdUser) {
            userRepository.delete(createdUser);
        }
    }

    @Test
    public void createNewContact() {
        String resourceUrl = "/api/users";
        String name = "Alejandro";
        String birthdate = "26/09/1996";

        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");

        JSONObject postBody = testHelper.constructUser(name, birthdate);

        ResponseEntity<User> responseEntity =
                restTemplate.exchange(resourceUrl, HttpMethod.POST, testHelper.getPostRequestHeaders(postBody.toString()),User.class);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());

        createdUser = responseEntity.getBody();
        assertEquals(name, createdUser.getName());
        assertEquals(birthdate,dateFormat.format(createdUser.getBirthdate()));


        // Check Location HeaderURL
        String expectedLocationUrl = testHelper.userUrlHelper(resourceUrl, createdUser.getId().toString());
        String returnedLocationUrl = responseEntity.getHeaders().getLocation().toString();

        assertThat(returnedLocationUrl, containsString(expectedLocationUrl));
    }

    @Test
    public void createNewUserWithoutName() {
        String resourceUrl = "/api/users";
        String name="";
        String birthdate = "26/09/1996";

        JSONObject postBody = testHelper.constructUser(name, birthdate);

        ResponseEntity<User> responseEntity =
                restTemplate.exchange(resourceUrl, HttpMethod.POST, testHelper.getPostRequestHeaders(postBody.toString()), User.class);

        assertEquals(422, responseEntity.getStatusCodeValue());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
    }

    @Test
    public void createNewContactWithoutBirthDate() {
        String resourceUrl = "/api/users";
        String name = "Alejandro";


        JSONObject postBody = testHelper.constructUser(name, null);

        ResponseEntity<User> responseEntity =
                restTemplate.exchange(resourceUrl, HttpMethod.POST, testHelper.getPostRequestHeaders(postBody.toString()), User.class);

        assertEquals(422, responseEntity.getStatusCodeValue());
        assertEquals(MediaType.APPLICATION_JSON_UTF8, responseEntity.getHeaders().getContentType());
    }



}
