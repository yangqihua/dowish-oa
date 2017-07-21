package net.dowish.modules.sys.controller;

import net.dowish.common.annotation.SysLog;
import net.dowish.common.base.controller.BaseController;
import net.dowish.common.utils.Apis;
import net.dowish.common.utils.Constant;
import net.dowish.common.utils.Page;
import net.dowish.common.utils.Query;
import net.dowish.common.security.validator.Assert;
import net.dowish.common.security.validator.ValidatorUtils;
import net.dowish.common.security.validator.group.AddGroup;
import net.dowish.common.security.validator.group.UpdateGroup;
import net.dowish.modules.sys.entity.UserEntity;
import net.dowish.modules.sys.service.UserRoleService;
import net.dowish.modules.sys.service.UserService;
import org.apache.commons.lang.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统用户
 */
@RestController
@RequestMapping("/sys/user")
public class UserController extends BaseController {
	@Autowired
	private UserService userService;
	@Autowired
	private UserRoleService userRoleService;
	
	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:user:list")
	public Apis list(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
		if(getUserId() != Constant.SUPER_ADMIN){
			params.put("createUserId", getUserId());
		}
		
		//查询列表数据
		Query query = new Query(params);
		List<UserEntity> userList = userService.queryList(query);
		int total = userService.queryTotal(query);
		
		Page pageUtil = new Page(userList, total, query.getLimit(), query.getPage());
		
		return Apis.ok().put("page", pageUtil);
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping("/info")
	public Apis info(){
		return Apis.ok().put("user", getUser());
	}
	
	/**
	 * 修改登录用户密码
	 */
	@SysLog("修改密码")
	@RequestMapping("/password")
//	@RequiresPermissions("sys:user:password")  // 为了让普通用户可以修改密码，故注释掉后台权限验证
	public Apis password(@RequestBody Map<String,Object> map ){
		String password= (String) map.get("password");
		String newPassword = (String) map.get("newPassword");
		Assert.isBlank(newPassword, "新密码不为能空");
		UserEntity userEntity = getUser();
		if(map.get("userId")!=null){
			// TODO : map不能直接转Long?
			Integer userId = (Integer) map.get("userId");
			userEntity = userService.queryObject(userId.longValue());
		}
		
		//sha256加密
		password = new Sha256Hash(password, userEntity.getSalt()).toHex();
		//sha256加密
		newPassword = new Sha256Hash(newPassword, userEntity.getSalt()).toHex();
				
		//更新密码
		int count = userService.updatePassword(userEntity.getUserId(), password, newPassword);
		if(count == 0){
			return Apis.error("原密码不正确");
		}
		
		return Apis.ok();
	}
	
	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public Apis info(@PathVariable("userId") Long userId){
		UserEntity user = userService.queryObject(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = userRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		
		return Apis.ok().put("user", user);
	}
	
	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	public Apis save(@RequestBody UserEntity user, @RequestParam String password){
		user.setPassword(password);
		ValidatorUtils.validateEntity(user, AddGroup.class);
		
		user.setCreateUserId(getUserId());
		userService.save(user);
		
		return Apis.ok();
	}
	
	/**
	 * 修改用户
	 */
	@SysLog("修改用户")
	@RequestMapping("/update")
	@RequiresPermissions("sys:user:update")
	public Apis update(@RequestBody UserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);
		
		user.setCreateUserId(getUserId());
		userService.update(user);
		
		return Apis.ok();
	}
	
	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public Apis delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return Apis.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return Apis.error("当前用户不能删除");
		}
		
		userService.deleteBatch(userIds);
		
		return Apis.ok();
	}
}
