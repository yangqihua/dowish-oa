package net.dowish.modules.sys.service;

import net.dowish.modules.sys.entity.DeptEntity;

import java.util.List;
import java.util.Map;

/**
 * 部门管理
 */
public interface DeptService {
	
	DeptEntity queryObject(Long deptId);
	
	List<DeptEntity> queryList(Map<String, Object> map);

	void save(DeptEntity sysDept);
	
	void update(DeptEntity sysDept);
	
	void delete(Long deptId);

	/**
	 * 查询子部门ID列表
	 * @param parentId  上级部门ID
	 */
	List<Long> queryDetpIdList(Long parentId);

	/**
	 * 获取子部门ID(包含本部门ID)，用于数据过滤
	 */
	String getSubDeptIdList(Long deptId);

}
