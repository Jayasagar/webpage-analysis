package com.webscraping.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ResponseEntity<T> {
    private T data;
    private boolean status;
    private String message;

    public static ResponseEntity error(String message) {
        ResponseEntity responseEntity = new ResponseEntity();
        responseEntity.setMessage(message);
        responseEntity.setStatus(false);
        return responseEntity;
    }

    public static <T> ResponseEntity success(T data) {
        ResponseEntity responseEntity = new ResponseEntity<T>();
        responseEntity.setData(data);
        responseEntity.setStatus(true);
        return responseEntity;
    }
}
