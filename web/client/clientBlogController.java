package com.jing.blogs.web.client;

import com.jing.blogs.clientQueue.clientAction;
import com.jing.blogs.clientQueue.clientResultHolder;
import com.jing.blogs.util.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;

@Controller
@RequestMapping("/client")
public class clientBlogController {
    @Autowired
    private clientAction action;
    @Autowired
    private clientResultHolder resultHolder;

    @GetMapping("/goodstuff/{typeId}")
    public DeferredResult<String> goodstuff(@PathVariable Long typeId,
                                            Model model,
                                            @PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable){
        DeferredResult<String> result = new DeferredResult<>();
        String order = "BLOGS"+"-"+MyBeanUtils.getRandomOrderNum(8);
        action.setblogs(order,model,pageable,typeId);
        resultHolder.getClientMap().put(order,result);
        return result;
        //action.setblogs();
    }
    @GetMapping("/blog/{blogId}")
    public DeferredResult<String> blogView(@PathVariable Long blogId,
                                           Model model){
        DeferredResult<String> result = new DeferredResult<>();
        String order = "ARTICLE"+"-"+MyBeanUtils.getRandomOrderNum(8);
        action.setBlogView(order,blogId,model);
        resultHolder.getClientMap().put(order,result);
        return result;
    }
}
