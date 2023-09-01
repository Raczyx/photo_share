package com.example.photo_share.service;


import com.example.photo_share.entity.ResponseBody;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface ImagesService {
    ResponseBody getImagesList(Integer page,Integer size);

    ResponseBody uploadImages(String email, String title, String description, MultipartFile[] files) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    ResponseBody deletePhtot(String email, Integer id);

    ResponseBody collectPhoto(String email, Integer id);

    ResponseBody uncollectPhoto(String email, Integer id);

    ResponseBody getImages(String email,Integer pid);

    ResponseBody myPhoto(String email);

    ResponseBody myCollect(String email);
}
