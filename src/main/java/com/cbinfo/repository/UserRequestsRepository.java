package com.cbinfo.repository;

import com.cbinfo.model.User;
import com.cbinfo.model.UserRequests;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRequestsRepository extends CrudRepository<UserRequests, Long> {
    List<UserRequests> findAll();
}
