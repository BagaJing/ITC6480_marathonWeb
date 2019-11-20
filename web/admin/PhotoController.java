package com.jing.blogs.web.admin;

import com.jing.blogs.orderQueue.DeferredResultHolder;
import com.jing.blogs.orderQueue.actionQueue;
import com.jing.blogs.service.PhotoSevice;
import com.jing.blogs.util.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

//@RestController // postman test using
@Controller
@RequestMapping("/admin")
public class PhotoController {
    @Autowired
    private PhotoSevice photoSevice;
    @Autowired
    private actionQueue actionQueue;
    @Autowired
    private DeferredResultHolder resultHolder;

    final static  int GALLERY_TYPE = 2;
    final static  int ARTICLE_TYPE = 1;
    @PostMapping("/gallery/uploadArticlePic")
    public DeferredResult<String> uploadsArticlePic(@RequestParam("files") MultipartFile[] files, RedirectAttributes attributes) throws IOException {
        String placeOrder = "GA1"+MyBeanUtils.getRandomOrderNum(8);
        actionQueue.setUploadPhotoOrder(placeOrder,files,ARTICLE_TYPE,attributes);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return  result;
    }
    @PostMapping("/gallery/uploadGalleryPic")
    public DeferredResult<String> uploadsGalleryPic(@RequestParam("files") MultipartFile[] files, RedirectAttributes attributes) throws IOException {
        String placeOrder = "GA2"+MyBeanUtils.getRandomOrderNum(8);
        actionQueue.setUploadPhotoOrder(placeOrder,files,GALLERY_TYPE,attributes);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return  result;
    }

    @GetMapping("/gallery/{names}/delete")
    public DeferredResult<String> delete(@PathVariable String[] names, RedirectAttributes attributes){
        String placeOrder = "GA3" + MyBeanUtils.getRandomOrderNum(8);
        actionQueue.setDeletePhotoOrder(placeOrder,names,attributes);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
        //return REDIRECT;
    }

    @GetMapping("/gallery")
    public DeferredResult<String> getAllArtilcePic(Model model){
        String placeOrder = "SGA" + MyBeanUtils.getRandomOrderNum(8);
        actionQueue.setShowAllOrder(placeOrder,model);
        DeferredResult<String> result = new DeferredResult<>();
        resultHolder.getMap().put(placeOrder,result);
        return result;
    }
}
