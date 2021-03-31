package cn.oreo.common.util.common;

import cn.oreo.common.util.constant.OreoConstant;
import lombok.extern.slf4j.Slf4j;

/**
 * 封装一些常用的处理代码
 * @author GuanMingJian
 * @since 2021/1/23
 */
@Slf4j
public class CommonCode {

    /**
     * 常用捕获异常处理方法
     * @param e
     * @return
     */
    public static OreoResponse returnException(Exception e){
        log.info(OreoConstant.MESSAGE_SYSTEM_INTERNAL_FAILED,e.getMessage());
        return new OreoResponse().code("302").message(OreoConstant.MESSAGE_OPERATE_FAILED);
    }
}
