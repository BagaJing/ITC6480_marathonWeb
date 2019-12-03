package com.jing.blogs.clientQueue;
import com.jing.blogs.NotFoundException;
import com.jing.blogs.domain.Blog;
import com.jing.blogs.domain.Type;
import com.jing.blogs.domain.UserBasic;
import com.jing.blogs.service.*;
import com.jing.blogs.util.MyBeanUtils;
import com.jing.blogs.vo.contactEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@Component
public class clientAction {
    private Logger logger = LoggerFactory.getLogger(getClass());
    public Queue<String> queue = new LinkedList<>();
    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TrainingService trainService;
    @Autowired
    private MailService mailService;
    @Autowired
    private UserService userService;
   //@Async("taskAsyncPool")
    public void setblogs(String order, Model model, Pageable pageable,Long typeId){
        List<Type> types = typeService.listType();
        types = types.size()>4 ? types.subList(0,4):types;
        Page<Blog> blogs = (typeId == -1)? blogService.listBlog(pageable) : blogService.listBlogByTypes(typeId,pageable);
        model.addAttribute("blogs",blogs);
        model.addAttribute("types",types);
        queue.offer(order);
    }
    //@Async("taskAsyncPool")
    public void setBlogView(String order,Long blogId,Model model){
        Blog b  = blogService.getAndConvert(blogId);
        if (b==null) throw new NotFoundException();
        model.addAttribute("blog",b);
        queue.offer(order);
    }
    @Async("taskAsyncPool")
    public void setContactPost(String order, contactEntity contact, RedirectAttributes attributes){
        String content = MyBeanUtils.contactToHtmlContent(contact);
        List<UserBasic> list = userService.listUsersInfo();
        String feedBack = "Hello "+contact.getName()+". Thanks for contact With us. We will response you ASAP!";
        attributes.addFlashAttribute("message",feedBack);
        queue.offer(order);
        try{
            for (UserBasic coach : list)
                mailService.sendHtmlEmail(coach.getEmail(),"New Contact from Customer !",content);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Async("taskAsyncPool")
    public void setIndex(String order){
        queue.offer(order);
    }
    @Async("taskAsyncPool")
    public void setCoach(String order){
        queue.offer(order);
    }
    @Async("taskAsyncPool")
    public void setContactView(String order){ queue.offer(order);}
    @Async("taskAsyncPool")
    public void setTrainingView(String order,Model model){
        model.addAttribute("trains",trainService.listAll());
        queue.offer(order);
    }

    public Queue<String> getQueue() {
        return queue;
    }
    public void setQueue(Queue<String> queue) {
        this.queue = queue;
    }
}
