package cn.oreo.common.core.exception;

/**
 * 验证码类型异常
 *
 * @author GuanMingJian
 * @since 2020/10/4
 */
public class ValidateCodeException extends Exception {

    private static final long serialVersionUID = 7514854456967620043L;

    public ValidateCodeException(String message) {
        super(message);
    }
}
