package com.jing.blogs.web.client;

import com.jing.blogs.service.PhotoSevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.concurrent.Callable;

@Controller
@RequestMapping("/client")
public class galleryController {
    @Autowired
    private PhotoSevice photoSevice;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final String GALLERY = "client/gallery";
    @GetMapping("/gallery")
    public Callable<String> showGallery(@PageableDefault(size=5,sort = {"uploadTime"},direction = Sort.Direction.DESC) Pageable pageable, Model model){
        logger.info("start to render page");
        Callable<String> result = ()->{
            logger.info("subThread Task");
            model.addAttribute("galleryPage",photoSevice.listPhoto(pageable,2));
            return GALLERY;
        };
        logger.info("page prepared to show");
        return  result;
    }
}
