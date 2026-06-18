package com.example.demo.enums;

public enum Roles {
    ROLE_ADMIN("admin"),
    ROLE_CLIENTE("cliente"),
    ROLE_FUNCIONARIO("funcionario");

    private String role;

    Roles(String role){
        this.role = role;
    }

    public String getRole(){
        return role;
    }
}
