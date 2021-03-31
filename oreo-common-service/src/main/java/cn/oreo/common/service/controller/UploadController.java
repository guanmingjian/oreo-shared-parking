package cn.oreo.common.service.controller;

import cn.hutool.core.date.DateUtil;
import cn.oreo.common.service.configure.OssStorageConfig;
import cn.oreo.common.service.mapper.FileInfoMapper;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.model.entity.po.OssFile;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/oss")
public class UploadController {

    @Autowired
    private FileInfoMapper fileInfoMapper;

    private  String ENDPOINT;
    private  String ACCESS_KEY_ID;
    private  String ACCESS_KEY_SECRET;
    private  String BUCKET_NAME;

    public  UploadController(OssStorageConfig ossStorageConfig){
        this.ENDPOINT = ossStorageConfig.getENDPOINT();
        this.BUCKET_NAME = ossStorageConfig.getBUCKET_NAME();
        this.ACCESS_KEY_ID = ossStorageConfig.getACCESS_KEY_ID();
        this.ACCESS_KEY_SECRET = ossStorageConfig.getACCESS_KEY_SECRET();
    }

    /**
     * 文件上传
     * @param   multipartFile       文件
     * @return  ResponseResult      文件路径
     */
    @PostMapping(value = "/upload")
    public OreoResponse uploadFile(MultipartFile multipartFile) {

        String fileName = multipartFile.getOriginalFilename();

        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String newName = UUID.randomUUID() + "." + suffix;

        OSS client = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);

        try {

            client.putObject(new PutObjectRequest(BUCKET_NAME, newName, new ByteArrayInputStream(multipartFile.getBytes())));

            OssFile ossFile = new OssFile();
            ossFile.setPath("https://oss.oreo5.cn/" + newName);
            ossFile.setAddTime(DateUtil.date());

            // 存入数据库
            fileInfoMapper.insert(ossFile);

            return new OreoResponse().code("200").message("文件上传成功").data(ossFile);

        } catch (IOException e) {
            return new OreoResponse().code("400").message("文件上传失败，请重试");
        } finally {
            client.shutdown();
        }
    }
}

