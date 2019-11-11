package com.jing.blogs.web.client;

import com.jing.blogs.domain.Order;
import com.jing.blogs.domain.Trainning;
import com.jing.blogs.service.MailService;
import com.jing.blogs.service.OrderService;
import com.jing.blogs.service.TrainingService;
import com.jing.blogs.util.MyBeanUtils;
import org.hibernate.annotations.AttributeAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class OrderController {
    private final static int ORDER_DEFAULT_LENGTH = 10;
    @Autowired
    private TrainingService trainingService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private MailService mailService;
    @PostMapping("/postOrder")
    public String postOrder(@RequestParam(value = "name") String name,
                            @RequestParam(value = "serviceId") Long serviceId,
                            @RequestParam(value = "email") String email){
        String res = "";
        Order order = new Order();
        order.setCustomerName(name);
        Trainning service = trainingService.getTraining(serviceId);
        order.setSelectedTrain(service);
        order.setCoach(service.getCoach());
        order.setOrderId(MyBeanUtils.getRandomOrderNum(ORDER_DEFAULT_LENGTH));
        order.setEmailAddress(email);
        order.setStartDate(new Date());
        order.setEndDate(new Date());
        try{
            Order o = orderService.saveOrder(order);
            if (o!=null) res = "Succeed";
            mailService.sendEmail(service.getCoach().getEmail(),"New Order(TEST)","This is a test");
        } catch (Exception e){
            res = e.getMessage();
        }
        return res;
    }
}
