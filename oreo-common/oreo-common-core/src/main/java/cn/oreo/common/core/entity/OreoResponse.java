package cn.oreo.common.core.entity;

import java.util.HashMap;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
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
}
