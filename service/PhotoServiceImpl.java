package com.jing.blogs.service;

import com.amazonaws.services.s3.AmazonS3;
import com.jing.blogs.dao.PhotoRepository;
import com.jing.blogs.domain.Photo;
import com.jing.blogs.util.MyBeanUtils;
import com.jing.blogs.util.amazonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.jing.blogs.awsService.s3Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class PhotoServiceImpl implements PhotoSevice{
    /*
     * The start of amazon S3 functions
     */
    private AmazonS3 s3Client;

    //bucket and AWS properties

    @Value("${amazonProperties.endpointUrl}")
    private String endPointUrl;
    @Value("${amazonProperties.bucketName}")
    public String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;


    @Autowired
    private PhotoRepository photoRepository;
    @Autowired
    private s3Service s3Service;

    private Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public String batchUploadFiles(MultipartFile[] orginalFiles,int typeId) throws IOException {
        List<File> files = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        for (int i = 0 ; i < orginalFiles.length ; i++){
            files.add(amazonUtils.convertMultiPartFile(orginalFiles[i]));
            nameList.add(files.get(i).getName().substring(2));
        }
        String uploadResult = s3Service.batchUploadFiles(files); //  batchUploadFilestoS3Bucket(files);
        if(uploadResult.equalsIgnoreCase("Completed")){
            try{
                Photo[] saveList = new Photo[nameList.size()];
                int i = 0;
                for (String name : nameList){
                    Photo photo = new Photo();
                    photo.setTypeId(typeId);
                    logger.info("exception : test"+ name);
                    photo.setName(name);
                    photo.setUploadTime(new Date());
                    photo.setURL(amazonUtils.generateURl(bucketName,name));
                    saveList[i] = photo;
                    i++;
                }
                Iterable<Photo> iterable = Arrays.asList(saveList);
                List<Photo> res = photoRepository.saveAll(iterable);
                if (res.size() == 0){
                    throw new Exception("failed to upload into database");
                }
            } catch (Exception e){
                for (String name : nameList)
                    s3Service.deleteS3File(name);
                uploadResult = "Upload Canclled by Exceptions, Please make sure you are uploading valid pictures.";
                e.printStackTrace();
            }

            return uploadResult;
        }
        else
            return uploadResult;
    }

    @Transactional
    @Override
    public void deltePhotos(String[] fileName) {
        boolean s3Deleted = true;
        try {
            for (int i = 0 ; i < fileName.length;i++){
                s3Service.deleteS3File(fileName[i]); //deleteFileFromS3();
            }
        }catch (Exception e){
            s3Deleted = false;
            e.printStackTrace();
        }
        if (s3Deleted){
            for (int i = 0; i < fileName.length;i++){
                photoRepository.deleteByName(fileName[i]);
            }
        }
    }

    @Override
    public List<Photo> getAllUrls() {
        List<Photo> result = photoRepository.findAll();
        return result;
    }

    @Override
    public List<Photo> getAllByIds(String imgIds) {
        return photoRepository.findAllById(MyBeanUtils.converttoList(imgIds));
    }

    @Override
    public List<String> getPathsById(String imgIds) {
        List<Photo> photos = photoRepository.findAllById(MyBeanUtils.converttoList(imgIds));
        List<String> res = new ArrayList<>();
        for(Photo photo : photos){
            res.add(photo.getURL());
        }
        return  res;
    }

    @Override
    public List<Photo> getAllUrlsByType(int typeId) {
        return photoRepository.findAllByTypeId(typeId);
    }

    @Override
    public Page<Photo> listPhoto(Pageable pageable, int typeId) {
        return photoRepository.showWithPages(typeId,pageable);
    }
}
