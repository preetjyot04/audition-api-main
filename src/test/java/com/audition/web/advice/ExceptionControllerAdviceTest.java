package com.audition.web.advice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.audition.common.exception.ResourceNotFoundException;
import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;

@ExtendWith(MockitoExtension.class)
public class ExceptionControllerAdviceTest {

    @InjectMocks
    private ExceptionControllerAdvice exceptionControllerAdvice;

    @Mock
    private SystemException systemException;

    @Mock
    private AuditionLogger logger;

    @Test
    public void testHandleHttpClientExcpetion() {
        HttpClientErrorException exception = mock(HttpClientErrorException.class);
        when(exception.getStatusCode()).thenReturn(HttpStatus.BAD_REQUEST);
        when(exception.getMessage()).thenReturn("Bad Request");
        ResponseEntity<ProblemDetail> response = exceptionControllerAdvice.handleHttpClientException(exception);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Bad Request", response.getBody().getDetail());

    }

    @Test
    public void testHandleMainException() {
        Exception exception = new Exception("Test Exception");
        ResponseEntity<ProblemDetail> response = exceptionControllerAdvice.handleMainException(exception);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void testHandleSystemException() {
        when(systemException.getMessage()).thenReturn("Error");
        when(systemException.getStatusCode()).thenReturn(HttpStatus.INTERNAL_SERVER_ERROR);
        ResponseEntity<ProblemDetail> response = exceptionControllerAdvice.handleSystemException(systemException);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error", response.getBody().getDetail());
    }

    @Test
    public void testHandleResourceNotFoundException() {
        ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");
        ResponseEntity<ProblemDetail> response = exceptionControllerAdvice.handleResourceNotFoundException(exception);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        // verify(logger,times(1)).error(any(),anyString());
    }


}
