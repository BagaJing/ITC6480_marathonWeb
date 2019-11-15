package com.jing.blogs.service;

import com.jing.blogs.dao.OrderRepository;
import com.jing.blogs.domain.Order;
import com.jing.blogs.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserService userService;
    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersByCoach(User coach) {
        return orderRepository.findAllByCoach(coach);
    }

    @Override
    public List<Order> getAllOrdersWithRoot(User user) {
        int access = user.getType();
        if(access == 1) return  getAllOrders();
        else return getOrdersByCoach(user);
    }

    @Override
    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }


    @Override
    @Transactional
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Long countOrders(Long coachId) {
        if (coachId == -1) return (long) getAllOrders().size();
        return (long)getOrdersByCoach(userService.findUserById(coachId)).size();
    }
}
