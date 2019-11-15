package com.jing.blogs.service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.transfer.*;
import com.jing.blogs.dao.PhotoRepository;
import com.jing.blogs.domain.Photo;
import com.jing.blogs.util.MyBeanUtils;
import com.jing.blogs.util.amazonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
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
    private String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;

    // initialze the s3client
    @PostConstruct
    private void initializeAmazonS3(){
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey,this.secretKey);
        AWSCredentialsProvider credentialsProvider = new AWSCredentialsProvider() {
            @Override
            public AWSCredentials getCredentials() {
                //AWSCredentials credentials = new BasicAWSCredentials(this.accessKey,this.secretKey);
                return credentials;
            }

            @Override
            public void refresh() {

            }
        };
        Regions clientRegion = Regions.US_EAST_1;
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(clientRegion)
                .withCredentials(credentialsProvider)
                .build();
        System.out.println(this.s3Client.getBucketPolicy(bucketName).getPolicyText());
    }
    public String batchUploadFilestoS3Bucket(List<File> files){
        if (files.size()==0) return "no files found to upload";
        String response = "";
        TransferManager transfer = TransferManagerBuilder.standard().withS3Client(s3Client).build();
        try {
            /*
             * transfer.uploadFileList
             * parameter 1:String bucketName
             * parameter 2:String virtualDirecotryKeyPrefix : the virtual directory of files to upload, use null or empty String to upload into the root of bucket
             * parameter 3:File direcotry: the common parent directory of files to upload, use new File(".") to upload without a common directory
             * parameter 4:List<File> files: a list of files to upload
             */
            ObjectCannedAclProvider aclProvider = new ObjectCannedAclProvider() {
                @Override
                public CannedAccessControlList provideObjectCannedAcl(File file) {
                    return CannedAccessControlList.PublicRead;
                }
            };
            MultipleFileUpload upload = transfer.uploadFileList(bucketName,"",new File("."),files,null,null,aclProvider);
            do{
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e){
                    break;
                }
                TransferProgress progress = upload.getProgress();
                double pct = progress.getPercentTransferred();
                amazonUtils.eraseProgressBar();
                amazonUtils.printProgressBar(pct);
            }while (!upload.isDone());
            System.out.println();
            response = upload.getState().toString();
            /*upload succeed, delete local files*/
            for (File file : files){
                file.delete();
            }
        } catch (Exception e){
            response = e.getMessage();
        }
        return response;
    }
    public void deleteFileFromS3(String fileName){
        s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
    }
    /*
    * --------------The end of amazon S3 functions----------------------------------------------------------------------
    */
    @Autowired
    private PhotoRepository photoRepository;
    @Override
    public String batchUploadFiles(MultipartFile[] orginalFiles,int typeId) throws IOException {
        List<File> files = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        for (int i = 0 ; i < orginalFiles.length ; i++){
            files.add(amazonUtils.convertMultiPartFile(orginalFiles[i]));
            nameList.add(files.get(i).getName().substring(2));
        }
        String uploadResult = batchUploadFilestoS3Bucket(files);
        if(uploadResult.equalsIgnoreCase("Completed")){
            try{
                Photo[] saveList = new Photo[nameList.size()];
                int i = 0;
                for (String name : nameList){
                    Photo photo = new Photo();
                    photo.setTypeId(typeId);
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
                uploadResult = e.getMessage();
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
                deleteFileFromS3(fileName[i]);
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
}
