package com.jing.blogs.awsService;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.transfer.*;
import com.jing.blogs.util.amazonUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

@Service
public class s3ServiceImpl implements s3Service {
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
    @Override
    public String uploadFile(File file) {
        List<File> files = new LinkedList<>();
        files.add(file);
        return batchUploadFiles(files);
    }

    @Override
    public String batchUploadFiles(List<File> files) {
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

    @Override
    public void deleteS3File(String fileName) {
        s3Client.deleteObject(new DeleteObjectRequest(bucketName,fileName));
    }
}
