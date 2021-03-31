package cn.oreo.common.service.service.impl;

import cn.oreo.common.service.service.SendSms;
import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Data
@ConfigurationProperties(prefix = "sms")
public class SendSmsImpl implements SendSms {

    private String accsessKeyId;
    private String secret;
    private String SignName;
    private String TemplateCode;

    @Override
    public boolean send(String phoneNums, Map<String, Object> code) {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accsessKeyId, secret);
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");

        //手机号、验证码、签名、模板
        request.putQueryParameter("SignName", SignName);
        request.putQueryParameter("TemplateCode", TemplateCode);

        request.putQueryParameter("PhoneNumbers", phoneNums);

        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(code));
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
