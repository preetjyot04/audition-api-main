package com.audition.configuration;

import com.audition.AuditionApplication;
import com.audition.common.logging.AuditionLogger;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

@Component
public class LoggingInterceptor implements ClientHttpRequestInterceptor {

    transient AuditionLogger logger;
    private static final Logger LOG = LoggerFactory.getLogger(AuditionApplication.class);


    public LoggingInterceptor() {

    }


    public LoggingInterceptor(final AuditionLogger auditionLogger) {
        this.logger = auditionLogger;
    }

    @Override
    public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
        final ClientHttpRequestExecution execution)
        throws IOException {
        logRequest(request, body);
        final ClientHttpResponse response = execution.execute(request, body);
        logResponse(response);
        return response;
    }

    public void logRequest(final HttpRequest request, final byte[] body) throws IOException {

        logger.debug(LOG, "===========================request begin================================================");
        logger.debug(LOG, "URI         : {}" + request.getURI());
        logger.debug(LOG, "Method      : {}" + request.getMethod());
        logger.debug(LOG, "Headers     : {}" + request.getHeaders());
        logger.debug(LOG, "Request body: {}" + new String(body, StandardCharsets.UTF_8));
        logger.debug(LOG, "==========================request end================================================");


    }


    public void logResponse(final ClientHttpResponse response) throws IOException {
        logger.debug(LOG, "============================response begin==========================================");
        logger.debug(LOG, "Status code  : {}" + response.getStatusCode());
        logger.debug(LOG, "Status text  : {}" + response.getStatusText());
        logger.debug(LOG, "Headers      : {}" + response.getHeaders());
        logger.debug(LOG, "Response body: {}" + getResponseBody(response));
        logger.debug(LOG, "=======================response end=================================================");
    }


    public String getResponseBody(final ClientHttpResponse response) throws IOException {
        final StringBuilder inputStringBuilder = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(
            new InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            String line;
            while (true) {
                line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                inputStringBuilder.append(line);
            }
        }
        return inputStringBuilder.toString();
    }
}
