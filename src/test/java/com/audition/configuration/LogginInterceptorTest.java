package com.audition.configuration;

import static org.mockito.MockitoAnnotations.openMocks;

import com.audition.common.logging.AuditionLogger;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpRequest;

class LogginInterceptorTest {

    @Mock
    private AuditionLogger logger;

    @Mock
    private HttpRequest request;

    @InjectMocks
    LoggingInterceptor loggingInterceptor;


    @BeforeEach
    void setup() {
        openMocks(this); // Initialize mocks
        //  auditionLogger = new AuditionLogger();
    }


}
