package com.example.photo_share;

import com.example.photo_share.entity.ResponseBody;
import com.example.photo_share.service.ImagesService;
import com.example.photo_share.service.UserService;
import io.minio.errors.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class PhotoShareApplicationTests {


    @Autowired
    private ImagesService imagesService;

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() throws IOException, ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
//        ResponseBody responseBody = imagesService.collectPhoto("111", 2);
//        System.out.println("收藏:"+responseBody.toString());
//        ResponseBody responseBody1 = imagesService.uncollectPhoto("111", 2);
//        System.out.println("取消收藏:"+responseBody1.toString());
//        ResponseBody imagesList = imagesService.getImagesList(1, 5);
//        System.out.println("列表："+imagesList);
//        ResponseBody images = imagesService.getImages("111", 1);
//        System.out.println("具体:"+images.toString());
//        userService.Logup("555","555","555");
//        ResponseBody login = userService.Login("555", "555");
//        System.out.println(login);
//        ResponseBody responseBody = userService.updatePassword("555", "666", "555");
//        System.out.println(responseBody);
        String[] list ={"C:\\Users\\xyz17\\Pictures\\Acer\\Acer_Wallpaper_01_5000x2814.jpg","C:\\Users\\xyz17\\Pictures\\Acer\\Acer_Wallpaper_02_5000x2813.jpg","C:\\Users\\xyz17\\Pictures\\Acer\\Acer_Wallpaper_03_5000x2814.jpg"};
        List<MockMultipartFile> files = new ArrayList<>();

        for (String s : list) {
            File file = new File(s);
            FileInputStream fileInputStream = new FileInputStream(file);
            MockMultipartFile mockMultipartFile = new MockMultipartFile(file.getName(), file.getName(), null, fileInputStream);
            files.add(mockMultipartFile);
        }
        MockMultipartFile[] mockMultipartFiles = files.toArray(new MockMultipartFile[3]);
//        imagesService.uploadImages("111","title","test", mockMultipartFiles);
        ResponseBody imagesList = imagesService.getImagesList(1, 5);
        System.out.println(imagesList);
//        ResponseBody responseBody = userService.updateImages("111", mockMultipartFile);
//        System.out.println(responseBody);
    }

    @Test
    void delete(){
        ResponseBody responseBody = userService.updatePassword("666", "11", "666");
        System.out.println(responseBody);
    }

}
