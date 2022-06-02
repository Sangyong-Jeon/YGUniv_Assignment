package com.univ.backend.dto;

import com.univ.backend.domain.Post;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostFormDto {

    private String title;
    private String content;
    private String password;

    public Post createAddPostEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .viewCount(0)
                .password(password)
                .build();
    }
}
