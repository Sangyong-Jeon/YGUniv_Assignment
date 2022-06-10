package com.univ.backend.service;

import com.univ.backend.domain.Post;
import com.univ.backend.dto.PostDetailResponse;
import com.univ.backend.dto.PostForm;
import com.univ.backend.dto.PostResponse;
import com.univ.backend.dto.response.Header;
import com.univ.backend.dto.response.ResponseData;
import com.univ.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글 등록
    public ResponseData<String> addPost(PostForm postFormDto) {
        Header header = new Header(200, "OK", "등록완료");
        postRepository.save(postFormDto.createAddPostEntity());
        return new ResponseData<>(header, "");
    }

    // 게시글 수정
    public ResponseData<String> updatePost(Long postId, PostForm postFormDto) {
        Header header = new Header(200, "OK", "수정이 완료되었습니다.");
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.orElse(null);
        // 게시글이 존재하지 않을때
        if (post == null) {
            log.error("게시글이 존재하지 않습니다. : 게시글 번호 = {}", postId);
            header = new Header(400, "BAD_REQUEST", "게시글이 존재하지 않습니다.");
            return new ResponseData<>(header, "");
        }
        // 게시글 비밀번호와 입력한 비밀번호가 같지 않을때
        if (!post.getPassword().equals(postFormDto.getPassword())) {
            log.error("게시글의 비밀번호가 일치하지 않습니다. : 게시글 번호 = {}", postId);
            header = new Header(400, "BAD_REQUEST", "비밀번호가 틀렸습니다.");
            return new ResponseData<>(header, "");
        }
        // 제목이 null 또는 "" 이 아닐 경우 수정
        if (StringUtils.hasLength(postFormDto.getTitle())) {
            log.info("게시글 제목이 수정되었습니다. : 게시글 번호 = {}", postId);
            post.updateTitle(postFormDto.getTitle());
        }
        // 내용이 null 또는 "" 이 아닐 경우 수정
        if (StringUtils.hasLength(postFormDto.getContent())) {
            log.info("게시글 본문이 수정되었습니다. : 게시글 번호 = {}", postId);
            post.updateContent(postFormDto.getContent());
        }
        return new ResponseData<>(header, "");
    }

    // 게시글 전체 조회(정렬)
    @Transactional(readOnly = true)
    public ResponseData<List<PostResponse>> getPosts(Sort sort) {
        List<Post> posts = postRepository.findAll(sort);
        // List로 받은 Entity들을 Dto로 변환
        List<PostResponse> postDtos = posts.stream().map(PostResponse::new).collect(Collectors.toList());
        Header header = new Header(200, "OK", "게시글 전체 조회 완료");
        return new ResponseData<>(header, postDtos);
    }

    // 게시글 검색
    @Transactional(readOnly = true)
    public ResponseData<List<PostResponse>> searchPosts(Sort sort, String title) {
        List<Post> posts = postRepository.searchByTitle(sort, title);
        // Dto 변환
        List<PostResponse> postDtos = posts.stream().map(PostResponse::new).collect(Collectors.toList());
        Header header = new Header(200, "OK", "게시글 검색 완료");
        return new ResponseData<>(header, postDtos);
    }

    // 게시글 상세 조회
    public ResponseData<PostDetailResponse> getPost(Long postId) {
        Header header = new Header(200, "OK", "게시글이 조회되었습니다.");
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.orElse(null);
        // 게시글이 존재하지 않을 때
        if (post == null) {
            log.error("게시글이 존재하지 않습니다. : 게시글 번호 = {}", postId);
            header = new Header(400, "BAD_REQUEST", "게시글이 존재하지 않습니다.");
            return new ResponseData(header, "");
        }
        // 상세 조회했으므로 조회수 +1
        post.increaseView();
        // Dto 변환
        PostDetailResponse postDto = new PostDetailResponse(post);
        return new ResponseData<>(header, postDto);
    }

    // 게시글 삭제
    public ResponseData<String> deletePost(Long postId, String password) {
        Header header = new Header(200, "OK", "게시글이 삭제되었습니다.");
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.orElse(null);
        if (post == null) {
            log.error("게시글이 존재하지 않습니다. : 게시글 번호 = {}", postId);
            header = new Header(400, "BAD_REQUEST", "게시글이 존재하지 않습니다.");
            return new ResponseData<>(header, "");
        }
        // 비밀번호 검증
        if (!post.getPassword().equals(password)) {
            log.error("게시글의 비밀번호가 일치하지 않습니다. : 게시글 번호 = {}", postId);
            header = new Header(400, "BAD_REQUEST", "비밀번호가 틀렸습니다.");
            return new ResponseData<>(header, "");
        }
        // 게시글 삭제
        postRepository.delete(post);
        return new ResponseData<>(header, "");
    }
}
