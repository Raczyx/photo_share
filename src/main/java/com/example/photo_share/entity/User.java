package com.example.photo_share.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    private String email;
    private String username;
    private String password;
    private boolean status;
    private String image;
    private LocalDateTime createtime;
}
