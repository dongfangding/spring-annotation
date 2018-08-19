package com.ddf.springmvc.annotation.controller;

import com.ddf.springmvc.annotation.entity.User;
import com.ddf.springmvc.annotation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author DDf on 2018/8/19
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/add")
    public List<User> addUser(@RequestBody User user) {
        userService.addUser(user);
        return userService.list();
    }

    @RequestMapping("/list")
    public List<User> list() {
        return userService.list();
    }
}
