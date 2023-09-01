package com.example.photo_share.service.Impl;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.photo_share.entity.ResponseBody;
import com.example.photo_share.entity.User;
import com.example.photo_share.mapper.UserMapper;
import com.example.photo_share.service.UserService;
import com.example.photo_share.utils.JwtUtil;
import com.example.photo_share.utils.MinioUtils;
import io.minio.errors.*;
import org.apache.catalina.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Security;
import java.time.LocalDateTime;
import java.util.HashMap;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MinioUtils minioUtils;

    @Override
    public ResponseBody Login(String email, String password) {

        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("email",email);
        User user = userMapper.selectOne(qw);
        if (user == null){
            return new ResponseBody<>().error("用户不存在");
        }
        String s = SecureUtil.md5(password);

        if (s.equals(user.getPassword()) ){
            String jwt = jwtUtil.buildJWT(new HashMap<>(), email, new HashMap<>());
            return new ResponseBody<>().success("登录成功",jwt);
        }else {
            return new ResponseBody<>().error("登录失败");
        }
    }

    @Override
    public ResponseBody Logup(String email, String username, String password) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("email",email).or().eq("username",username);
        User user = userMapper.selectOne(qw);
        if (user!=null){
            String msg = "已存在";
            if (user.getUsername().equals(username)){
                msg = "用户名 "+msg;
            }
            if (user.getEmail().equals(email)){
                msg = "邮箱 " +msg;
            }
            return new ResponseBody<>().error(msg);
        }
        user = new User();
        user.setStatus(true);
        user.setEmail(email);
        user.setPassword(SecureUtil.md5(password));
        user.setUsername(username);
        int insert = userMapper.insert(user);
        if (insert>0){
            return new ResponseBody<>().success("注册成功");
        }else {
            return new ResponseBody<>().error("未知错误，注册失败");
        }

    }

    /**
     * 更改头像
     * @param email
     * @param file
     * @return
     */
    @Override
    public ResponseBody updateImages(String email, MultipartFile file) throws ServerException, InvalidBucketNameException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        if (file==null){
            return new ResponseBody<>().error("文件不能为空");
        }
        String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        String fileName = SecureUtil.md5(LocalDateTime.now().toString()+email)+fileSuffix;
        String path = SecureUtil.md5(email);
        String url = minioUtils.uploadObject(file,path+"/"+fileName);
        boolean b = minioUtils.doesObjectExist(path+"/"+fileName);
        if (b){
            QueryWrapper<User> qw = new QueryWrapper<>();
            qw.eq("email",email);
            User user = new User();
            user.setImage(url);
            int update = userMapper.update(user, qw);
            if (update>0){
                return new ResponseBody<>().success("修改成功",url);
            }
        }
        return new ResponseBody<>().error("修改失败");
    }


    /**
     * 修改密码
     * @param email
     * @param password
     * @param newPassword
     * @return
     */
    @Override
    public ResponseBody updatePassword(String email, String password, String newPassword) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("email",email);
        User user = userMapper.selectOne(qw);
        if (user == null){
            return new ResponseBody<>().error("用户不存在");
        }
        if (!user.getPassword().equals(SecureUtil.md5(password))){
            return new ResponseBody<>().error("密码错误");
        }
        user.setPassword(SecureUtil.md5(newPassword));
        int update = userMapper.update(user, qw);
        if (update>0){
            return new ResponseBody<>().success("修改成功");
        }else {
            return new ResponseBody<>().error("更改失败");
        }
    }

    @Override
    public ResponseBody getuserData(String email) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("email",email);
        User user = userMapper.selectOne(qw);
        if (user == null){
            return new ResponseBody<>().error("用户不存在");
        }else {
            user.setPassword(null);
            return new ResponseBody<>().success("查询成功",user);
        }
    }
}
