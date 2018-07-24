package com.zgq.service;

import com.zgq.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by z on 2018/6/24.
 */
public interface UserService {

    public List<User> getAllUser();

    public int update();
}
