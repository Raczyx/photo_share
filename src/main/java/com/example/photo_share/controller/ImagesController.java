package com.example.photo_share.controller;


import com.example.photo_share.entity.ResponseBody;
import com.example.photo_share.service.ImagesService;
import io.minio.errors.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@RestController

public class ImagesController {
    @Autowired
    private ImagesService service;

    @RequestMapping("/list")
    public ResponseBody getList(Integer page,Integer size){
        return service.getImagesList(page,size);
    }

    @RequestMapping("/delete")
    public ResponseBody deletePhtot(String email,Integer id){
        return service.deletePhtot(email, id);
    }

    @RequestMapping("/get")
    public ResponseBody getImages(String email,Integer id){
        return service.getImages(email, id);
    }

    @RequestMapping("/collect")
    public ResponseBody collectPhoto(String email,Integer id){
        return service.collectPhoto(email, id);
    }

    @RequestMapping("/uncollect")
    public ResponseBody uncollectPhoto(String email,Integer id){
        return service.uncollectPhoto(email,id);
    }

    @RequestMapping("/upload")
    public ResponseBody uploadImages(String email, String title, String description, MultipartFile[] files) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return service.uploadImages(email, title, description, files);
    }

    @RequestMapping("/myphoto")
    public ResponseBody myPhoto(String email){
        return service.myPhoto(email);
    }

    @RequestMapping("/mycollect")
    public ResponseBody myCollect(String email){
        return service.myCollect(email);
    }
}
