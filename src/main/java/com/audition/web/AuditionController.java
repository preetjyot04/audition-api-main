package com.audition.web;

import com.audition.common.exception.ResourceNotFoundException;
import com.audition.common.logging.AuditionLogger;
import com.audition.model.AuditionPost;
import com.audition.model.Comment;
import com.audition.service.AuditionService;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuditionController {

    private static final Logger LOG = LoggerFactory.getLogger(AuditionController.class);

    @Autowired
    private transient AuditionService auditionService;

    @Autowired
    private transient AuditionLogger auditionLogger;

    @RequestMapping(value = "/posts", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<AuditionPost> getPosts(@RequestParam(required = false) final Integer userId) {
        auditionLogger.info(LOG, "Retrieving posts");
        return auditionService.getPosts(userId);
    }

    @RequestMapping(value = "/posts/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody AuditionPost getPostById(@PathVariable("id") final String postId) {
        auditionLogger.info(LOG, "Retrieving post with id {}", postId);

        if (!postId.matches("\\d+")) {
            auditionLogger.error(LOG, "Invalid post id: {}" + postId);
            throw new ResourceNotFoundException("Post id should be number. Current id is " + postId);
        }

        return auditionService.getPostById(postId);
    }

    @RequestMapping(value = "/posts/{postId}/comments", method = RequestMethod.GET)
    public @ResponseBody ResponseEntity<List<Comment>> getCommentsForPost(
        @PathVariable("postId") final String postId) {
        auditionLogger.info(LOG, "Retrieving comments for post with id {}", postId);

        if (!postId.matches("\\d+")) {
            auditionLogger.error(LOG, "Invalid post id: {}" + postId);
            throw new ResourceNotFoundException("Post id should be number. Current id is " + postId);
        }

        final List<Comment> comments = auditionService.getCommentsForPost(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }
}
