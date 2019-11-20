package com.jing.blogs.web.admin;

import com.jing.blogs.domain.Blog;
import com.jing.blogs.domain.User;
import com.jing.blogs.service.BlogService;
import com.jing.blogs.service.PhotoSevice;
import com.jing.blogs.service.TagService;
import com.jing.blogs.service.TypeService;
import com.jing.blogs.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.concurrent.Callable;

@Controller
@RequestMapping("/admin")
public class BlogController {



    private final String INPUT="admin/blogs-input";
    private final String LIST="admin/blogs";
    private final String REDIRECT_LIST="redirect:/admin/blogs";
    private final String VIEW = "admin/blog";
    private final static int ARTICLE_TYPE = 1;
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @Autowired
    private PhotoSevice photoSevice;

    @GetMapping("/blogs")
    public Callable<String> blogs(@PageableDefault(size=5,sort = {"updateTime"},direction = Sort.Direction.DESC)
                                    Pageable pageable,
                                    BlogQuery blog,
                                    Model model) {
        Callable<String> result = ()-> {
            model.addAttribute("types", typeService.listType());
            model.addAttribute("page", blogService.listBlog(pageable, blog));
            return LIST;
        };
        return result;
    }

    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size=5,sort = {"updateTime"},direction = Sort.Direction.DESC)
                                Pageable pageable,
                        BlogQuery blog,
                        Model model) {
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return "admin/blogs :: blogList";
    }

    @GetMapping("/blogs/input")
    public String input(Model model) {
        setTypeaAndTag(model);
        model.addAttribute("blog",new Blog());
        return INPUT;
    }

    private  void setTypeaAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("photos",photoSevice.getAllUrlsByType(ARTICLE_TYPE));
    }

    @GetMapping("/blogs/{id}/input")
    public Callable<String> editInput(@PathVariable Long id, Model model) {
        Callable<String> result = ()->{
            setTypeaAndTag(model);
            Blog blog = blogService.getBlog(id);
            blog.init();
            model.addAttribute("blog",blogService.getBlog(id));
            return INPUT;
        };
        return  result;
    }

    @PostMapping("/blogs")
    public String post(Blog blog,
                       RedirectAttributes attributes,
                       HttpSession session) {
        blog.setUser((User) session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if (blog.getId() == null) {
            b  = blogService.saveBlog(blog);
        } else {
            b =blogService.updateBlog(blog.getId(),blog);
        }
        if (b == null) {
            attributes.addFlashAttribute("message","Submit Denied");
        } else {
            attributes.addFlashAttribute("message","Successfully Submitted");
        }
        return  REDIRECT_LIST;
    }
    @GetMapping("blogs/{id}")
    public String view(@PathVariable Long id,Model model){
        model.addAttribute("blog",blogService.getAndConvert(id));
        return VIEW;
    }
    @GetMapping("/blogs/{id}/delete")
    public Callable<String> delete(@PathVariable Long id,RedirectAttributes attributes){
        Callable<String> result= ()-> {
            blogService.deleteBlog(id);
            attributes.addFlashAttribute("message", "Successfully deleted");
            return REDIRECT_LIST;
        };
        return  result;
    }
}
