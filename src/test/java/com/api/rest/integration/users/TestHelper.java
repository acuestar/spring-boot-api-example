package com.api.rest.integration.users;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class TestHelper {


    public HttpEntity getRequestHeaders() {
        List<MediaType> acceptTypes = new ArrayList<MediaType>();
        acceptTypes.add(MediaType.APPLICATION_JSON_UTF8);

        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        reqHeaders.setAccept(acceptTypes);

        return new HttpEntity<String>("parameters", reqHeaders);
    }

    public HttpEntity getPostRequestHeaders(String jsonPostBody) {
        List<MediaType> acceptTypes = new ArrayList<MediaType>();
        acceptTypes.add(MediaType.APPLICATION_JSON_UTF8);

        HttpHeaders reqHeaders = new HttpHeaders();
        reqHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
        reqHeaders.setAccept(acceptTypes);

        return new HttpEntity<String>(jsonPostBody, reqHeaders);
    }

    public String userUrlHelper(String resourceUrl, String resourceId) {
        return resourceUrl + "/" + resourceId;
    }

    public JSONObject constructUser(String name, String birthDate) {
        JSONObject userBody = new JSONObject();


        try {
            userBody.put("name",name);
            userBody.put("birthdate", birthDate);
        } catch (JSONException e) {
           return null;
        }

            return userBody;

    }

    public String formatDate(Date date){

        SimpleDateFormat dateFormat=new SimpleDateFormat("dd/MM/yyyy");

        return dateFormat.format(date);

    }

}

