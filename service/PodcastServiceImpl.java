package com.jing.blogs.service;

import com.jing.blogs.awsService.s3Service;
import com.jing.blogs.dao.PodcastRepository;
import com.jing.blogs.domain.Podcast;
import com.jing.blogs.util.amazonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
public class PodcastServiceImpl implements PodcastService {
    @Value("${amazonProperties.endpointUrl}")
    private String endPointUrl;
    @Value("${amazonProperties.bucketName}")
    public String bucketName;
    @Value("${amazonProperties.accessKey}")
    private String accessKey;
    @Value("${amazonProperties.secretKey}")
    private String secretKey;
    @Autowired
    private PodcastRepository podcastRepository;
    @Autowired
    private s3Service s3Service;
    @Override
    public List<Podcast> findAll() {
        return podcastRepository.findAll();
    }

    @Override
    public List<Podcast> findAllByAuthor(String name) {
        return podcastRepository.findAllByAuthor(name);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Podcast podcast = podcastRepository.getOne(id);
        if (podcast == null) return;
        s3Service.deleteS3File(podcast.getFileName());
        podcastRepository.deleteById(id);
    }

    @Override
    public String savePodcast(MultipartFile file, Podcast podcast) throws IOException {
        String res = "";
        String uploadName = amazonUtils.generateFileName(file);

        String uploadStatus = s3Service.uploadFile(amazonUtils.convertMultiPartFileWithName(file,uploadName));
        if (uploadStatus.toLowerCase().equals("completed")){
            try {
                podcast.setFileName(uploadName.substring(2));
                podcast.setURL(amazonUtils.generateURl(bucketName,uploadName.substring(2)));
                Podcast podcast1 = podcastRepository.save(podcast);
                if (podcast1 != null) res = "Upload Succeed";
                else throw new Exception();
            } catch (Exception e){
                res = e.getMessage();
                s3Service.deleteS3File(uploadName.substring(2));
            }
        }
        return res;
    }
}
