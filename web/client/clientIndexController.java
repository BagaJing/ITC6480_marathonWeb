package com.jing.blogs.web.client;

import com.amazonaws.services.apigateway.model.Model;
import com.jing.blogs.clientQueue.clientAction;
import com.jing.blogs.clientQueue.clientResultHolder;
import com.jing.blogs.util.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;

@Controller
@RequestMapping("/client")
public class clientIndexController {
    @Autowired
    private clientAction action;
    @Autowired
    private clientResultHolder resultHolder;
    @GetMapping("/index")
    public DeferredResult<String> showIndex(){
        DeferredResult<String> result = new DeferredResult<>();
        String order = "INDEX"+"-"+ MyBeanUtils.getRandomOrderNum(8);
        action.setIndex(order);
        resultHolder.getClientMap().put(order,result);
        return result;
    }
    @GetMapping("/coach")
    public DeferredResult<String> showCoach(){
        DeferredResult<String> result = new DeferredResult<>();
        String order = "COACH"+"-"+ MyBeanUtils.getRandomOrderNum(8);
        action.setIndex(order);
        resultHolder.getClientMap().put(order,result);
        return result;
    }
}
