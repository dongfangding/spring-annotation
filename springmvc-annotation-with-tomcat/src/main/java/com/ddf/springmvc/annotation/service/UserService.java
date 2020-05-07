package com.ddf.springmvc.annotation.service;

import com.ddf.springmvc.annotation.configuration.dao.UserDao;
import com.ddf.springmvc.annotation.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author DDf on 2018/8/19
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Transactional(rollbackFor = Exception.class)
    public void addUser(User user) {
        userDao.addUser(user);
    }

    @Transactional(readOnly = true)
    public List<User> list() {
        return userDao.list();
    }
}
