package com.example.photo_share.service.Impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.photo_share.entity.*;
import com.example.photo_share.entity.temp.Records;
import com.example.photo_share.mapper.*;
import com.example.photo_share.service.ImagesService;
import com.example.photo_share.utils.MinioUtils;
import io.minio.errors.*;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class ImagesServiceImpl implements ImagesService {


    @Autowired
    private CollectMapper collectMapper;

    @Autowired
    private ImagesMapper imagesMapper;

    @Autowired
    private RecordsMapper recordsMapper;

    @Autowired
    private PhotoGroupMapper photoGroupMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MinioUtils minioUtils;

    /**
     * 获取列表
     * @return
     */

    @Override
    public ResponseBody getImagesList(Integer page,Integer size) {
        IPage<Records> objectPage = new Page<>();
        objectPage.setSize(size);
        objectPage.setCurrent(page);
        QueryWrapper<Records> qw = new QueryWrapper<>();
        qw.groupBy("id","user","email","title","description","cover");
        qw.select("id","user","email","title","description","cover");
        qw.orderByDesc("count(*)");
        IPage<Records> recordsIPage = recordsMapper.selectPage(objectPage, qw);

        return new ResponseBody<>().success("刷新成功",recordsIPage.getRecords());
    }


    /**
     * 发布图片
     * @param email
     * @param title
     * @param description
     * @param files
     * @return
     */
    @Override
    public ResponseBody uploadImages(String email, String title, String description, MultipartFile[] files) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        String path = SecureUtil.md5(email);
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("email", email);
        User user = userMapper.selectOne(qw);
        if (user==null){
            return new ResponseBody<>().error("用户不存在");
        }
        List<String> list = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            String fileName = SecureUtil.md5(LocalDateTime.now().toString()+email)+fileSuffix;
            String url = minioUtils.uploadObject(file,path+"/"+fileName);
            list.add(url);
        }
//        MultipartFile file = files[0];
//        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//        String fileName = SecureUtil.md5(LocalDateTime.now().toString()+email)+fileSuffix;
//        String url = minioUtils.uploadObject(file,path+"/"+fileName);
//        list.add(url);
        Photogroup photogroup = new Photogroup();
        photogroup.setCover(list.get(0));
        photogroup.setEmail(email);
        photogroup.setTitle(title);
        photogroup.setDescription(description);
        photogroup.setUsername(user.getUsername());
        photoGroupMapper.insert(photogroup);
        QueryWrapper<Photogroup> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("email",email);
        queryWrapper.eq("cover",photogroup.getCover());
        queryWrapper.eq("title",photogroup.getTitle());
        Photogroup selectOne = photoGroupMapper.selectOne(queryWrapper);
        for (String s : list) {
            Images images = new Images();
            images.setPid(selectOne.getId());
            images.setUrl(s);
            imagesMapper.insert(images);
        }
        files.clone();
        return new ResponseBody<>().success("上传成功");
    }


    /**
     * 删除帖子
     * @param email
     * @param id
     * @return
     */
    @Override
    public ResponseBody deletePhtot(String email, Integer id) {
        int i = photoGroupMapper.deleteById(id);
        if (i>0){
            return new ResponseBody<>().success("删除成功");
        }else {
            return new ResponseBody<>().error("删除失败");
        }
    }

    /**
     * 收藏
     * @param email
     * @param id
     * @return
     */
    @Override
    public ResponseBody collectPhoto(String email, Integer id) {
        Collect collect = new Collect();
        collect.setEmail(email);
        collect.setPid(id);
        int insert = collectMapper.insert(collect);
        if (insert>0){
            return new ResponseBody<>().success("收藏成功");
        }else {
            return new ResponseBody<>().error("收藏失败");
        }
    }

    /**
     * 取消收藏
     * @param email
     * @param id
     * @return
     */
    @Override
    public ResponseBody uncollectPhoto(String email, Integer id) {
        QueryWrapper<Collect> qw = new QueryWrapper<>();
        qw.eq("email",email);
        qw.eq("pid",id);
        int delete = collectMapper.delete(qw);
        if (delete>0){
            return new ResponseBody<>().success("取消成功");
        }else {
            return new ResponseBody<>().error("取消失败");
        }

    }

    @Override
    public ResponseBody getImages(String email,Integer pid) {
        QueryWrapper<Images> qw = new QueryWrapper<>();
        qw.eq("pid",pid);
        List<Images> images = imagesMapper.selectList(qw);
        List<String> list = new ArrayList<>();
        for (Images image : images) {
            list.add(image.getUrl());
        }
        Temp temp = new Temp();
        temp.setUrl(list);
        QueryWrapper<Collect> qwc = new QueryWrapper<>();
        qwc.eq("email",email);
        qwc.eq("pid",pid);
        Collect collect = collectMapper.selectOne(qwc);
        temp.setCollect(collect!=null);
        return new ResponseBody<>().success("查询成功",temp);
    }

    @Override
    public ResponseBody myPhoto(String email) {
        QueryWrapper<Photogroup> qw = new QueryWrapper<>();
        qw.eq("email",email);
        List<Photogroup> photogroups = photoGroupMapper.selectList(qw);
        return new ResponseBody<>().success("查询成功",photogroups);
    }

    @Override
    public ResponseBody myCollect(String email) {
        QueryWrapper<Photogroup> qw = new QueryWrapper<>();
        qw.inSql("id","select pid from collect where email = '"+email+"'");
        List<Photogroup> photogroups = photoGroupMapper.selectList(qw);

        return new ResponseBody<>().success("查询成功",photogroups);
    }


    @Data
    class Temp{
        boolean isCollect;
        List<String> url;
    }
}
