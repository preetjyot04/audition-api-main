package com.audition.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CommentTest {

    @Mock
    private Comment comment;


    @Test
    void testConstructorWithArguments() {
        // Given
        int postId = 1;
        int id = 2;
        String name = "Audition";
        String email = "audition@example.com";
        String body = "This is Audition application";
        // When
        comment = new Comment(postId, id, name, email, body);
        // Then
        assertEquals(postId, comment.getPostId());
        assertEquals(id, comment.getId());
        assertEquals(name, comment.getName());
        assertEquals(email, comment.getEmail());
        assertEquals(body, comment.getBody());
    }

    @Test
    void testNoArgsConstructor() {
        // Then
        assertEquals(0, comment.getPostId());
        assertEquals(0, comment.getId());
    }


    @Test
    void testGettersAndSetters() {
        // Given
        comment = new Comment();
        // When

        comment.setId(1);
        comment.setName("Audition");
        comment.setBody("Take Auditions");
        comment.setEmail("auditions@example.com");

        // Then
        assertEquals(1, comment.getId());
        assertEquals("Audition", comment.getName());
        assertEquals("Take Auditions", comment.getBody());
        assertEquals("auditions@example.com", comment.getEmail());
    }


}
