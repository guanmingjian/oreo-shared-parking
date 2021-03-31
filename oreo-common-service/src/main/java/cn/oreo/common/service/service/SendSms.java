package cn.oreo.common.service.service;

import java.util.Map;

public interface SendSms {

    boolean send(String phoneNums, Map<String, Object> code);
}
