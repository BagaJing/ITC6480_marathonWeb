package com.jing.blogs.web.admin;

import com.jing.blogs.service.PhotoSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

//@RestController
@Controller
@RequestMapping("/admin")
public class PhotoController {
    @Autowired
    PhotoSevice photoSevice;
    final static String REDIRECT = "redirect:/admin/gallery";
    final static String GALLERY = "admin/gallery";
    @PostMapping("/gallery/upload")
    public String uploads(@RequestParam("files") MultipartFile[] files, RedirectAttributes attributes) throws IOException {
        if (files.length == 0) return "No files found need to be upload";
        String status;
        String type;
        try {
            status = photoSevice.batchUploadFiles(files);
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
    public String getAll(Model model){
        model.addAttribute("photos",photoSevice.getAllUrls());
        return GALLERY;
    }
}
