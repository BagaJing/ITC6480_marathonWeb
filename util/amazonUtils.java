package com.jing.blogs.util;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class amazonUtils {
    public static String generateURl(String bucketName,String fileName){
        return bucketName+".s3.amazonaws.com/"+fileName;
    }
    /*
     *S3 uploading files method require "File" parameters
     *The springframework provides multipartFile Interface
     * Then convert process is needed
     */
    public static File convertMultiPartFile(MultipartFile file) throws IOException {
        File targetFile = new File(generateFileName(file));
        FileOutputStream fos = new FileOutputStream(targetFile);
        fos.write(file.getBytes());
        fos.close();
        return targetFile;
    }
    public static File convertMultiPartFileWithName(MultipartFile file,String name) throws IOException {
        File targetFile = new File(name);
        FileOutputStream fos = new FileOutputStream(targetFile);
        fos.write(file.getBytes());
        fos.close();
        return targetFile;
    }
    /*
     * it is possible for uploading the same file more than one time
     * than generating unique name for the file each time is needed
     * use timestamp to generate it
     */
    public static String generateFileName(MultipartFile file){
        return new Date().getTime()+"-"+file.getOriginalFilename().replace(" ","_");
    }
    public static void printProgressBar(double pct){
        final int bar_size = 40;
        final String empty_bar = "                                        ";
        final String filled_bar = "########################################";
        int amt_full = (int) (bar_size * (pct / 100.0));
        System.out.format("  [%s%s]", filled_bar.substring(0, amt_full),
                empty_bar.substring(0, bar_size - amt_full));
    }
    public static void eraseProgressBar(){
        // erase_bar is bar_size (from printProgressBar) + 4 chars.
        final String erase_bar = "\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b\b";
        System.out.format(erase_bar);
    }
}
