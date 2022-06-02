package com.univ.backend.repository;

import com.univ.backend.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("select p from Post p where p.title like %:title% order by p.createdDateTime desc")
    List<Post> searchByTitle(@Param("title") String title);
}
