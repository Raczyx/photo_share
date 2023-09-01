package com.example.photo_share.service;

import com.example.photo_share.entity.ResponseBody;
import io.minio.errors.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface UserService {
    ResponseBody Login(String email,String password);

    ResponseBody Logup(String email,String username,String password);

    ResponseBody updateImages(String email, MultipartFile file) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

    ResponseBody updatePassword(String email,String password,String newPassword);

    ResponseBody getuserData(String email);

}
