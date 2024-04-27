package com.audition.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AuditionPost {

    private int userId;
    private int id;
    private String title;
    private String body;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<Comment> comments;

}
