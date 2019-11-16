package com.jing.blogs.awsService;

import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.util.List;

public interface s3Service {
    String uploadFile(File file);
    String batchUploadFiles(List<File> files);
    void deleteS3File(String fileName);
}
