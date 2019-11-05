package com.jing.blogs.dao;

import com.jing.blogs.domain.Trainning;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainingRepository extends JpaRepository<Trainning,Long> {
    Trainning findByName(String name);
}
