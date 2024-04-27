package com.audition.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.audition.common.exception.ResourceNotFoundException;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class AuditionIntegrationClientTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private AuditionLogger logger;

    @InjectMocks
    private AuditionIntegrationClient auditionIntegrationClient;

    @Test
    void testGetPosts_WithUserId() {
        // Mocking restTemplate.exchange() method to return a ResponseEntity with some data
        List<AuditionPost> posts = new ArrayList<AuditionPost>();
        posts.add(new AuditionPost());
        ResponseEntity<List<AuditionPost>> responseEntity = new ResponseEntity<>(posts, HttpStatus.OK);
        ParameterizedTypeReference<List<AuditionPost>> responseType = new ParameterizedTypeReference<List<AuditionPost>>() {
        };

        // Stubbing the exchange method to return the mocked ResponseEntity
        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(),
            eq(responseType)
        ))
            .thenReturn(responseEntity);

        // Calling the method under test
        List<AuditionPost> result = auditionIntegrationClient.getPosts(123);

        // Verifying that the method returns the expected result
        assertEquals(posts, result);

        // Verifying that the logger is called at least once with appropriate messages
        verify(logger, atLeastOnce()).info(any(), anyString());
        verify(logger, atLeastOnce()).debug(any(), anyString());
    }

    @Test
    void testGetPosts_WithoutUserId() {
        // Mocking restTemplate.exchange() method to return a ResponseEntity with some data
        List<AuditionPost> posts = new ArrayList<>();
        posts.add(new AuditionPost());
        ResponseEntity<List<AuditionPost>> responseEntity = new ResponseEntity<>(posts, HttpStatus.OK);
        ParameterizedTypeReference<List<AuditionPost>> responseType = new ParameterizedTypeReference<List<AuditionPost>>() {
        };

        // Stubbing the exchange method to return the mocked ResponseEntity
        when(restTemplate.exchange(
            eq("https://jsonplaceholder.typicode.com/posts"), // Expecting the URL without userId
            eq(HttpMethod.GET),
            any(),
            eq(responseType)
        ))
            .thenReturn(responseEntity);

        // Calling the method under test without providing a userId
        List<AuditionPost> result = auditionIntegrationClient.getPosts(null);

        // Verifying that the method returns the expected result
        assertEquals(posts, result);

        // Verifying that the logger is called at least once with appropriate messages
        verify(logger, atLeastOnce()).info(any(), anyString());
        verify(logger, atLeastOnce()).debug(any(), anyString());
    }

    @Test
    void testGetCommentsForPost_Success() {
        ParameterizedTypeReference<List<Comment>> responseType = new ParameterizedTypeReference<List<Comment>>() {
        };

        // Mocking the restTemplate
        List<Comment> expectedComments = new ArrayList<>();
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(responseType)))
            .thenReturn(new ResponseEntity<>(expectedComments, HttpStatus.OK));

        // Calling the method under test
        List<Comment> result = auditionIntegrationClient.getCommentsForPost("1");

        // Verifying the behavior
        assertNotNull(result);
        assertEquals(expectedComments, result);
        verify(logger, atLeastOnce()).info(any(), anyString());
        verify(logger, atLeastOnce()).debug(any(), anyString());
    }

    @Test
    void testGetPostWithComments_Success() {
        ParameterizedTypeReference<List<Comment>> responseType = new ParameterizedTypeReference<List<Comment>>() {
        };

        // Mocking the restTemplate
        List<Comment> expectedComments = new ArrayList<>();
        when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), any(), eq(responseType)))
            .thenReturn(new ResponseEntity<>(expectedComments, HttpStatus.OK));

        // Calling the method under test
        List<Comment> result = auditionIntegrationClient.getPostWithComments("1");

        // Verifying the behavior
        assertNotNull(result);
        assertEquals(expectedComments, result);
        verify(logger, atLeastOnce()).info(any(), anyString());
        verify(logger, atLeastOnce()).debug(any(), anyString());
    }

    @Test
    void testGetPostById_NotFound() {
        // Mocking the restTemplate to throw HttpClientErrorException with status NOT_FOUND
        when(restTemplate.getForObject(anyString(), eq(AuditionPost.class)))
            .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Calling the method under test and expecting ResourceNotFoundException
        assertThrows(ResourceNotFoundException.class, () -> auditionIntegrationClient.getPostById("1"));
    }
}
