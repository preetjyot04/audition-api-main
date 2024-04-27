package com.audition.web;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.audition.common.exception.ResourceNotFoundException;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.service.AuditionService;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
class AuditionControllerTest {

    @Mock
    private AuditionService auditionService;

    @InjectMocks
    private AuditionController auditionController;
    @Mock
    private AuditionLogger logger;


    @Test
    void testGetPostById() {
        // Given
        int postId = 1;
        AuditionPost expectedPost = new AuditionPost();
        expectedPost.setId(postId);
        when(auditionService.getPostById(postId + "")).thenReturn(expectedPost);

        // When
        AuditionPost actualPost = auditionController.getPostById(postId + "");

        // Then
        assertEquals(expectedPost.getId(), actualPost.getId());
    }

    @Test
    void testGetPostByIdWithInvalidPostId() {
        // Given
        String invalidPostId = "abc";

        // When/Then
        assertThrows(ResourceNotFoundException.class, () -> auditionController.getPostById(invalidPostId));
    }

    @Test
    void testGetCommentsForPost() {
        // Given
        String postId = "1";
        List<Comment> expectedComments = Arrays.asList(new Comment(), new Comment());
        when(auditionService.getCommentsForPost(postId)).thenReturn(expectedComments);

        // When
        ResponseEntity<List<Comment>> responseEntity = auditionController.getCommentsForPost(postId);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedComments.size(), responseEntity.getBody().size());
    }


    @Test
    void testGetPosts_WithUserId() {
        // Given
        Integer userId = 123;
        List<AuditionPost> expectedPosts = Collections.singletonList(new AuditionPost());
        when(auditionService.getPosts(userId)).thenReturn(expectedPosts);

        // When
        List<AuditionPost> actualPosts = auditionController.getPosts(userId);

        // Then
        assertEquals(expectedPosts, actualPosts);
        verify(logger).info(any(Logger.class), eq("Retrieving posts"));
        //verify(logger).getPosts(userId);
    }

    @Test
    void testGetPosts_WithoutUserId() {
        // Given
        List<AuditionPost> expectedPosts = Collections.singletonList(new AuditionPost());
        when(auditionService.getPosts(null)).thenReturn(expectedPosts);

        // When
        List<AuditionPost> actualPosts = auditionController.getPosts(null);

        // Then
        assertEquals(expectedPosts, actualPosts);
        verify(logger).info(any(Logger.class), eq("Retrieving posts"));
        verify(auditionService).getPosts(null);
    }

    @Test
    void testGetCommentsForPost_InvalidPostId() {
        // Given
        String postId = "abc";

        // When & Then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
            () -> auditionController.getCommentsForPost(postId));
        assertEquals("Post id should be number. Current id is " + postId, exception.getMessage());
        verify(auditionService, never()).getCommentsForPost(postId);
    }
}
