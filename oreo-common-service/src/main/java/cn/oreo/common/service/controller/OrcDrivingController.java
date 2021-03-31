package cn.oreo.common.service.controller;

import cn.oreo.common.model.entity.po.Car;
import cn.oreo.common.model.entity.po.OssFile;
import cn.oreo.common.service.configure.OssStorageConfig;
import cn.oreo.common.service.mapper.CarMapper;
import cn.oreo.common.service.orc.OrcDrivingLicense;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.constant.OreoConstant;
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
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/orcDriving")
@Api("行驶证识别控制类")
public class  OrcDrivingController {

    @Autowired
    private CarMapper carMapper;

    private String ENDPOINT;
    private String BUCKET_NAME;
    private String ACCESS_KEY_ID;
    private String ACCESS_KEY_SECRET;

    public OrcDrivingController(OssStorageConfig ossStorageConfig) {
        this.ENDPOINT = ossStorageConfig.getENDPOINT();
        this.BUCKET_NAME = ossStorageConfig.getBUCKET_NAME();
        this.ACCESS_KEY_ID = ossStorageConfig.getACCESS_KEY_ID();
        this.ACCESS_KEY_SECRET = ossStorageConfig.getACCESS_KEY_SECRET();
    }

    public String getUrl(MultipartFile multipartFile) {
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
     * 行驶证识别
     *
     * @param multipartFile     图片url
     * @param side              font: 正面  back: 反面
     * @param userId            用户id
     */
    @PostMapping("/drivingLicense/{side}/{userId}")
    @ResponseBody
    @ApiOperation("行驶证识别接口")
    public OreoResponse identifyDrivingLicense(@ApiParam(value = "图片", required = true) MultipartFile multipartFile,
                                            @ApiParam(value = "font：正面， back：反面", required = true) @PathVariable String side,
                                            @ApiParam(value = "用户id", required = true) @PathVariable Long userId) {

        String imgFileUrl = getUrl(multipartFile);
        // 根据行驶证上的车牌号码查询用户车辆信息
        JSONObject jsonObject = new OrcDrivingLicense().OrcGetJson(imgFileUrl, side);
        QueryWrapper<Car> wrapper = new QueryWrapper<>();
        wrapper.eq("car_number", jsonObject.getString("plate_num"));
        Car car = carMapper.selectOne(wrapper);
        try {
            // 数据库中存在该车牌号
            if (car != null) {
                return new OreoResponse().code("200").message(OreoConstant.MESSAGE_CAR_EXISTED);
            }
            car = new Car();
            if ("font".equals(side)) {
                car.setUserId(userId);
                car.setUserName(jsonObject.getString("owner"));         // 所有人姓名
                car.setCarBrand(jsonObject.getString("model"));         // 品牌型号
                car.setCarNumber(jsonObject.getString("plate_num"));    // 车牌号
                car.setCarType(jsonObject.getString("vehicle_type"));   // 车辆类型
                car.setRegisterTime(new Date());
                car.setDeleted(0);
            }
            carMapper.insert(car);
            return new OreoResponse().code("200").message("识别成功").data(jsonObject);
        } catch (Exception e) {
            return new OreoResponse().code("400").message("识别失败");
        }
    }
}