package com.cbinfo.repository;

import com.cbinfo.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by islabukhin on 19.09.16.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findAll();

    User save(User user);

    User findByUserIp(String userIp);

    User findByEmail(String email);
}

