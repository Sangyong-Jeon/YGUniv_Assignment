package com.univ.backend.controller;

import com.univ.backend.Service.PostService;
import com.univ.backend.dto.PostFormDto;
import com.univ.backend.dto.response.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostApiController {

    private final PostService postService;
    /*
    전체 게시글 조회
    - 각 리스트 항목은 글번호, 제목, 등록일시, 조회수
    - 기본은 최신순 조회. 조회수 많은 순으로 조회 가능해야 함
    - 페이지 단위 조회는 구현하지 않고 한번에 전체 게시글 정보를 제공
     */

    /*
    게시글 검색
    - 사용자가 입력한 키워드를 제목에 포함한 게시글 리스트 조회 가능
     */

    /*
    게시글 조회
    - 리스트에서 선택한 글 확인
     */

    /*
    게시글 등록
    - 제목, 본문, 비밀번호
     */
    @PostMapping("/post")
    public ResponseEntity<ResponseData<String>> addPost(@RequestBody PostFormDto postFormDto) throws URISyntaxException {
        ResponseData<String> responseData = postService.addPost(postFormDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("http://localhost:5554"));
        return new ResponseEntity<>(responseData, headers, HttpStatus.MOVED_PERMANENTLY);
    }

    /*
    게시글 수정
    - 비밀번호가 맞으면 제목과 본문 수정 가능
    - 게시글이 수정되어도 조회수는 변하지 않음
     */
    @PatchMapping("/post/{postId}")
    public ResponseEntity<ResponseData<String>> updatePost(@PathVariable("postId") Long postId,
                                                           @RequestBody PostFormDto postFormDto) throws URISyntaxException {
        ResponseData<String> responseData = postService.updatePost(postId, postFormDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("http://localhost:5554"));
        return new ResponseEntity<>(responseData, headers, HttpStatus.MOVED_PERMANENTLY);
    }

    /*
    게시글 삭제
    - 비밀번호가 맞으면 게시글 삭제
     */

}
