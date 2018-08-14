package com.ddf.spring.annotation.service;

import com.ddf.spring.annotation.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author DDf on 2018/8/14
 */
@Service
public class PersonService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 不使用事务的方法保存一个person
     * @param person
     * @return
     */
    public int add(Person person) {
        String sql = "INSERT INTO PERSON(NAME, BIRTH_DAY, TEL, ADDRESS) VALUES (?, ?, ?, ?)";
        int i = jdbcTemplate.update(sql, person.getName(), person.getBirthDay(), person.getTel(), person.getAddress());
        if (i == 1) {
            throw new RuntimeException("抛出异常");
        }
        return i;
    }


    /**
     * 使用事务控制来保存一个person，在保存结束后抛出异常，看是否会回滚
     * @param person
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public int addWithTransactional(Person person) {
        String sql = "INSERT INTO PERSON(NAME, BIRTH_DAY, TEL, ADDRESS) VALUES (?, ?, ?, ?)";
        int i = jdbcTemplate.update(sql, person.getName(), person.getBirthDay(), person.getTel(), person.getAddress());
        if (i == 1) {
            throw new RuntimeException("抛出异常");
        }
        return i;
    }
}
