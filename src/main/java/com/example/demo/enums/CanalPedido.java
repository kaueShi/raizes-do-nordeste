package com.example.demo.enums;

public enum CanalPedido {
    WEB("web"),
    APP("app"),
    TOTEM("totem");

    private String canal;

    CanalPedido(String canal){
        this.canal = canal;
    }

    public String getCanal(){
        return canal;
    }
}

