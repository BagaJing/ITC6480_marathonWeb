package com.jing.blogs.web.admin;

import com.jing.blogs.domain.Podcast;
import com.jing.blogs.domain.User;
import com.jing.blogs.service.PodcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private PodcastService podcastService;
    @GetMapping("/podcast-post")
    public String post(Model model){
        model.addAttribute("podcast",new Podcast());
        return POST;
    }
    @PostMapping("/podcasts")
    public String post(@RequestParam(value = "audio") MultipartFile file,
                       RedirectAttributes attributes,
                       HttpSession session,
                       Podcast podcast) throws IOException {
        User user = (User) session.getAttribute("user");
        String author = user.getNickname();
        user = null;
        podcast.setAuthor(author);
        author = null;
        String saveStatus = podcastService.savePodcast(file, podcast);
        attributes.addFlashAttribute("message",saveStatus);
        return REDIRECT_POST;
    }
    @GetMapping("/podcasts")
    public String listPodcasts(Model model){
        model.addAttribute("podcasts",podcastService.findAll());
        return LIST;
    }
    @GetMapping("/deletePodcast/{id}")
    public String deletePodcast(RedirectAttributes attributes, @PathVariable Long id){
        podcastService.deleteById(id);
        attributes.addFlashAttribute("message","Deleted");
        return REDIRECT_POST;
    }
}
