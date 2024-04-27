package com.audition.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

import com.audition.common.logging.AuditionLogger;
import com.audition.integration.AuditionIntegrationClient;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.retry.RetryRegistry;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

class AuditionServiceTest {

    @Mock
    private AuditionIntegrationClient integrationClient;

    @Mock
    private RetryRegistry retryRegistry;

    @Mock
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Mock
    private AuditionLogger auditionLogger;

    @InjectMocks
    private AuditionService auditionService;

    @BeforeEach
    void setup() {
        openMocks(this); // Initialize mocks
        //  auditionLogger = new AuditionLogger();
    }

    @Test
    void testGetPosts() {
        // Given
        int userId = 123;
        List<AuditionPost> expectedPosts = new ArrayList<>();
        // When
        List<AuditionPost> result = auditionService.getPosts(userId);
        // Then
        assertEquals(expectedPosts, result);
        verify(auditionLogger, atLeastOnce()).info(any(), any());
    }

    @Test
    void testGetPosts_Success() {
        // Given
        int userId = 123;
        List<AuditionPost> expectedPosts = new ArrayList<>();
        // Create expected post instance
        AuditionPost expectedPost = new AuditionPost();
        expectedPosts.add(expectedPost);
        when(integrationClient.getPosts(userId)).thenReturn(expectedPosts);

        // When
        List<AuditionPost> result = auditionService.getPosts(userId);

        // Then
        // assertEquals(expectedPosts.size(), result.size());
        // assertEquals(expectedPost, result.get(0)); // Compare individual post instance
        verify(auditionLogger, atLeastOnce()).info(any(), any());
    }

    @Test
    void testGetPostById_Fallback() {
        // Given
        String postId = "postId";
        // Simulate failure by returning null from integrationClient
        when(integrationClient.getPostById(postId)).thenReturn(null);

        // When
        AuditionPost result = auditionService.getPostById(postId);

        // Then
        verify(auditionLogger, atLeastOnce()).info(any(), any());
    }

    @Test
    void testGetCommentsForPost_Success() {
        // Given
        String postId = "postId";
        List<Comment> expectedComments = new ArrayList<>();
        when(integrationClient.getCommentsForPost(postId)).thenReturn(expectedComments);

        // When
        List<Comment> result = auditionService.getCommentsForPost(postId);

        // Then
        assertEquals(expectedComments, result);
        verify(auditionLogger, atLeastOnce()).info(any(), any());
    }

    @Test
    void testGetCommentsForPost_Fallback() {
        // Given
        String postId = "postId";
        // Simulate failure by returning null from integrationClient
        when(integrationClient.getCommentsForPost(postId)).thenReturn(null);

        // When
        List<Comment> result = auditionService.getCommentsForPost(postId);

        // Then
        assertEquals(new ArrayList<Comment>(), result); // Fallback should return an empty list
        verify(auditionLogger, atLeastOnce()).info(any(), any());
    }

}
