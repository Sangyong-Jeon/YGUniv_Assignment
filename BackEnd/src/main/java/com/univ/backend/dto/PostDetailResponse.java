package com.univ.backend.dto;

import com.univ.backend.domain.Post;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailResponse {
    private Long id;
    private String title;
    private String content;
    private String createDate;
    private int viewCount;

    public PostDetailResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.viewCount = post.getViewCount();
        // LocalDateTime을 프론트에서 보기 편하게 yyyy-MM-dd HH:MM:SS 로 변환하여 보냄
        this.createDate = post.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
