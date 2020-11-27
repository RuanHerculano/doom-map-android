package com.e.doommap;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

public class RequestCreator {
    public  String              url;
    public  JSONObject          json = new JSONObject();

    public RequestCreator(){}

    public ResponseEntity<String> getRequest(HttpMethod method, Context context){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new StringHttpMessageConverter());

        try {
            return template.exchange(url, method, entity, String.class);
        } catch (Exception e) {
            return null;
        }
    }

    public ResponseEntity<String> postRequest(HttpMethod method, Context context){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        JSONArray arr = new JSONArray();
        arr.put(json);
        System.out.println("JSON: " + arr);
        HttpEntity<String> entity = new HttpEntity<>(arr.toString(), headers);

        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new StringHttpMessageConverter());

        try {
            return template.exchange(url, method, entity, String.class);
        } catch (HttpStatusCodeException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
            return null;
        }
    }

    public ResponseEntity<String> deleteRequest(HttpMethod method, Context context){

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        RestTemplate template = new RestTemplate();
        template.getMessageConverters().add(new StringHttpMessageConverter());

        try {
            return template.exchange(url, method, entity, String.class);
        } catch (HttpStatusCodeException e) {
            System.out.println(e.getStatusCode());
            System.out.println(e.getResponseBodyAsString());
            return null;
        }
    }
}