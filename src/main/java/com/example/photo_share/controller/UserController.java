package com.example.photo_share.controller;

import com.example.photo_share.entity.ResponseBody;
import com.example.photo_share.service.UserService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService service;

    @RequestMapping("/login")
    public ResponseBody login(String email,String password){
        return service.Login(email,password);
    }

    @RequestMapping("/logup")
    public ResponseBody logup(String email,String username,String password){
        return service.Logup(email, username, password);
    }

    @RequestMapping("/update/password")
    public ResponseBody updatePassword(String email,String password,String newpassword){
        return service.updatePassword(email, password, newpassword);
    }

    @RequestMapping("/update/cover")
    public ResponseBody updateCover(String email, MultipartFile file) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return service.updateImages(email, file);
    }

    @RequestMapping("/data")
    public ResponseBody userData(String email){
        return service.getuserData(email);
    }
}
