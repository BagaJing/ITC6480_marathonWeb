package com.jing.blogs.service;

import com.jing.blogs.domain.Photo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PhotoSevice {
    String batchUploadFiles(MultipartFile[] multipartFiles,int typeId) throws IOException;
    void deltePhotos(String[] fileName);
    List<Photo> getAllUrls();
    List<Photo> getAllUrlsByType(int typeId);
    List<Photo> getAllByIds(String imgIds);
    List<String> getPathsById(String imgIds);
}
