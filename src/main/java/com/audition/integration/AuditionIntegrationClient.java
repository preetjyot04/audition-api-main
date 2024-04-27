package com.audition.integration;

import com.audition.common.exception.ResourceNotFoundException;
import com.audition.common.exception.SystemException;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


@Component
public class AuditionIntegrationClient {

    @Autowired
    private transient RestTemplate restTemplate;

    @Autowired
    transient AuditionLogger logger;

    static final Logger LOG = LoggerFactory.getLogger(AuditionIntegrationClient.class);


    public List<AuditionPost> getPosts(final Integer userId) {
        logger.info(LOG, "Entering in getPosts");
        String url;
        if (userId == null) {
            url = "https://jsonplaceholder.typicode.com/posts";

        } else {
            url = "https://jsonplaceholder.typicode.com/posts?userId=" + userId;
        }

        logger.debug(LOG, "final url for the request is getPosts {} " + url);
        final ResponseEntity<List<AuditionPost>> response = restTemplate.exchange(url, HttpMethod.GET,
            null, new ParameterizedTypeReference<List<AuditionPost>>() {
            }
        );
        logger.debug(LOG, "Reponse of the requets is " + response.getBody());
        return response.getBody();
    }

    public AuditionPost getPostById(final String id) {
        logger.info(LOG, "Entering getPostById");

        final String url = "https://jsonplaceholder.typicode.com/posts/" + id;
        logger.debug(LOG, "Final URL for the request in getPostById: {} " + url);

        try {
            final AuditionPost post = restTemplate.getForObject(url, AuditionPost.class);
            if (post != null) {
                final List<Comment> comments = getPostWithComments(String.valueOf(post.getId()));
                post.setComments(comments);
            }
            logger.debug(LOG, "Response of the request in getPostById: {}" + post);

            return post;
        } catch (HttpClientErrorException e) {
            final String errorMessage = "Error from the request: " + e.getMessage();
            logger.error(LOG, errorMessage);
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ResourceNotFoundException("Cannot find a Post with id " + id, e);
            } else {
                throw new SystemException("Error occurred while fetching the post: " + e.getMessage(), e);
            }
        }
    }


    public List<Comment> getPostWithComments(final String postId) {
        logger.info(LOG, "Entering in getPostWithComments");

        final String url = "https://jsonplaceholder.typicode.com/posts/" + postId + "/comments";
        logger.debug(LOG, "final url for the request is {} getPostWithComments " + url);

        final ResponseEntity<List<Comment>> response = restTemplate.exchange(url, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Comment>>() {
            });
        logger.debug(LOG, "Reponse of the requets is {} " + response.getBody());

        return response.getBody();
    }

    public List<Comment> getCommentsForPost(final String postId) {
        logger.info(LOG, "Entering in getCommentsForPost");

        final String url = "https://jsonplaceholder.typicode.com/comments?postId=" + postId;
        logger.debug(LOG, "final url for the request is {} getCommentsForPost " + url);

        final ResponseEntity<List<Comment>> response = restTemplate.exchange(url, HttpMethod.GET, null,
            new ParameterizedTypeReference<List<Comment>>() {
            });
        logger.debug(LOG, "Reponse of the requets is {} " + response.getBody());

        return response.getBody();
    }


}
