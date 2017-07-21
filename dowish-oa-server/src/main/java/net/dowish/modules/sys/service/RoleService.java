package net.dowish.modules.sys.service;

import net.dowish.modules.sys.entity.RoleEntity;

import java.util.List;
import java.util.Map;


/**
 * 角色
 */
public interface RoleService {
	
	RoleEntity queryObject(Long roleId);
	
	List<RoleEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(RoleEntity role);
	
	void update(RoleEntity role);
	
	void deleteBatch(Long[] roleIds);
	
	/**
	 * 查询用户创建的角色ID列表
	 */
	List<Long> queryRoleIdList(Long createUserId);
}
