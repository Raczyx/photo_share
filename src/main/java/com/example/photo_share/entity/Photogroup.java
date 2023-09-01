package com.example.photo_share.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Photogroup {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String username;
    private String email;
    private String description;
    private String title;
    private String cover;
}
