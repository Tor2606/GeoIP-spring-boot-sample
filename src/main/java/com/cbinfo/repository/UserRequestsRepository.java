package com.cbinfo.repository;

import com.cbinfo.model.UserRequest;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRequestsRepository extends CrudRepository<UserRequest, Long> {
    List<UserRequest> findAll();
}
