package com.jing.blogs.service;

import com.jing.blogs.domain.Trainning;

import java.util.List;

public interface TrainingService {
    Trainning saveTraining(Trainning train);
    Trainning updateTraining(Long id,Trainning train);
    Trainning getTraining(Long id);
    List<Trainning> listAll();
    void deleteTraining(Long id);
}
