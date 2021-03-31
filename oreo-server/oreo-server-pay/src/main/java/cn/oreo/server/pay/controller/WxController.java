package cn.oreo.server.pay.controller;

import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.server.pay.constant.WeChatConstant;
import cn.oreo.server.pay.service.WxPayService;

import cn.oreo.server.pay.utils.HttpRequestUtils;
import cn.oreo.server.pay.vo.WxPayNotifyVO;

import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@Slf4j
@RequestMapping("/pay")
public class WxController {

    @Autowired
    private WxPayService wxPayService;

    /**
     * 登录
     * @param code
     * @return
     */
    @PostMapping("/login/{code}")
    @ResponseBody
    public String login(@PathVariable String code){
        String url = String.format("https://api.weixin.qq.com/sns/jscode2session?" +
                        "appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                WeChatConstant.APPID,WeChatConstant.SECRET,code);
        String result = HttpRequestUtils.sendGet(url,null);
        log.info(result);
        System.out.println(result);
        return result;
    }

    /**
     * 支付
     * 是定金还是租金（通过前端传标识符）
     * @param request
     * @param openId
     * @param orderId 预约表id或者订单表id（具体看status状态）
     * @param status 1：定金   2：租金
     * @return
     * @throws Exception
     */
    @PostMapping("/pay/{openId}")
    @ResponseBody
    public Map<String,String> pay(HttpServletRequest request, @PathVariable String openId,String orderId, @ApiParam("收费方式(1：定金 2：租金)") String status) throws Exception {
        //获取请求的ip地址
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
            ip = request.getRemoteAddr();
        }
        if(ip.indexOf(",")!=-1){
            String[] ips = ip.split(",");
            ip = ips[0].trim();
        }
        System.out.println("openId : "+openId);
        return wxPayService.wxPay(openId,ip,orderId,status);
    }

    /**
     * 支付成功的回调通知
     * 等项目上线之后，才能测试，因为localhost不是公网，微信那里返回通知不了我们
     * 同理
     * @param request
     * @param param
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/success/{status}/{id}",produces = MediaType.TEXT_PLAIN_VALUE)//xml
    @ResponseBody
    public String success(HttpServletRequest request,
                          @RequestBody WxPayNotifyVO param,
                          @PathVariable(value = "status") String status,
                          @PathVariable(value = "id") Long id) throws Exception {
        return wxPayService.success(request,param,status,id);
    }


    /**
     * 退款申请
     * @param request
     * @param orderId  订单号（out_trade_no）
     * @return
     * @throws Exception
     */
    @GetMapping("/refund")
    @ResponseBody
    public OreoResponse Refund(HttpServletRequest request, String orderId, String status) throws Exception {
        System.out.println(orderId);
        return wxPayService.refuse(orderId,status);
    }

}
