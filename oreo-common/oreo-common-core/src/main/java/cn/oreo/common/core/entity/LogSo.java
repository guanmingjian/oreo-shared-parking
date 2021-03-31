package cn.oreo.common.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;

/**
 * 远程调用保存操作日志时传输的封装类
 *
 * @author GuanMingJian
 * @since 2020/10/24
 */
@Data
@AllArgsConstructor
public class LogSo {

    ProceedingJoinPoint point;

    Method method;

    String ip;

    String operation;

    String username;

    long start;
}
