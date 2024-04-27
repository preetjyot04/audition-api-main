package com.audition.service;

import com.audition.common.logging.AuditionLogger;
import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.decorators.Decorators;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryRegistry;
import io.vavr.control.Try;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuditionService {

    @Autowired
    private transient AuditionIntegrationClient auditionIntegrationClient;
    @Autowired

    private transient RetryRegistry retryRegistry;
    @Autowired

    private transient CircuitBreakerRegistry circuitBreakerRegistry;

    private static final Logger LOG = LoggerFactory.getLogger(AuditionService.class);

    @Autowired
    private transient AuditionLogger auditionLogger;


    public List<AuditionPost> getPosts(final Integer userId) {
        auditionLogger.info(LOG, "getPosts {} " + userId);
        return executeWithResilience(
            () -> auditionIntegrationClient.getPosts(userId),
            this::fallBackForGetUserId

        );
    }


    public AuditionPost getPostById(final String postId) {
        auditionLogger.info(LOG, "getPostById " + postId);

        return executeWithResilience(
            () -> auditionIntegrationClient.getPostById(postId),
            this::fallBackForGetPostById
        );
    }


    public List<Comment> getCommentsForPost(final String postId) {
        auditionLogger.info(LOG, "getCommentsForPost " + postId);

        return executeWithResilience(
            () -> auditionIntegrationClient.getCommentsForPost(postId),
            this::fallBackForGetCommentsForPost
        );
    }


    public <T> T executeWithResilience(final Supplier<T> supplier, final Supplier<T> fallback) {
        auditionLogger.info(LOG, "executeWithResilience ");

        final Retry retry = retryRegistry.retry("auditionClient");
        final CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("auditionClient");

        final Supplier<T> decoratedSupplier = Decorators.ofSupplier(supplier)
            .withRetry(retry)
            .withCircuitBreaker(circuitBreaker)
            .decorate();
        return Try.ofSupplier(decoratedSupplier)
            .recover(throwable -> fallback.get())
            .get();
    }

    private List<AuditionPost> fallBackForGetUserId() {
        auditionLogger.info(LOG, "fallBackForGetUserId ");
        return new ArrayList<>();
    }

    private AuditionPost fallBackForGetPostById() {
        auditionLogger.info(LOG, "fallBackForGetPostById ");
        return new AuditionPost();
    }

    private List<Comment> fallBackForGetCommentsForPost() {
        auditionLogger.info(LOG, "fallBackForGetCommentsForPost ");
        return new ArrayList<>();
    }


}
