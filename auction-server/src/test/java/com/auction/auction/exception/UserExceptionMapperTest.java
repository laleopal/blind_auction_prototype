package com.auction.auction.exception;

import jdk.jfr.Description;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class UserExceptionMapperTest {

    private final AuctionExceptionMapper exceptionMapper = new AuctionExceptionMapper();

    @Test
    @Description("test exception mapper when WithNoSuchElementException is thrown")
    public void testHandleGenericException_WithNoSuchElementException() {
        NoSuchElementException exception = new NoSuchElementException("User not found");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> responseEntity = exceptionMapper.handleGenericException(exception, request);

        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertEquals("Requested item not found", responseEntity.getBody());
    }

    @Test
    @Description("test exception mapper when generic exception is thrown")
    public void testHandleGenericException_WithGenericException() {
        Exception exception = new Exception("Some error");
        WebRequest request = mock(WebRequest.class);

        ResponseEntity<Object> responseEntity = exceptionMapper.handleGenericException(exception, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals("An error occurred: Some error", responseEntity.getBody());
    }
}
