package com.jing.blogs.web.client;

import com.jing.blogs.domain.Order;
import com.jing.blogs.domain.Trainning;
import com.jing.blogs.domain.User;
import com.jing.blogs.service.MailService;
import com.jing.blogs.service.OrderService;
import com.jing.blogs.service.TrainingService;
import com.jing.blogs.util.MyBeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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
        Date endDate = new Date();
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(endDate);
        calendar.add(calendar.DATE,order.getSelectedTrain().getDurations());
        order.setEndDate(calendar.getTime());
        String content = MyBeanUtils.getHtmlContent(order.getCustomerName(),order.getSelectedTrain().getName(),
                order.getSelectedTrain().getId(),order.getOrderId(),order.getEmailAddress());
        try{
            Order o = orderService.saveOrder(order);
            if (o!=null) res = "Succeed";
            mailService.sendHtmlEmail(service.getCoach().getEmail(),"A new Order!",content);
        } catch (Exception e){
            res = e.getMessage();
        }
        return res;
    }

}
