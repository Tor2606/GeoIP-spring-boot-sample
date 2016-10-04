package com.cbinfo.repository;

import com.cbinfo.model.User;
import com.cbinfo.model.UserData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserDataRepository extends CrudRepository<UserData, Long>{
    //List<UserData> findByUser(User user);
    //UserData save(UserData userData);

    List<UserData> findAll();
}
