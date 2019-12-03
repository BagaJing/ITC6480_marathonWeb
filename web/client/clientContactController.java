package com.jing.blogs.web.client;

import com.jing.blogs.clientQueue.clientAction;
import com.jing.blogs.clientQueue.clientResultHolder;
import com.jing.blogs.util.MyBeanUtils;
import com.jing.blogs.vo.contactEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/client")
public class clientContactController {
    @Autowired
    private clientAction action;
    @Autowired
    private clientResultHolder resultHolder;
    @GetMapping("/contact")
    public DeferredResult<String> contactPages(){
        DeferredResult<String> result = new DeferredResult<>();
        String order = "CONTACT"+"-"+ MyBeanUtils.getRandomOrderNum(8);
        action.setContactView(order);
        resultHolder.getClientMap().put(order,result);
        return result;
    }
    @PostMapping("/contact")
    public DeferredResult<String> postEmail(@RequestParam("name") String name,
                                            @RequestParam("email") String email,
                                            @RequestParam("phone") String phone,
                                            @RequestParam("message") String message,
                                            RedirectAttributes attributes){
        DeferredResult<String> result = new DeferredResult<>();
        contactEntity contact = new contactEntity();
        contact.setName(name); contact.setEmail(email); contact.setPhone(phone); contact.setMessage(message);
        String order = "PCONTACT"+"-"+ MyBeanUtils.getRandomOrderNum(8);
        action.setContactPost(order,contact,attributes);
        resultHolder.getClientMap().put(order,result);
        return result;
    }
}
