package com.jing.blogs.dao;

import com.jing.blogs.domain.Order;
import com.jing.blogs.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order>  findAllByCoach(User coach);
}
