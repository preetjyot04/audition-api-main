package com.audition.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class SystemExceptionTest {

    @Test
    void testDefaultConstructor() {
        // When
        SystemException exception = new SystemException();

        // Then
        assertNull(exception.getMessage());
        assertNull(exception.getTitle());
        assertNull(exception.getStatusCode());
        assertNull(exception.getDetail());
    }

    @Test
    void testConstructorWithMessage() {
        // Given
        String message = "Test message";

        // When
        SystemException exception = new SystemException(message);

        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertNull(exception.getStatusCode());
        assertNull(exception.getDetail());
    }

    @Test
    void testConstructorWithMessageAndStatus() {
        // Given
        String message = "Test message";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // When
        SystemException exception = new SystemException(message, status);

        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertEquals(status, exception.getStatusCode());
        assertNull(exception.getDetail());
    }

    @Test
    void testConstructorWithMessageAndException() {
        // Given
        String message = "Test message";
        Exception innerException = new Exception();

        // When
        SystemException exception = new SystemException(message, innerException);

        // Then
        assertEquals(message, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertNull(exception.getStatusCode());
        assertNull(exception.getDetail());
    }

    @Test
    void testConstructorWithDetailTitleStatus() {
        // Given
        String detail = "Test detail";
        String title = "Custom Title";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        // When
        SystemException exception = new SystemException(detail, title, status);

        // Then
        assertEquals(detail, exception.getMessage());
        assertEquals(title, exception.getTitle());
        assertEquals(status, exception.getStatusCode());
        assertEquals(detail, exception.getDetail());
    }

    @Test
    void testConstructorWithDetailTitleException() {
        // Given
        String detail = "Test detail";
        String title = "Custom Title";
        Exception innerException = new Exception();

        // When
        SystemException exception = new SystemException(detail, title, innerException);

        // Then
        assertEquals(detail, exception.getMessage());
        assertEquals(title, exception.getTitle());
        assertNull(exception.getStatusCode());
        assertEquals(detail, exception.getDetail());
    }

    @Test
    void testConstructorWithDetailStatusException() {
        // Given
        String detail = "Test detail";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Exception innerException = new Exception();

        // When
        SystemException exception = new SystemException(detail, status, innerException);

        // Then
        assertEquals(detail, exception.getMessage());
        assertEquals(SystemException.DEFAULT_TITLE, exception.getTitle());
        assertEquals(status, exception.getStatusCode());
        assertEquals(detail, exception.getDetail());
    }

    @Test
    void testConstructorWithDetailTitleStatusException() {
        // Given
        String detail = "Test detail";
        String title = "Custom Title";
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Exception innerException = new Exception();

        // When
        SystemException exception = new SystemException(detail, title, status, innerException);

        // Then
        assertEquals(detail, exception.getMessage());
        assertEquals(title, exception.getTitle());
        assertEquals(status, exception.getStatusCode());
        assertEquals(detail, exception.getDetail());
    }
}
