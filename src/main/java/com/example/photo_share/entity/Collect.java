package com.example.photo_share.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

@Data
public class Collect {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String email;
    private Integer pid;

}
