package com.art.intex.model;

import lombok.Data;

@Data
public class Person {

    private String login;
    private String password;
    private String surname;
    private Integer age;

    public Person() {
        login = "";
        password = "";
        surname = "";
        age = -1;
    }
}
