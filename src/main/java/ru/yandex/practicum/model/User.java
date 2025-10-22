package ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String email;
    private String password;
    private String name;

    public String getName() {
        return name;
    }

    public void withName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void withEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void withPassword(String password) {
        this.password = password;
    }
}
