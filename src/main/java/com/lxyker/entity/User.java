package com.lxyker.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {
    private Integer id;
    private String username;
    private String password;
    private String name;
    private Date birthday;
    private Double salary;
}