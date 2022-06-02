package com.univ.backend.controller;

import com.univ.backend.Service.PostService;
import com.univ.backend.dto.PostDetailResponse;
import com.univ.backend.dto.PostForm;
import com.univ.backend.dto.PostResponse;
import com.univ.backend.dto.response.ResponseData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostApiController {

    private final PostService postService;

    /*
    전체 게시글 조회
    - 각 리스트 항목은 글번호, 제목, 등록일시, 조회수
    - 기본은 "최신순" 조회. "조회수" 많은 순으로 조회 가능해야 함
    - 페이지 단위 조회는 구현하지 않고 한번에 전체 게시글 정보를 제공
     */
    /*
    전체 조회이므로 PagingAndSortingRepository 인터페이스의 findAll(Sort sort) 메서드를 사용하여 정렬함.
    따라서 Sort 클래스를 사용할 것인데 컨트롤러 파라미터로 받으려면 아래와 같이 적어놓고 url에는 다음과 같이 적는다.
    ex) /posts?sort=name,asc => name으로 오름차순 정렬함. asc는 기본값이라 안적어도 됨.
        /posts?sort=name,id => name과 id로 오름차순 정렬
        /posts?sort=name,asc&sort=id,desc => name으로 오름차순 정렬하고 id로 내림차순 정렬
    ex) /posts?sort=createdDateTime,desc => 생성일시로 내림차순 정렬함 (최신순으로 정렬)
        /posts?sort=viewCount,desc => 조회순으로 내림차순 정렬함 (최신순으로 정렬)
     */
    @GetMapping("/posts")
    public ResponseEntity<ResponseData<List<PostResponse>>> getPosts(Sort sort) {
        ResponseData<List<PostResponse>> responseData = postService.getPosts(sort);
        log.info("게시글 전체 조회 완료");
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    /*
    게시글 검색
    - 사용자가 입력한 키워드를 제목에 포함한 게시글 리스트 조회 가능
     */

    /*
    게시글 조회
    - 리스트에서 선택한 글 확인
     */
    @GetMapping("/posts/{postId}")
    public ResponseEntity<ResponseData<PostDetailResponse>> getPost(@PathVariable("postId") Long postId) {
        ResponseData<PostDetailResponse> responseData = postService.getPost(postId);
        log.info("게시글 상세 조회 완료");
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    /*
    게시글 등록
    - 제목, 본문, 비밀번호
     */
    @PostMapping("/posts")
    public ResponseEntity<ResponseData<String>> addPost(@RequestBody PostForm postFormDto) throws URISyntaxException {
        ResponseData<String> responseData = postService.addPost(postFormDto);
        HttpHeaders headers = new HttpHeaders(); // 헤더를 설정하기 위해 생성
        headers.setLocation(new URI("http://localhost:5554")); // 헤더에 location 추가함.
        // MOVED_PERMANENTLY는 HTTP 301로 영구적인 리다이렉션을 하겠다는 의미임.
        // 웹 브라우저는 300번 대 응답의 결가에 Location 헤더가 있으면 Location 위치로 자동 이동(리다이렉트, GET)한다.
        log.info("게시글 등록 완료");
        return new ResponseEntity<>(responseData, headers, HttpStatus.MOVED_PERMANENTLY);
    }

    /*
    게시글 수정
    - 비밀번호가 맞으면 제목과 본문 수정 가능
    - 게시글이 수정되어도 조회수는 변하지 않음
     */
    @PatchMapping("/posts/{postId}")
    public ResponseEntity<ResponseData<String>> updatePost(@PathVariable("postId") Long postId,
                                                           @RequestBody PostForm postFormDto) throws URISyntaxException {
        ResponseData<String> responseData = postService.updatePost(postId, postFormDto);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(new URI("http://localhost:5554"));
        log.info("게시글 수정 완료");
        return new ResponseEntity<>(responseData, headers, HttpStatus.MOVED_PERMANENTLY);
    }

    /*
    게시글 삭제
    - 비밀번호가 맞으면 게시글 삭제
     */

}
