package cn.oreo.common.service.controller;

import cn.oreo.common.model.entity.po.OssFile;
import cn.oreo.common.model.entity.po.User;
import cn.oreo.common.service.configure.OssStorageConfig;
import cn.oreo.common.service.mapper.UserMapper;
import cn.oreo.common.service.orc.OrcIdCard;
import cn.oreo.common.util.common.OreoResponse;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@Controller
@RequestMapping("/orc")
@Api("身份证识别控制类")
public class OrcController {

    @Autowired
    private UserMapper userMapper;

    private  String ENDPOINT;
    private  String ACCESS_KEY_ID;
    private  String ACCESS_KEY_SECRET;
    private  String BUCKET_NAME;

    public OrcController(OssStorageConfig ossStorageConfig){
        this.ENDPOINT = ossStorageConfig.getENDPOINT();
        this.BUCKET_NAME = ossStorageConfig.getBUCKET_NAME();
        this.ACCESS_KEY_ID = ossStorageConfig.getACCESS_KEY_ID();
        this.ACCESS_KEY_SECRET = ossStorageConfig.getACCESS_KEY_SECRET();
    }


    public String getUrl(MultipartFile multipartFile){
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String newName = UUID.randomUUID() + "." + suffix;

        OSS client = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        OssFile ossFile = new OssFile();
        try {
            client.putObject(new PutObjectRequest(BUCKET_NAME, newName, new ByteArrayInputStream(multipartFile.getBytes())));
            ossFile.setPath("https://oss.oreo5.cn/" + newName);

        } catch (IOException e) {
            // 忽视异常
        }

        return ossFile.getPath();
    }

    /**
     * 身份证识别
     *
     *  localhost:8301/service/orc/idCard/font/id
     *  localhost:8301/service/orc/idCard/back/id
     *
     * @param multipartFile     图片url
     * @param side              font：正面   back：反面
     * @return
     */
    @PostMapping("/idCard/{side}/{id}")
    @ResponseBody
    @ApiOperation("身份证识别接口")
    public OreoResponse identifyIdCard(@ApiParam(value = "图片",required = true) MultipartFile multipartFile,
                                       @ApiParam(value = "font：正面， back：反面", required = true) @PathVariable String side,
                                        @ApiParam(value = "用户id",required = true) @PathVariable Long id){

        System.out.println("身份证识别开始");
        System.out.println("MultipartFile:"+multipartFile);
        if(multipartFile == null){
            return new OreoResponse().message("参数异常");
        }
        String imgFileUrl = getUrl(multipartFile);

        // 根据用户id查询用户信息
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("id", id);
        User user = userMapper.selectOne(wrapper);
        System.out.println(user);
        try {

            JSONObject jsonObject = new OrcIdCard().OrcGetJson(imgFileUrl,side);

            // 更新用户信息
            if("font".equals(side)) {
                user.setIdCard(jsonObject.getString("num"));
                user.setAddress(jsonObject.getString("address"));
                user.setGender(jsonObject.getString("sex"));
                user.setBorn(jsonObject.getString("birth"));
                user.setNation(jsonObject.getString("nationality"));
                user.setRealName(jsonObject.getString("name"));
                user.setInfoCode("1");
                user.setAccountAmount(100.0);//设置信用分100
                System.out.println(user);
            } else if("back".equals(side)){
                user.setBeginDate(jsonObject.getString("start_date"));
                user.setEndDate(jsonObject.getString("end_date"));
                user.setDepartment(jsonObject.getString("issue"));
                System.out.println(user);
            }

            //存储到数据库中
            userMapper.updateById(user);
            return new OreoResponse().code("200").message("识别成功").data(jsonObject);

        } catch (Exception e) {
            return new OreoResponse().code("400").message("识别失败");
        }
    }

}
