package com.univ.backend.dto;

import com.univ.backend.domain.Post;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostForm {

    private String title; // 제목
    private String content; // 본문
    private String password; // 비밀번호

    public Post createAddPostEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .viewCount(0)
                .password(password)
                .build();
    }
}
