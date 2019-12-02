package com.jing.blogs.clientQueue;
import com.jing.blogs.NotFoundException;
import com.jing.blogs.domain.Blog;
import com.jing.blogs.domain.Type;
import com.jing.blogs.service.BlogService;
import com.jing.blogs.service.TrainingService;
import com.jing.blogs.service.TypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
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
    @Async("taskAsyncPool")
    public void setblogs(String order, Model model, Pageable pageable,Long typeId){
        List<Type> types = typeService.listType();
        types = types.subList(0,4);
        Page<Blog> blogs = (typeId == -1)? blogService.listBlog(pageable) : blogService.listBlogByTypes(typeId,pageable);
        model.addAttribute("blogs",blogs);
        model.addAttribute("types",types);
        queue.offer(order);
    }
    @Async("taskAsyncPool")
    public void setBlogView(String order,Long blogId,Model model){
        Blog b  = blogService.getBlog(blogId);
        if (b==null) throw new NotFoundException();
        model.addAttribute("blog",b);
        queue.offer(order);
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
