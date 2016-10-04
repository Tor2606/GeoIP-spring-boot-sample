package com.cbinfo.repository;

import com.cbinfo.model.UserRequests;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by islabukhin on 03.10.16.
 */
public interface UserRequestsRepository extends CrudRepository<UserRequests, Long> {
    //List<UserRequests> findByUser(User user);

    List<UserRequests> findAll();

}
