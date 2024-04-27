package com.audition.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AuditionPostTest {

    @Mock
    private List<Comment> mockComments;

    @InjectMocks
    private AuditionPost post;


    @Test
    void testGettersAndSetters() {
        // Given
        post = new AuditionPost();

        // When
        post.setUserId(1);
        post.setId(100);
        post.setTitle("Test Title");
        post.setBody("Test Body");

        // Then
        assertEquals(1, post.getUserId());
        assertEquals(100, post.getId());
        assertEquals("Test Title", post.getTitle());
        assertEquals("Test Body", post.getBody());
    }

    @Test
    void testNoArgsConstructor() {
        // Then
        assertNotNull(post);
    }

    @Test
    void testAllArgsConstructor() {
        // Given
        List<Comment> comments = Arrays.asList(new Comment(), new Comment());

        // When
        post = new AuditionPost(1, 100, "Test Title", "Test Body", comments);

        // Then
        assertEquals(1, post.getUserId());
        assertEquals(100, post.getId());
        assertEquals("Test Title", post.getTitle());
        assertEquals("Test Body", post.getBody());
        assertEquals(comments, post.getComments());
    }

    @Test
    void testJsonIncludeNonNull() {
        // Given
        Class<AuditionPost> clazz = AuditionPost.class;

        // When
        JsonInclude jsonIncludeAnnotation = clazz.getAnnotation(JsonInclude.class);

        // Then
        if (jsonIncludeAnnotation != null) {
            assertEquals(JsonInclude.Include.NON_NULL, jsonIncludeAnnotation.value());
        }
    }


    @Test
    void testMockComments() {
        // Given
        post.setComments(mockComments);

        // When
        List<Comment> actualComments = post.getComments();

        // Then
        assertEquals(mockComments, actualComments);
    }
}
