package com.jing.blogs.service;

import com.jing.blogs.NotFoundException;
import com.jing.blogs.dao.TrainingRepository;
import com.jing.blogs.domain.Trainning;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Table;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class TrainingServiceImpl implements TrainingService {
    @Autowired
    private TrainingRepository trainingRepository;
    @Transactional
    @Override
    public Trainning saveTraining(Trainning train) {
        return trainingRepository.save(train);
    }
    @Transactional
    @Override
    public Trainning updateTraining(Long id, Trainning train) {
        Trainning t = trainingRepository.getOne(id);
        if(t==null) throw new NotFoundException("the training service doesn't exist!");
        BeanUtils.copyProperties(train,t);
        return trainingRepository.save(t);
    }

    @Override
    public Trainning getTraining(Long id) {
        Trainning trainning = trainingRepository.getOne(id);
        if (trainning == null) throw new NotFoundException("the train service doesn't exist!");
        else return trainning;
    }

    @Override
    public List<Trainning> listAll() {
        return trainingRepository.findAll();
    }

    @Override
    public void deleteTraining(Long id) {
        trainingRepository.deleteById(id);
    }
}
