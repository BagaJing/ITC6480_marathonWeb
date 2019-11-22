package com.jing.blogs.orderQueue;
import com.jing.blogs.domain.Podcast;
import com.jing.blogs.domain.User;
import com.jing.blogs.service.PhotoSevice;
import com.jing.blogs.service.PodcastService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

@Component
public class actionQueue {
    private Logger logger = LoggerFactory.getLogger(getClass());
    private Queue<String> actionQueue = new LinkedList<>();
    @Autowired
    private PhotoSevice photoSevice;
    @Autowired
    private PodcastService podcastService;
    /*admin Phtoto Controller*/
    @Async("taskAsyncPool")
    public void setUploadPhotoOrder(String placeOrder, MultipartFile[] files, int type, RedirectAttributes attributes) throws IOException {
        logger.info("Upload Task Received");
        String status;
        String redirectMessage;
        try{
            status = photoSevice.batchUploadFiles(files,type);
            redirectMessage = "message";
        } catch (Exception e){
            status = e.getMessage();
            redirectMessage = "exception";
        }
        attributes.addFlashAttribute(redirectMessage,status);
        actionQueue.offer(placeOrder);
        logger.info("Upload Task Finished");
    }
    @Async("taskAsyncPool")
    public void setDeletePhotoOrder(String placeOrder,String[] names, RedirectAttributes attributes){
        String redirectMessage = "delete completed";
        String type = "message";
        if (names.length == 0) redirectMessage = "No Files found need to be deleted";
        try {
            photoSevice.deltePhotos(names);
        }catch (Exception e){
            type = "exception";
            redirectMessage = e.getMessage();
        }
        attributes.addFlashAttribute(type,redirectMessage);
        actionQueue.offer(placeOrder);

    }
    @Async("taskAsyncPool")
    public void setShowAllOrder(String placeOrder, Model model){
        model.addAttribute("articlePhotos",photoSevice.getAllUrlsByType(1));
        model.addAttribute("galleryPhotos",photoSevice.getAllUrlsByType(2));
        actionQueue.offer(placeOrder);
    }
    /*admin Podcast Controller*/
    @Async("taskAsyncPool")
    public void setPodCastPostPageOrder(String placeOrder,Model model){
        model.addAttribute("podcast",new Podcast());
        actionQueue.offer(placeOrder);
    }
    @Async("taskAsyncPool")
    public void setPodcastPostOrder(String placeOrder,MultipartFile file, RedirectAttributes attributes, HttpSession session, Podcast podcast) throws IOException {
        User user = (User) session.getAttribute("user");
        String author = user.getNickname();
        user = null;
        podcast.setAuthor(author);
        author = null;
        String saveStatus = podcastService.savePodcast(file, podcast);
        attributes.addFlashAttribute("message",saveStatus);
        actionQueue.offer(placeOrder);
    }
    @Async("taskAsyncPool")
    public void setListPodCastsOrder(String placeOrder,Model model){
        model.addAttribute("podcasts",podcastService.findAll());
        actionQueue.offer(placeOrder);
    }
    @Async("taskAsyncPool")
    public void setDeletePodcastOrder(String placeOrder,Long id,RedirectAttributes attributes){
        String type = "message";
        String message = "Deleted";
        try {
            podcastService.deleteById(id);
        } catch (Exception e){
            type = "exception";
            logger.error(e.getMessage());
            message = e.getMessage();
        }
        attributes.addFlashAttribute(type,message);
        actionQueue.offer(placeOrder);
    }

    public Queue<String> getActionQueue() {
        return actionQueue;
    }

    public void setActionQueue(Queue<String> actionQueue) {
        this.actionQueue = actionQueue;
    }
}
