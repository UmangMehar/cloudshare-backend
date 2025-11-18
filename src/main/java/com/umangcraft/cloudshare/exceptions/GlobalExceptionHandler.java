package com.umangcraft.cloudshare.exceptions;

import com.mongodb.DuplicateKeyException;
import com.mongodb.MongoWriteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({DuplicateKeyException.class, MongoWriteException.class})
    public ResponseEntity<?> handleMongoDuplicateKey(Exception ex) {
        Map<String, Object> data = new HashMap<>();
        data.put("status", HttpStatus.CONFLICT.value());
        data.put("message", "Email already exists");
        return ResponseEntity.status(HttpStatus.CONFLICT).body(data);
    }
}
