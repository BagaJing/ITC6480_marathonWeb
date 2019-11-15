package com.jing.blogs.service;

import com.jing.blogs.dao.PodcastRepository;
import com.jing.blogs.domain.Podcast;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PodcastServiceImpl implements PodcastService {
    @Autowired
    private PodcastRepository podcastRepository;
    @Autowired
    private PhotoSevice photoSevice;
    @Override
    public List<Podcast> findAll() {
        return podcastRepository.findAll();
    }

    @Override
    public List<Podcast> findAllByAuthor(String name) {
        return podcastRepository.findAllByAuthor(name);
    }

    @Override
    public void deleteById(Long id) {
        podcastRepository.deleteById(id);
    }

    @Override
    public void savePodcast(Podcast podcast) {
        
    }
}
