package net.dowish.common.exception;

import net.dowish.common.utils.Apis;
import org.apache.shiro.authz.AuthorizationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 * 
 *
 * @date 2016年10月27日 下午10:16:19
 */
@RestControllerAdvice
public class ResultExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 自定义异常
	 */
	@ExceptionHandler(ResultException.class)
	public Apis handleRRException(ResultException e){
		Apis apis = new Apis();
		apis.put("code", e.getCode());
		apis.put("msg", e.getMessage());

		return apis;
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public Apis handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return Apis.error("数据库中已存在该记录");
	}

	@ExceptionHandler(AuthorizationException.class)
	public Apis handleAuthorizationException(AuthorizationException e){
		logger.error(e.getMessage(), e);
		return Apis.error("没有权限，请联系管理员授权");
	}

	@ExceptionHandler(Exception.class)
	public Apis handleException(Exception e){
		logger.error(e.getMessage(), e);
		return Apis.error();
	}
}
