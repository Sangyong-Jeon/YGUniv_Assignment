package com.univ.backend.dto;

import com.univ.backend.domain.Post;
import lombok.*;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse {

    private Long id; // 글번호
    private String title; // 제목
    private String createDate; // 등록일시
    private int viewCount; // 조회수

    public PostResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        // LocalDateTime을 프론트에서 보기 편하게 yyyy-MM-dd HH:MM:SS 로 변환하여 보냄
        this.createDate = post.getCreatedDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.viewCount = post.getViewCount();
    }
}
