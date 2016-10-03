package com.cbinfo.repository;

import com.cbinfo.model.RequestModel;
import com.cbinfo.model.User;
import com.cbinfo.model.UserData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by islabukhin on 03.10.16.
 */
public interface RequestModelRepository extends CrudRepository<RequestModel, Long> {
    //List<RequestModel> findByUser(User user);

    List<RequestModel> findAll();

    RequestModel save(UserData userData);
}
