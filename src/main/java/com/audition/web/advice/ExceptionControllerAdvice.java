package com.audition.web.advice;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import com.audition.common.exception.ResourceNotFoundException;
import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@ControllerAdvice
public class ExceptionControllerAdvice extends ResponseEntityExceptionHandler {

    public static final String DEFAULT_TITLE = "API Error Occurred";
    static final Logger LOG = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    //private static final String ERROR_MESSAGE = " Error Code from Exception could not be mapped to a valid HttpStatus Code - ";
    private static final String DEFAULT_MESSAGE = "API Error occurred. Please contact support or administrator.";

    @Autowired
    private transient AuditionLogger logger;


    @ExceptionHandler(HttpClientErrorException.class)
    ResponseEntity<ProblemDetail> handleHttpClientException(final HttpClientErrorException e) {
        logger.error(LOG, getMessageFromException(e));
        final HttpStatusCode status = getHttpStatusCodeFromException(e);
        final ProblemDetail problemDetail = createProblemDetail(e, status);
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setDetail(e.getMessage());
        return new ResponseEntity<>(problemDetail, status);
    }


    @ExceptionHandler(Exception.class)
    ResponseEntity<ProblemDetail> handleMainException(final Exception e) {
        logger.error(LOG, getMessageFromException(e));
        final HttpStatusCode status = getHttpStatusCodeFromException(e);
        final ProblemDetail problemDetail = createProblemDetail(e, status);
        problemDetail.setTitle("Internal Server Error");
        problemDetail.setDetail(e.getMessage());
        return new ResponseEntity<>(problemDetail, status);
    }

    @ExceptionHandler(SystemException.class)
    ResponseEntity<ProblemDetail> handleSystemException(final SystemException e) {
        logger.error(LOG, getMessageFromException(e));
        final HttpStatusCode statusCode = getHttpStatusCodeFromSystemException(e);
        final ProblemDetail problemDetail = createProblemDetail(e, statusCode);
        problemDetail.setDetail(e.getMessage());
        return new ResponseEntity<>(problemDetail, statusCode);

    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(final ResourceNotFoundException e) {
        logger.error(LOG, getMessageFromException(e));
        final ProblemDetail problemDetail = createProblemDetail(e, HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(problemDetail, HttpStatus.NOT_FOUND);
    }


    private ProblemDetail createProblemDetail(final Exception exception,
        final HttpStatusCode statusCode) {
        logger.error(LOG, getMessageFromException(exception));
        final ProblemDetail problemDetail = ProblemDetail.forStatus(statusCode);
        problemDetail.setDetail(getMessageFromException(exception));
        if (exception instanceof SystemException) {
            problemDetail.setTitle(((SystemException) exception).getTitle());
        } else {
            problemDetail.setTitle(DEFAULT_TITLE);
        }
        return problemDetail;
    }

    private String getMessageFromException(final Exception exception) {
        if (StringUtils.isNotBlank(exception.getMessage())) {
            return exception.getMessage();
        }
        return DEFAULT_MESSAGE;
    }

    private HttpStatusCode getHttpStatusCodeFromSystemException(final SystemException exception) {
        logger.error(LOG, getMessageFromException(exception));
        try {
            return exception.getStatusCode();
        } catch (final IllegalArgumentException iae) {
            // logger.info(LOG, ERROR_MESSAGE + exception.getStatusCode());
            return INTERNAL_SERVER_ERROR;
        }
    }

    private HttpStatusCode getHttpStatusCodeFromException(final Exception exception) {
        logger.error(LOG, getMessageFromException(exception));
        if (exception instanceof HttpClientErrorException) {
            return ((HttpClientErrorException) exception).getStatusCode();
        } else if (exception instanceof HttpRequestMethodNotSupportedException) {
            return METHOD_NOT_ALLOWED;
        }
        return INTERNAL_SERVER_ERROR;
    }


}



