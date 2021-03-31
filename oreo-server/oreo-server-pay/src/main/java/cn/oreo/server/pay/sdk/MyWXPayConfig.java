package cn.oreo.server.pay.sdk;

import cn.oreo.server.pay.constant.WeChatConstant;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

@Slf4j
public class MyWXPayConfig extends WXPayConfig {

    @Override
    String getAppID() {
        return WeChatConstant.APPID;
    }
    @Override
    String getMchID() {
        return WeChatConstant.MCH_ID;
    }
    @Override
    String getKey() {
        return WeChatConstant.MCH_KEY;
    }
    @Override
    InputStream getCertStream() {
        return null;
    }
    @Override
    IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String domain, long elapsedTimeMillis, Exception ex) {
                log.info("report");
            }
            @Override
            public DomainInfo getDomain(WXPayConfig config) {
                return new IWXPayDomain.DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
    }
}
