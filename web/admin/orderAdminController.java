package com.jing.blogs.web.admin;

import com.jing.blogs.domain.User;
import com.jing.blogs.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin")
public class orderAdminController {
    @Autowired
    private OrderService orderService;
    private final static String ORDERS_LIST = "admin/orders";
    @GetMapping("/orderList")
    public String listOrders(HttpSession session, Model model){
        User user = (User)session.getAttribute("user");
        model.addAttribute("orders",orderService.getAllOrdersWithRoot(user));
        return  ORDERS_LIST;
    }
}
