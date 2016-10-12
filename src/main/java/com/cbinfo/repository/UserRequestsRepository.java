package com.cbinfo.repository;

import com.cbinfo.model.UserRequest;
import org.springframework.data.repository.CrudRepository;

public interface UserRequestsRepository extends CrudRepository<UserRequest, Long> {
}
