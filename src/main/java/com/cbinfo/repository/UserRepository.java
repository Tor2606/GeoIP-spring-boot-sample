package com.cbinfo.repository;

import com.cbinfo.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long> {
    List<User> findByUserIp(String userIp);

    User findOneByEmail(String email);
}

