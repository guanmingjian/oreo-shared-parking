package cn.oreo.common.core.exception;

/**
 * OREO系统异常
 *
 * @author GuanMingJian
 * @since 2020/10/4
 */
public class OreoException extends Exception {

    private static final long serialVersionUID = -6916154462432027437L;

    public OreoException(String message) {
        super(message);
    }
}
