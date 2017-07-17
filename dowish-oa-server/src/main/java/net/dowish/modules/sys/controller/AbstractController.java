package net.dowish.modules.sys.controller;

import net.dowish.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Controller公共组件
 */
public abstract class AbstractController {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	protected SysUserEntity getUser() {
		SysUserEntity userEntity = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
		return userEntity;
	}

	protected Long getUserId() {
		return getUser().getUserId();
	}
	protected Long getDeptId() {
		return getUser().getDeptId();
	}
}
