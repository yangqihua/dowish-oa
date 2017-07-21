package net.dowish.modules.sys.service.impl;

import net.dowish.modules.sys.dao.RoleDao;
import net.dowish.modules.sys.entity.RoleEntity;
import net.dowish.modules.sys.service.*;
import net.dowish.common.utils.Constant;
import net.dowish.common.exception.ResultException;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



/**
 * 角色
 */
@Service("sysRoleService")
public class RoleServiceImpl implements RoleService {
	@Autowired
	private RoleDao roleDao;
	@Autowired
	private RoleMenuService roleMenuService;
	@Autowired
	private RoleDeptService roleDeptService;
	@Autowired
	private UserService userService;

	@Override
	public RoleEntity queryObject(Long roleId) {
		return roleDao.queryObject(roleId);
	}

	@Override
	public List<RoleEntity> queryList(Map<String, Object> map) {
		return roleDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return roleDao.queryTotal(map);
	}

	@Override
	@Transactional
	public void save(RoleEntity role) {
		role.setCreateTime(new Date());
		roleDao.save(role);
		
		//检查权限是否越权
		checkPrems(role);
		
		//保存角色与菜单关系
		roleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());
		roleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
	}

	@Override
	@Transactional
	public void update(RoleEntity role) {
		roleDao.update(role);
		
		//检查权限是否越权
		checkPrems(role);
		
		//更新角色与菜单关系
		roleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

		//更新角色与部门关系
		roleDeptService.saveOrUpdate(role.getRoleId(), role.getDeptIdList());
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] roleIds) {
		roleDao.deleteBatch(roleIds);
	}
	
	@Override
	public List<Long> queryRoleIdList(Long createUserId) {
		return roleDao.queryRoleIdList(createUserId);
	}

	/**
	 * 检查权限是否越权
	 */
	private void checkPrems(RoleEntity role){
		//如果不是超级管理员，则需要判断角色的权限是否超过自己的权限
		if(role.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}
		
		//查询用户所拥有的菜单列表
		List<Long> menuIdList = userService.queryAllMenuId(role.getCreateUserId());
		
		//判断是否越权
		if(!menuIdList.containsAll(role.getMenuIdList())){
			throw new ResultException("新增角色的权限，已超出你的权限范围");
		}
	}
}
