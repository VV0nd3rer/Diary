package com.kverchi.diary.repository;

import com.kverchi.diary.model.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Kverchi on 28.6.2018.
 */
@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
