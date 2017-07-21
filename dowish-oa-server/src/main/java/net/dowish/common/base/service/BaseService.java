package net.dowish.common.base.service;

import net.dowish.modules.sys.entity.UserEntity;
import org.apache.shiro.SecurityUtils;

/**
 * Created by yangqihua on 2017/7/19.
 */
public abstract class BaseService {

	protected static UserEntity getUser() {
		UserEntity userEntity = (UserEntity) SecurityUtils.getSubject().getPrincipal();
		return userEntity;
	}

	/**
	 * 数据范围过滤，过滤自己角色拥有的部门or自己创建的数据
	 * 这里是手动设置参数user，tableName，所以无sql注入的风险
	 * userAlias和xml中的查询的别名一致
	 */
	public static String dataScopeFilter(String tableName,String userAlias) {
		UserEntity user = getUser();
		// 超级管理员，跳过权限过滤
		if (!user.isAdmin()) {
		StringBuilder sqlString = new StringBuilder();
			sqlString.append(" and (select dept_id from sys_user where user_id = ")
					 .append(tableName)
					 .append(".create_user_id)")
					 .append(" in (select dept_id from sys_role_dept where role_id ")
					 .append(" in (select role_id from sys_user_role where user_id = ")
					 .append(user.getUserId().toString())
					 .append("))")
					 .append(" or ")
					 .append(userAlias)
					 .append(".create_user_id = ")
					 .append(user.getUserId().toString());
			return sqlString.toString();
		}
		return "";

	}
}
