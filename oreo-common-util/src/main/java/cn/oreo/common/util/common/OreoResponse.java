package cn.oreo.common.util.common;

import cn.oreo.common.util.constant.OreoConstant;
import lombok.Data;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Data
@Slf4j
@ToString
public class OreoResponse extends HashMap<String, Object> {

    private static final long serialVersionUID = -8713837118340960775L;

    public OreoResponse code(String code) {
        this.put("code", code);
        return this;
    }

    public OreoResponse message(String message) {
        this.put("message", message);
        return this;
    }

    public OreoResponse data(Object data) {
        this.put("data", data);
        return this;
    }

    @Override
    public OreoResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public Object getCode() {
        return get("code");
    }

    public String getMessage() {
        return String.valueOf(get("message"));
    }

    public Object getData() {
        return get("data");
    }

    /**
     * 返回成功
     * @return
     */
    public static OreoResponse success(){

        return new OreoResponse().code("200");
    }

    /**
     * 返回成功
     * @param message
     * @return
     */
    public static OreoResponse successMessage(String message){

        return new OreoResponse().code("200").message(message);
    }

    /**
     * 返回成功
     * @param data
     * @return
     */
    public static OreoResponse successData(Object data){

        return new OreoResponse().code("200").data(data);
    }

    /**
     * 返回成功
     * @param message
     * @return
     */
    public static OreoResponse success(String message,Object data){

        if(!message.isEmpty()&&message!=""&&message!=null){
            if(data!=null&&data!=""){
                return new OreoResponse().code("200").message(message).data(data);
            }else {
                return new OreoResponse().code("200").message(message);
            }
        }else {
            return new OreoResponse().code("200");
        }
    }

    /**
     * 返回失败
     * @param message
     * @return
     */
    public static OreoResponse failure(String message){
        log.info(OreoConstant.MESSAGE_CUSTOMIZE_EXCEPTION+":"+message);
        return new OreoResponse().code("300").message(message);
    }
}
