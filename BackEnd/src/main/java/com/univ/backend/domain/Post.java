package com.univ.backend.domain;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // 글번호
    private String title; // 제목
    private String content; // 내용
    private int viewCount; // 조회수
    private String password; // 비밀번호

    @Builder
    public Post(String title, String content, int viewCount, String password) {
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.password = password;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }
}
