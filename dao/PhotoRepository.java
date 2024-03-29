package com.jing.blogs.dao;

import com.jing.blogs.domain.Photo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface PhotoRepository extends JpaRepository<Photo,Long>{
    @Query("select p from Photo p where p.photo_id=?1")
    Photo findOne(Long id);
    @Query("select p from Photo p where p.typeId=?1")
    Page<Photo> showWithPages(int typeId,Pageable pageable);
    List<Photo> findAllByTypeId(int typeId);
    void deleteByName(String name);
    void deleteAll();
}
