package com.jing.blogs.dao;

import com.jing.blogs.domain.Podcast;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PodcastRepository extends JpaRepository<Podcast,Long> {
    List<Podcast> findAllByAuthor(String name);
    void deleteById(Long id);
}
