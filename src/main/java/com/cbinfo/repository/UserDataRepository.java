package com.cbinfo.repository;

import com.cbinfo.model.User;
import com.cbinfo.model.UserData;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by islabukhin on 19.09.16.
 */
public interface UserDataRepository extends CrudRepository<UserData, Long>{
    //List<UserData> findByUser(User user);

    List<UserData> findAll();

    UserData save(UserData userData);
}
