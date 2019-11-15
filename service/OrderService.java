package com.jing.blogs.service;

import com.jing.blogs.domain.Order;
import com.jing.blogs.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();
    List<Order> getOrdersByCoach(User coach);
    List<Order> getAllOrdersWithRoot(User user);
    void deleteOrder(Long id);
    Order saveOrder(Order order);
    Long countOrders(Long coachId);
}
