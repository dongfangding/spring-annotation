package com.ddf.springmvc.annotation.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author DDf on 2018/8/19
 */
public class User implements RowMapper {
    private Integer id;
    private String name;
    private String password;
    private Date birthDay;
    private String address;
    private String tel;

    public User() {
    }

    public User(Integer id, String name, String password, Date birthDay, String address, String tel) {
        this.id = id;
        this.name = name;
        this.password = password;
        this.birthDay = birthDay;
        this.address = address;
        this.tel = tel;
    }

    public User(String name, String password, Date birthDay, String address, String tel) {
        this.name = name;
        this.password = password;
        this.birthDay = birthDay;
        this.address = address;
        this.tel = tel;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDay() {
        return birthDay;
    }

    public void setBirthDay(Date birthDay) {
        this.birthDay = birthDay;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public User mapRow(ResultSet resultSet, int i) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setPassword(resultSet.getString("password"));
        user.setBirthDay(resultSet.getDate("BIRTH_DAY"));
        user.setAddress(resultSet.getString("address"));
        user.setTel(resultSet.getString("tel"));
        return user;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", birthDay=" + birthDay +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}
