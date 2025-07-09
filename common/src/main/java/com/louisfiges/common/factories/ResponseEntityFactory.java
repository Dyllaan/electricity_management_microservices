package com.louisfiges.common.factories;

import com.louisfiges.common.http.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public class ResponseEntityFactory {

    public static ResponseEntity<Response> create(Response body, HttpStatus status) {
        return ResponseEntity.status(status).body(body);
    }

    public static ResponseEntity<Response> create(HttpStatus status) {
        return ResponseEntity.status(status).build();
    }

    public static ResponseEntity<Response> create(String cause, HttpStatus status) {
        return ResponseEntity.status(status).body(StringErrorFactory.create(cause));
    }

    public static <T> ResponseEntity<List<T>> create(List<T> body, HttpStatus status) {
        return ResponseEntity.status(status).body(body);
    }
}
