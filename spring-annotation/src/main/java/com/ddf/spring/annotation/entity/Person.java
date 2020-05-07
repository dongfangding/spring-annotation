package com.ddf.spring.annotation.entity;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author DDf on 2018/8/14
 */
public class Person implements RowMapper<Person> {
    private Integer id;
    private String name;
    private Date birthDay;
    private String address;
    private String tel;

    public Person() {
    }

    public Person(Integer id, String name, Date birthDay, String address, String tel) {
        this.id = id;
        this.name = name;
        this.birthDay = birthDay;
        this.address = address;
        this.tel = tel;
    }

    public Person(String name, Date birthDay, String address, String tel) {
        this.name = name;
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

    @Override
    public Person mapRow(ResultSet resultSet, int i) throws SQLException {
        Person person = new Person();
        person.setId(resultSet.getInt("id"));
        person.setName(resultSet.getString("name"));
        person.setBirthDay(resultSet.getDate("birthDay"));
        person.setAddress(resultSet.getString("address"));
        person.setTel(resultSet.getString("tel"));
        return person;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDay=" + birthDay +
                ", address='" + address + '\'' +
                ", tel='" + tel + '\'' +
                '}';
    }
}
