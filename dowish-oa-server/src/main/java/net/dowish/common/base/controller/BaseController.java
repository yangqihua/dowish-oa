package net.dowish.common.base.controller;

import net.dowish.modules.sys.entity.UserEntity;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller公共组件
 */
public abstract class BaseController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected UserEntity getUser() {
		UserEntity userEntity = (UserEntity) SecurityUtils.getSubject().getPrincipal();
		return userEntity;
	}

	protected Long getUserId() {
		return getUser().getUserId();
	}
	protected Long getDeptId() {
		return getUser().getDeptId();
	}
}
