package net.dowish.modules.sys.controller;

import net.dowish.common.annotation.SysLog;
import net.dowish.common.base.controller.BaseController;
import net.dowish.common.utils.Apis;
import net.dowish.modules.sys.entity.RoleEntity;
import net.dowish.modules.sys.service.RoleDeptService;
import net.dowish.modules.sys.service.RoleMenuService;
import net.dowish.modules.sys.service.RoleService;
import net.dowish.common.utils.Constant;
import net.dowish.common.utils.Page;
import net.dowish.common.utils.Query;
import net.dowish.common.security.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 角色管理
 */
@RestController
@RequestMapping("/sys/role")
public class RoleController extends BaseController {
	@Autowired
	private RoleService roleService;
	@Autowired
	private RoleMenuService roleMenuService;
	@Autowired
	private RoleDeptService roleDeptService;
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:role:list")
	public Apis list(@RequestParam Map<String, Object> params){
		//如果不是超级管理员，则只查询自己创建的角色列表
		if(getUserId() != Constant.SUPER_ADMIN){
			params.put("createUserId", getUserId());
		}
		
		//查询列表数据
		Query query = new Query(params);
		List<RoleEntity> roleList = roleService.queryList(query);

//		roleList.forEach(role->{
//			List<Long> menuIdList = roleMenuService.queryMenuIdList(role.getRoleId());
//			role.setMenuIdList(menuIdList);
//		});

		int total = roleService.queryTotal(query);
		
		Page pageUtil = new Page(roleList, total, query.getLimit(), query.getPage());
		
		return Apis.ok().put("page", pageUtil);
	}
	
	/**
	 * 角色列表
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:role:select")
	public Apis select(){
		Map<String, Object> map = new HashMap<>();
		
		//如果不是超级管理员，则只查询自己所拥有的角色列表
		if(getUserId() != Constant.SUPER_ADMIN){
			map.put("createUserId", getUserId());
		}
		List<RoleEntity> list = roleService.queryList(map);
		
		return Apis.ok().put("list", list);
	}
	
	/**
	 * 角色信息
	 */
	@RequestMapping("/info/{roleId}")
	@RequiresPermissions("sys:role:info")
	public Apis info(@PathVariable("roleId") Long roleId){
		RoleEntity role = roleService.queryObject(roleId);

		//查询角色对应的菜单
		List<Long> menuIdList = roleMenuService.queryMenuIdList(roleId);
		role.setMenuIdList(menuIdList);

		//查询角色对应的部门
		List<Long> deptIdList = roleDeptService.queryDeptIdList(roleId);
		role.setDeptIdList(deptIdList);
		
		return Apis.ok().put("role", role);
	}
	
	/**
	 * 保存角色
	 */
	@SysLog("保存角色")
	@RequestMapping("/save")
	@RequiresPermissions("sys:role:save")
	public Apis save(@RequestBody RoleEntity role){
		ValidatorUtils.validateEntity(role);
		
		role.setCreateUserId(getUserId());
		roleService.save(role);
		
		return Apis.ok();
	}
	
	/**
	 * 修改角色
	 */
	@SysLog("修改角色")
	@RequestMapping("/update")
	@RequiresPermissions("sys:role:update")
	public Apis update(@RequestBody RoleEntity role){
		ValidatorUtils.validateEntity(role);
		
		role.setCreateUserId(getUserId());
		roleService.update(role);
		
		return Apis.ok();
	}
	
	/**
	 * 删除角色
	 */
	@SysLog("删除角色")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:role:delete")
	public Apis delete(@RequestBody Long[] roleIds){
		roleService.deleteBatch(roleIds);
		
		return Apis.ok();
	}
}
