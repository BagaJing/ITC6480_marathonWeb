package com.jing.blogs.dao;

import com.jing.blogs.domain.Comment;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByBlogId(Long blogId, Sort sort);

    @Query("select c from Comment c where c.id=?1")
     Comment findOne(Long id);

    List<Comment> findByBlogIdAndParentCommentNull(long blogId, Sort sort);
}
