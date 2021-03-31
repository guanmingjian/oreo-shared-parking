package cn.oreo.server.pay.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.oreo.common.model.entity.po.Order;
import cn.oreo.common.model.entity.po.Reserve;
import cn.oreo.common.model.entity.po.User;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.server.pay.constant.WeChatConstant;
import cn.oreo.server.pay.mapper.OrderMapper;
import cn.oreo.server.pay.mapper.ReserveMapper;
import cn.oreo.server.pay.mapper.UserMapper;
import cn.oreo.server.pay.sdk.MyWXPayConfig;
import cn.oreo.server.pay.sdk.WXPay;
import cn.oreo.server.pay.sdk.WXPayUtil;
import cn.oreo.server.pay.service.WxPayService;
import cn.oreo.server.pay.vo.WxPayNotifyVO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.wxpay.sdk.WXPayConstants;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.KeyStore;
import java.util.*;

@Service
@Slf4j
public class WxPayServiceImpl implements WxPayService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ReserveMapper reserveMapper;

    @Autowired
    private UserMapper userMapper;

    private final static String IP = "8.129.237.80";

    @Override
    public Map<String,String> wxPay(String openId, String ip,String orderId,String status) throws Exception {

        BigDecimal price;
        if("1".equals(status)) {
            // 定金
            QueryWrapper<Reserve> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", orderId);
            Reserve reserve = reserveMapper.selectOne(queryWrapper);

            price = reserve.getEarnestPrice().multiply(new BigDecimal("100"));
        } else {
            // 租金
            // 根据 Id 查询订单记录
            QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", orderId);
            Order order = orderMapper.selectOne(queryWrapper);

            // 价格×100 按分计算
            price = order.getActualPrice().multiply(new BigDecimal("100"));
        }
        String outTradeNo = UUID.randomUUID().toString().replaceAll("-", "");
        // 1、拼接统一下单地址参数
        Map<String,String> paraMap = new HashMap<>();
        paraMap.put("body","共享停车位"); // 商家名称-销售商品类目、String（128）
        paraMap.put("openid",openId);
        paraMap.put("out_trade_no", outTradeNo);//订单号，每次都不一样,商品单号
        paraMap.put("spbill_create_ip",ip);
        paraMap.put("total_fee",price.stripTrailingZeros().toPlainString());//支付金额，1分钱0.01
        paraMap.put("trade_type","JSAPI");//支付类型
        log.info("paraMap为： "+paraMap);
        log.info("====================================================================");

        // 2、通过MyWxPayConfig创建WxPay对象，用于支付请求
        final String SUCCESS_NOTIFY="http://"+IP+":8301/pay/wxPay/success/"+status+"/"+orderId; //成功后的通知地址，用于更新订单状态
        boolean useSandbox = false; // 是否使用沙盒环境支付API，true不会真正扣钱
        MyWXPayConfig wxPayConfig = new MyWXPayConfig();
        WXPay wxPay = new WXPay(wxPayConfig, SUCCESS_NOTIFY, false, useSandbox);

        // 3、fileRequestDate会将上述参数用key = value形式和mchKey一起加密为sign
        Map<String,String> map = wxPay.unifiedOrder(wxPay.fillRequestData(paraMap),15000,15000);

        // 4、发送post请求“统一下单”接口，返回预支付id：prepay_id
        String prepayId = (String) map.get("prepay_id");
        String return_msg = (String) map.get("return_msg");
        String result_code = (String) map.get("result_code");
        log.info("xmlStr为： "+map);
        log.info("=====================================================================");
        if(return_msg.equals("OK") && result_code.equals("SUCCESS")){
            System.out.println("开始付款");
        }

        Map<String,String> payMap = new HashMap<>();
        payMap.put("appId", WeChatConstant.APPID);
        payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp()+"");
        payMap.put("nonceStr",WXPayUtil.generateNonceStr());
        if(useSandbox){
            payMap.put("signType", WXPayConstants.MD5);
        } else {
            payMap.put("signType",WXPayConstants.HMACSHA256);
        }
        payMap.put("package","prepay_id="+prepayId);

        // 5、通过appId，timeStamp，nonceStr，signType，package以及用户密钥进行key=value形式拼接并加密
        String paySign;
        if(useSandbox){
            paySign = WXPayUtil.generateSignature(payMap,WeChatConstant.MCH_KEY, WXPayConstants.SignType.MD5);
        } else {
            paySign = WXPayUtil.generateSignature(payMap,WeChatConstant.MCH_KEY,WXPayConstants.SignType.HMACSHA256);
        }
        payMap.put("paySign",paySign);
        System.out.println(paySign);
        // 6、将这6个参数传给前端
        log.info("payMap: "+payMap);
        return payMap;
    }

    @Override
    public String success(HttpServletRequest request,WxPayNotifyVO param,String status,Long id) throws Exception{
        log.info("success_notity: " + param);
        Order order = null;
        Reserve reserve = null;
        if("2".equals(status)) {
            // 租金
            QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("id", id);
            order = orderMapper.selectOne(queryWrapper);
        }
        if("1".equals(status)) {
            // 定金
            QueryWrapper<Reserve> reserveQueryWrapper = new QueryWrapper<>();
            reserveQueryWrapper.eq("id", id);
            reserve = reserveMapper.selectOne(reserveQueryWrapper);
        }
        System.out.println("======================查询完毕!===================================================");
        Map<String, String> result = new HashMap<String, String>();
        if ("SUCCESS".equals(param.getReturn_code())) {
            System.out.println("=====================成功支付-=================================================");
            // 返回给微信说明我们收到了
            result.put("return_code", "SUCCESS");
            result.put("return_msg", "OK");
            System.out.println("成功收款");
            // 付款成功修改数据库
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.eq("id",reserve.getUserId());
            User user = userMapper.selectOne(userQueryWrapper);
            if("1".equals(status)) {
                // 1 ：定金
                System.out.println("==================定金 1======================");
                // 担保金类型（1：押金 2：信用）
                reserve.setEarnestType("1");
                // 支付状态（1、未支付  2、已支付  3、已退款）
                reserve.setPayStatus("2");
                // 支付方式（1：微信支付 2：支付宝支付）
                reserve.setPayType("1");
                // 支付时间
                reserve.setPayTime(new Date());
                // 商品单号
                reserve.setOutTradeNo(param.getOut_trade_no());
                // 设置支付人姓名
                if(user.getRealName()!=null) {
                    reserve.setPayName(user.getRealName());
                } else {
                    reserve.setPayName(user.getUserName());
                }
            } else {
                // 2 租金
                System.out.println(order);
                System.out.println("==================租金========================");
                if(user.getRealName()!=null) {
                    order.setPayName(user.getRealName());
                }else{
                    order.setPayName(user.getUserName());
                }
                order.setUpdateTime(new Date()); // 更新订单的更新时间
                order.setOutTradeNo(param.getOut_trade_no()); // 存入商品单号(用于退款)
                order.setPayTime(new Date()); // 支付时间
                order.setPayStatus("2"); // 2 : 已支付
                // 调保证金退款
                System.out.println(order);
                System.out.println("开始退定金");
                QueryWrapper<Reserve> wrapper = new QueryWrapper<>();
                wrapper.eq("user_id",order.getUserId());
                wrapper.eq("pay_status","2");
                reserve = reserveMapper.selectOne(wrapper);

                refuse(reserve.getOutTradeNo(),"1");
                System.out.println("-----xxxxx----");
                // 改预约状态--已完成
                reserve.setReserveStatus("5"); // 5:已完成
                orderMapper.updateById(order); // 更新数据库
            }
            reserveMapper.updateById(reserve);
        } else{
            System.out.println("收款失败");
        }
        log.info(String.valueOf(param));
        return WXPayUtil.mapToXml(result);
    }

    @Override
    public OreoResponse refuse(String orderId, String status) {

        /**
         *此处应该更具订单id查询订单  然后获取订单里面的支付信息去
         *当然也可以把在别处判断完成后再传入参数调用退款
         *退款
         */
        System.out.println("开始退款");
        BigDecimal price;
        QueryWrapper<Reserve> reserveQueryWrapper = null;
        QueryWrapper<Order> orderQueryWrapper = null;
        Reserve reserve =null;
        Order order =null;
        if("1".equals(status)) {
            // 定金
            reserveQueryWrapper = new QueryWrapper<>();
            reserveQueryWrapper.eq("out_trade_no", orderId);
            reserve = reserveMapper.selectOne(reserveQueryWrapper);
            if(reserve == null){
                System.out.println("还未预约");
                return null;
            }

            price = reserve.getEarnestPrice().multiply(new BigDecimal("100"));
        } else {
            // 租金
            // 根据 Id 查询订单记录
            orderQueryWrapper = new QueryWrapper<>();
            orderQueryWrapper.eq("out_trade_no", orderId);
            order = orderMapper.selectOne(orderQueryWrapper);

            // 价格×100 按分计算
            price = order.getActualPrice().multiply(new BigDecimal("100"));
        }
        System.out.println("查询完毕");
        OreoResponse res=new OreoResponse();
        try {
            System.out.println("==========================");
            String nonce_str = WXPayUtil.generateNonceStr(); /*获取随机字符串*/
            Map<String, String> packageParams = new HashMap<>();
            packageParams.put("appid", WeChatConstant.APPID);
            packageParams.put("mch_id", WeChatConstant.MCH_ID);
            packageParams.put("nonce_str", nonce_str);
            packageParams.put("out_trade_no", orderId);//商户订单号,自己设计生成的
            packageParams.put("out_refund_no", RandomUtil.randomNumbers(32));//商户订单号,自己的订单ID
            packageParams.put("total_fee", price.stripTrailingZeros().toPlainString());//支付金额，这边需要转成字符串类型，否则后面的签名会失败
            // 分步退款(暂时不用)
            packageParams.put("refund_fee", price.stripTrailingZeros().toPlainString());//退款金额，这边需要转成字符串类型，否则后面的签名会失败
            String sign=WXPayUtil.generateSignature(packageParams, WeChatConstant.MCH_KEY);//生成签名
            packageParams.put("sign", sign);
            String xml = WXPayUtil.mapToXml(packageParams);//
            System.out.println(xml);
            String weixinPost = doRefund("https://api.mch.weixin.qq.com/secapi/pay/refund", xml);//加载证书 然后发送退款信息
            Map map = WXPayUtil.xmlToMap(weixinPost);//
            System.out.println(map);
            String result_code=(String) map.get("result_code");
            String return_msg=(String) map.get("return_msg");

            if (result_code.equals("SUCCESS")&&return_msg.equals("OK")) {

                System.out.println("开始更新数据库");
                if("1".equals(status)){
                    reserve.setPayStatus("3"); // 已退款
                    reserve.setEarnestRefundTime(new Date()); // 退款时间
                    // 担保金退还金额
                    String refundFee=(String) map.get("refund_fee");
                    reserve.setEarnestRefundPrice(new BigDecimal(refundFee));
                    // 担保金退还时间
                    reserve.setEarnestRefundTime(DateUtil.date());

                    reserveMapper.updateById(reserve);
                } else {
                    order.setUpdateTime(new Date());
                    order.setPayStatus("3"); // 退款状态
                    order.setOrderStatus("3"); // 3:取消状态
                    order.setCancelTime(new Date()); // 取消时间
                    orderMapper.updateById(order);
                }

                //String  orderNo=	(String) map.get("out_trade_no");
                System.out.println("退款成功");


            }

        }catch(Exception e){
            res.put("message",e.getMessage());
        }
        return res.message("退款成功!");
    }


    /**
     * 发送带有证书请求的post请求
     * @param url
     * @param data
     * @return
     * @throws Exception
     */
    @SuppressWarnings("deprecation")
    public  String doRefund(String url,String data) throws Exception {

        //获取文件流加载证书  最好放在resources目录下 自定心路径服务器上可能找不到
        ClassPathResource classPathResource = new ClassPathResource("apiclient_cert.p12");
        System.out.println(classPathResource);
        InputStream certStream = classPathResource.getInputStream();
        KeyStore keyStore  = KeyStore.getInstance("PKCS12");
        try {
            keyStore.load(certStream, WeChatConstant.MCH_ID.toCharArray());//这里写密码..默认是你的MCHID
        } finally {
            certStream.close();
        }
        SSLContext sslcontext = SSLContexts.custom()
                .loadKeyMaterial(keyStore, WeChatConstant.MCH_ID.toCharArray())//这里也是写密码的
                .build();
        // Allow TLSv1 protocol only
        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                sslcontext,
                new String[] { "TLSv1" },
                null,
                SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {
            HttpPost httpost = new HttpPost(url); // 设置响应头信息
            httpost.addHeader("Connection", "keep-alive");
            httpost.addHeader("Accept", "*/*");
            httpost.addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            httpost.addHeader("Host", "api.mch.weixin.qq.com");
            httpost.addHeader("X-Requested-With", "XMLHttpRequest");
            httpost.addHeader("Cache-Control", "max-age=0");
            httpost.addHeader("User-Agent", "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0) ");
            httpost.setEntity(new StringEntity(data, "UTF-8"));
            CloseableHttpResponse response = httpclient.execute(httpost);
            try {
                HttpEntity entity = response.getEntity();

                String jsonStr = EntityUtils.toString(response.getEntity(), "UTF-8");
                EntityUtils.consume(entity);
                return jsonStr;
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
    }



}
