package com.audition.logger;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.audition.common.logging.AuditionLogger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.http.ProblemDetail;

class AuditionLoggerTest {

    @Mock
    Logger logger;
    @InjectMocks
    AuditionLogger auditionLogger;

    @BeforeEach
    void setup() {
        openMocks(this); // Initialize mocks
        //  auditionLogger = new AuditionLogger();
    }

    @Test
    void testInfoWithMessage() {
        // Mocking isInfoEnabled() to return true
        when(logger.isInfoEnabled()).thenReturn(true);

        auditionLogger.info(logger, "Test message");

        // Verify that logger.info(String) is called once
        verify(logger, times(1)).info("Test message");
    }

    @Test
    void testInfoWithMessageAndObject() {
        // Mocking isInfoEnabled() to return true
        when(logger.isInfoEnabled()).thenReturn(true);

        auditionLogger.info(logger, "Test message with object", "Object");

        // Verify that logger.info(String, Object) is called once
        verify(logger, times(1)).info("Test message with object", "Object");
    }


    @Test
    void testDebug() {
        when(logger.isDebugEnabled()).thenReturn(true);

        auditionLogger.debug(logger, "Test debug message");

        verify(logger, times(1)).debug("Test debug message");
    }

    @Test
    void testWarn() {
        when(logger.isWarnEnabled()).thenReturn(true);

        auditionLogger.warn(logger, "Test warn message");

        verify(logger, times(1)).warn("Test warn message");
    }

    @Test
    void testError() {
        when(logger.isErrorEnabled()).thenReturn(true);

        auditionLogger.error(logger, "Test error message");

        verify(logger, times(1)).error("Test error message");
    }

    @Test
    void testLogErrorWithException() {
        Exception exception = new RuntimeException("Test exception");

        when(logger.isErrorEnabled()).thenReturn(true);

        auditionLogger.logErrorWithException(logger, "Test message with exception", exception);

        verify(logger, times(1)).error("Test message with exception", exception);
    }


    @Test
    void testLogHttpStatusCodeError() {
        when(logger.isErrorEnabled()).thenReturn(true);

        auditionLogger.logHttpStatusCodeError(logger, "Test message", 404);

        verify(logger, times(1)).error("Error - Code 404, Message: Test message\n");
    }

    @Test
    void testLogStandardProblemDetail() {
        ProblemDetail problemDetail = mock(ProblemDetail.class);
        when(problemDetail.getTitle()).thenReturn("Title");
        when(problemDetail.getDetail()).thenReturn("Detail");
        when(problemDetail.getStatus()).thenReturn(500);

        Exception exception = new RuntimeException("Test exception");

        when(logger.isErrorEnabled()).thenReturn(true);

        auditionLogger.logStandardProblemDetail(logger, problemDetail, exception);

        verify(logger, times(1)).error("Problem Detail - Title: Title, Detail: Detail, Status: 500", exception);
    }
}
