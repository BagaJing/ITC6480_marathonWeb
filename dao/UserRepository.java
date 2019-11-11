package com.jing.blogs.dao;

import com.jing.blogs.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User,Long> {

    User findByUsernameAndPassword(String username, String password);
    @Query("select u from User u where u.id=?1")
    User findOne(Long id);
}
