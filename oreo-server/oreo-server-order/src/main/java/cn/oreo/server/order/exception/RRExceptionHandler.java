/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package cn.oreo.server.order.exception;


import cn.oreo.common.util.common.OreoResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * 异常处理器
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestControllerAdvice
public class RRExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(RRException.class)
	public OreoResponse handleRRException(RRException e){
		OreoResponse r = new OreoResponse();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());

		return r;
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public OreoResponse handlerNoFoundException(Exception e) {
		logger.error(e.getMessage(), e);
		return new OreoResponse().code("404").message("路径不存在，请检查路径是否正确");
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public OreoResponse handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return new OreoResponse().message("数据库已经存在该记录");
	}



	@ExceptionHandler(Exception.class)
	public OreoResponse handleException(Exception e){
		logger.error(e.getMessage(), e);
		return new OreoResponse().message("错误");
	}
}
