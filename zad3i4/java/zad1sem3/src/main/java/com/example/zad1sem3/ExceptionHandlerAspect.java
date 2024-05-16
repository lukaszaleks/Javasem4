package com.example.zad1sem3;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Aspect
@Component
public class ExceptionHandlerAspect {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAspect.class);

    @AfterThrowing(pointcut = "execution(* com.example.zad1sem3.*Controller.*(..)) || execution(* com.example.zad1sem3.*Service.*(..))", throwing = "ex")
    public ResponseEntity<String> handleException(JoinPoint joinPoint, Exception ex) {
        // Logowanie wyjątku
        logger.error("Exception occurred in method: " + joinPoint.getSignature().toShortString(), ex);

        // Zwracanie odpowiedzi HTTP z odpowiednim kodem błędu
        if (ex instanceof NullPointerException || ex instanceof NoSuchElementException) {
            return new ResponseEntity<>("Bad request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Internal server error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
