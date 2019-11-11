package com.jing.blogs.web.admin;

import com.jing.blogs.service.PhotoSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

//@RestController // postman test using
@Controller
@RequestMapping("/admin")
public class PhotoController {
    @Autowired
    PhotoSevice photoSevice;
    final static String REDIRECT = "redirect:/admin/gallery";
    final static String GALLERY = "admin/gallery";
    final static  int GALLERY_TYPE = 2;
    final static  int ARTICLE_TYPE = 1;
    @PostMapping("/gallery/uploadArticlePic")
    public String uploadsArticlePic(@RequestParam("files") MultipartFile[] files, RedirectAttributes attributes) throws IOException {
        if (files.length == 0) return "No files found need to be upload";
        String status;
        String type;
        try {
            status = photoSevice.batchUploadFiles(files,ARTICLE_TYPE);
            type = "message";
        } catch (Exception e){
            status = e.getMessage();
            type = "exception";
        }
        attributes.addFlashAttribute(type,status);
        return REDIRECT;
    }
    @PostMapping("/gallery/uploadGalleryPic")
    public String uploadsGalleryPic(@RequestParam("files") MultipartFile[] files, RedirectAttributes attributes) throws IOException {
        if (files.length == 0) return "No files found need to be upload";
        String status;
        String type;
        try {
            status = photoSevice.batchUploadFiles(files,GALLERY_TYPE);
            type = "message";
        } catch (Exception e){
            status = e.getMessage();
            type = "exception";
        }
        attributes.addFlashAttribute(type,status);
        return REDIRECT;
    }

    @GetMapping("/gallery/{names}/delete")
    public String delete(@PathVariable String[] names, RedirectAttributes attributes){
        if (names.length == 0) return "No Files found need to be deleted";
        String result = "delete completed";
        try {
            photoSevice.deltePhotos(names);
        }catch (Exception e){
            result = e.getMessage();
        }
        attributes.addFlashAttribute("message",result);
        return REDIRECT;
    }

    @GetMapping("/gallery")
    public String getAllArtilcePic(Model model){
        model.addAttribute("articlePhotos",photoSevice.getAllUrlsByType(ARTICLE_TYPE));
        model.addAttribute("galleryPhotos",photoSevice.getAllUrlsByType(GALLERY_TYPE));
        return GALLERY;
    }
}
