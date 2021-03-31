package cn.oreo.common.service.controller;

import cn.hutool.core.util.RandomUtil;
import cn.oreo.common.service.service.SendSms;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.constant.OreoConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class SmsController {

    public static String prefix = OreoConstant.ATTR_REDIS_PHONE_CAPTCHA_PREFIX;

    @Autowired
    private SendSms sendSms;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/send/captcha/{phoneNums}")
    public OreoResponse sendCaptcha(@PathVariable("phoneNums") String phoneNums){

        //判断是否该手机120秒内已经收到过验证码
        boolean isExists = redisTemplate.hasKey(prefix+phoneNums);
        if (isExists){
            return new OreoResponse().code("400").message(OreoConstant.MESSAGE_CAPTCHA_EXISTED);
        }

        //生成验证码
        String code = RandomUtil.randomNumbers (6);
        HashMap<String, Object> param = new HashMap<>();
        param.put("code",code);

        boolean isSend = sendSms.send(phoneNums, param);

        if (isSend){

            //将手机号，验证码存入redis 时限120秒
            redisTemplate.opsForValue().set(prefix+phoneNums,code,120, TimeUnit.SECONDS);

            Map<Object,String> map = new HashMap<>();
            map.put("captcha",code);

            return new OreoResponse().code("200").message(OreoConstant.MESSAGE_CAPTCHA_SUCCESS).data(map);
        }else {
            return new OreoResponse().code("400").message(OreoConstant.MESSAGE_CAPTCHA_FAILURE);
        }
    }
}
