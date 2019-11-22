package com.jing.blogs.web.admin;

import com.jing.blogs.domain.Podcast;
import com.jing.blogs.orderQueue.DeferredResultHolder;
import com.jing.blogs.orderQueue.actionQueue;
import com.jing.blogs.util.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("/admin")
public class PodcastController {
    private final static String POST = "admin/podcast-post";
    private final static String REDIRECT_POST = "redirect:/admin/podcasts";
    private final static  String LIST = "admin/podcasts";
    @Autowired
    private actionQueue queue;
    @Autowired
    private DeferredResultHolder resultHolder;

    @GetMapping("/podcast-post")
    public DeferredResult<String> post(Model model){
        String placeOrder = "P"+ MyBeanUtils.getRandomOrderNum(8);
        queue.setPodCastPostPageOrder(placeOrder,model);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }
    @PostMapping("/podcasts")
    public DeferredResult<String> post(@RequestParam(value = "audio") MultipartFile file,
                       RedirectAttributes attributes,
                       HttpSession session,
                       Podcast podcast) throws IOException {
        String placeOrder = "PR"+MyBeanUtils.getRandomOrderNum(8);
        queue.setPodcastPostOrder(placeOrder,file,attributes,session,podcast);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }
    @GetMapping("/podcasts")
    public DeferredResult<String> listPodcasts(Model model){
        String placeOrder = "PL"+MyBeanUtils.getRandomOrderNum(8);
        queue.setListPodCastsOrder(placeOrder,model);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }
    @GetMapping("/deletePodcast/{id}")
    public DeferredResult<String> deletePodcast(RedirectAttributes attributes, @PathVariable Long id){

        String placeOrder = "PR"+MyBeanUtils.getRandomOrderNum(8);
        queue.setDeletePodcastOrder(placeOrder,id,attributes);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return  result;


    }
}
