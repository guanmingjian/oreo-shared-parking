package cn.oreo.server.pay.service;

import cn.oreo.common.util.common.OreoResponse;

import cn.oreo.server.pay.vo.WxPayNotifyVO;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public interface WxPayService {


    Map<String,String> wxPay(String openId, String ip,String orderId,String status) throws Exception;

    String success(HttpServletRequest request,WxPayNotifyVO param,String status,Long id) throws Exception;

    OreoResponse refuse(String orderId, String status) throws Exception;

}
