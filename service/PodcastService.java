package com.jing.blogs.service;

import com.jing.blogs.domain.Podcast;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PodcastService {
    List<Podcast> findAll();
    List<Podcast> findAllByAuthor(String name);
    void deleteById(Long id);
    String savePodcast(MultipartFile file, Podcast podcast) throws IOException;
}
