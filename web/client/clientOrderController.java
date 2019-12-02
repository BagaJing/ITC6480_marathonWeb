package com.jing.blogs.web.client;

import com.jing.blogs.clientQueue.clientAction;
import com.jing.blogs.clientQueue.clientResultHolder;
import com.jing.blogs.util.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.async.DeferredResult;

@Controller
@RequestMapping("/client")
public class clientOrderController {
    @Autowired
    private clientAction action;
    @Autowired
    private clientResultHolder resultHolder;
    @GetMapping("/trainings")
    public DeferredResult<String> showTrainings(Model model){
        DeferredResult<String> result = new DeferredResult<>();
        String order = "TRAIN"+"-"+ MyBeanUtils.getRandomOrderNum(8);
        action.setTrainingView(order,model);
        resultHolder.getClientMap().put(order,result);
        return result;
        //action.setblog
    }
}
