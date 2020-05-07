package com.ddf.springmvc.annotation.configuration.dao;

import com.ddf.springmvc.annotation.configuration.exception.GlobalException;
import com.ddf.springmvc.annotation.configuration.exception.GlobalExceptionEnum;
import com.ddf.springmvc.annotation.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author DDf on 2018/8/19
 */
@Repository
public class UserDao {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void addUser(User user) {

        String select = "SELECT * FROM USER WHERE NAME = ?";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(select, user.getName());
        if (list != null && list.size() > 0) {
            throw new GlobalException(GlobalExceptionEnum.USER_EXIST, user.getName());
        }

        String sql = "INSERT INTO USER(NAME, PASSWORD, BIRTH_DAY, TEL, ADDRESS) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, user.getName(), user.getPassword(), user.getBirthDay(), user.getTel(), user.getAddress());
    }

    public List<User> list() {
        String sql = "SELECT * FROM USER ";
        return jdbcTemplate.query(sql, new User());
    }
}
