package com.jing.blogs.service;

import com.jing.blogs.domain.Podcast;

import java.util.List;

public interface PodcastService {
    List<Podcast> findAll();
    List<Podcast> findAllByAuthor(String name);
    void deleteById(Long id);
    void savePodcast(Podcast podcast);
}
