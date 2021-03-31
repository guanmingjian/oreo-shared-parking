package cn.oreo.common.util.exception;

import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.constant.OreoConstant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;

/**
 * 功能：全局异常处理
 * @author GuanMingJian
 * @since 2021/2/4
 */
@Order
@ControllerAdvice
public class BaseExceptionHandler {

    private Log logger = LogFactory.getLog(BaseExceptionHandler.class);
    public static String errorMessage = OreoConstant.MESSAGE_PARAMETER_EXCEPTION;

    /**
     * Exception异常捕获
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Object seriousHandler(Exception e) {
        logger.error("异常信息："+e.getMessage(), e);
        logger.error("异常类型:"+e.getClass());
        if(e instanceof DuplicateKeyException){
            // 违法唯一主键
            return new OreoResponse().code("500").message(OreoConstant.MESSAGE_MYSQL_UNIKEY_EXCEPTION);
        }
        return new OreoResponse().code("500").message("[自定义异常]异常类型："+e.getClass()+";异常信息："+e.getMessage());
    }

    /**
     * 自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(PentiumException.class)
    @ResponseBody
    public Object pentiumExceptionHandler(PentiumException e){
        e.printStackTrace();
        return new OreoResponse().code(String.valueOf(e.getCode())).message(e.getMessage());
    }

    /**
     * 方法参数注解异常
     * eg:public OreoResponse userList(@NotNull @PathVariable("id") Long id)
     * @param e
     * @return
     */
    @ResponseBody
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Object badArgumentHandler(MethodArgumentTypeMismatchException e) {
        logger.error(e.getMessage(), e);
        MethodArgumentTypeMismatchException methodArgumentTypeMismatchException = e;
        // 参数名
        String name = methodArgumentTypeMismatchException.getName();
        // 函数名
        MethodParameter parameter = methodArgumentTypeMismatchException.getParameter();

        return new OreoResponse().code("402").message(errorMessage+"参数名:"+name+",目标函数："+parameter);
    }

    /**
     * 抛出@Valid异常
     * @return
     */
    @ResponseBody
    @ExceptionHandler(BindException.class)
    public Object badArgumentHandler(BindException e) {

        if (e instanceof BindException) {
            BindException bindException = e;
            BindingResult bindingResult = bindException.getBindingResult();
            List<ObjectError> allErrors = bindingResult.getAllErrors();
            allErrors.forEach(objectError -> {
                FieldError fieldError = (FieldError)objectError;
                errorMessage = errorMessage+"函数名："+fieldError.getObjectName()+",参数名："+fieldError.getField()+",异常信息："+fieldError.getDefaultMessage()+";";
                logger.error("Data check failure : object{"+fieldError.getObjectName()+"},field{"+fieldError.getField()+
                        "},errorMessage{"+fieldError.getDefaultMessage()+"}");
            });
        }
        return new OreoResponse().code("402").message(errorMessage);
    }

    @ResponseBody
    @ExceptionHandler(IllegalArgumentException.class)
    public Object badArgumentHandler(IllegalArgumentException e) {
        logger.error(e.getMessage(), e);
        return new OreoResponse().code("402").message("1:"+errorMessage);
    }

    @ResponseBody
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Object badArgumentHandler(MissingServletRequestParameterException e) {
        logger.error(e.getMessage(), e);
        return new OreoResponse().code("402").message("3:"+errorMessage);
    }

    @ResponseBody
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Object badArgumentHandler(HttpMessageNotReadableException e) {
        logger.error(e.getMessage(), e);
        return new OreoResponse().code("402").message("4:"+errorMessage);
    }

    @ResponseBody
    @ExceptionHandler(ValidationException.class)
    public Object badArgumentHandler(ValidationException e) {
        logger.error(e.getMessage(), e);
        if (e instanceof ConstraintViolationException) {
            ConstraintViolationException exs = (ConstraintViolationException) e;
            Set<ConstraintViolation<?>> violations = exs.getConstraintViolations();
            for (ConstraintViolation<?> item : violations) {
                String message = ((PathImpl) item.getPropertyPath()).getLeafNode().getName() + item.getMessage();
                return new OreoResponse().code("402").message(message);
            }
        }
        return new OreoResponse().code("402").message("5:"+errorMessage);
    }
}