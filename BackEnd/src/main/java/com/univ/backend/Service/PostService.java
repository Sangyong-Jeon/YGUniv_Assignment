package com.univ.backend.Service;

import com.univ.backend.domain.Post;
import com.univ.backend.dto.PostFormDto;
import com.univ.backend.dto.response.Header;
import com.univ.backend.dto.response.ResponseData;
import com.univ.backend.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    // 게시글 등록
    public ResponseData<String> addPost(PostFormDto postFormDto) {
        postRepository.save(postFormDto.createAddPostEntity());
        Header header = new Header(200, "OK", "등록완료");
        return new ResponseData<>(header, "");
    }

    // 게시글 수정
    public ResponseData<String> updatePost(Long postId, PostFormDto postFormDto) {
        Optional<Post> findPost = postRepository.findById(postId);
        Post post = findPost.orElse(null);
        Header header = null;
        // 게시글이 존재하지 않을때
        if (post == null) {
            System.out.println("게시글 존재 X");
            header = new Header(400, "BAD_REQUEST", "존재하지 않는 게시글입니다.");
            return new ResponseData<>(header, "");
        }
        // 게시글 비밀번호와 입력한 비밀번호가 같지 않을때
        if (!post.getPassword().equals(postFormDto.getPassword())) {
            System.out.println("비번 X");
            header = new Header(400, "BAD_REQUEST", "비밀번호가 틀렸습니다.");
            return new ResponseData<>(header, "");
        }
        // 제목이 null 또는 "" 이 아닐 경우 수정
        if (StringUtils.hasLength(postFormDto.getTitle())) {
            System.out.println("제목 수정");
            post.updateTitle(postFormDto.getTitle());
        }
        // 내용이 null 또는 "" 이 아닐 경우 수정
        if (StringUtils.hasLength(postFormDto.getContent())) {
            System.out.println("본문 수정");
            post.updateContent(postFormDto.getContent());
        }
        System.out.println("수정완료");
        header = new Header(200, "OK", "수정이 완료되었습니다.");
        return new ResponseData<>(header, "");
    }

    // 게시글 전체 조회(정렬)
    @Transactional(readOnly = true)
    public ResponseData<List<Post>> getPosts(Sort sort) {
        List<Post> posts = postRepository.findAll(sort);
        Header header = new Header(200, "OK", "게시글 전체 조회 완료");
        return new ResponseData<>(header, posts);
    }
}
