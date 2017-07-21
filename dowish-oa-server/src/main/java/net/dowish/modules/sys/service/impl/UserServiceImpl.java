package net.dowish.modules.sys.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.dowish.common.base.service.BaseService;
import net.dowish.common.exception.ResultException;
import net.dowish.common.utils.Constant;
import net.dowish.modules.sys.dao.UserDao;
import net.dowish.modules.sys.entity.UserEntity;
import net.dowish.modules.sys.service.RoleService;
import net.dowish.modules.sys.service.UserRoleService;
import net.dowish.modules.sys.service.UserService;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 系统用户
 */
@Service("sysUserService")
@Slf4j
public class UserServiceImpl extends BaseService implements UserService {
	@Autowired
	private UserDao userDao;
	@Autowired
	private UserRoleService userRoleService;
	@Autowired
	private RoleService roleService;

	@Override
	public List<String> queryAllPerms(Long userId) {
		return userDao.queryAllPerms(userId);
	}

	@Override
	public List<Long> queryAllMenuId(Long userId) {
		return userDao.queryAllMenuId(userId);
	}

	@Override
	public UserEntity queryByUserName(String username) {
		return userDao.queryByUserName(username);
	}
	
	@Override
	public UserEntity queryObject(Long userId) {
		return userDao.queryObject(userId);
	}

	@Override
	public List<UserEntity> queryList(Map<String, Object> map){
		map.put("sqlFilter",dataScopeFilter("sys_user","u"));
		log.info("map : {}",map);
		return userDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map) {
		return userDao.queryTotal(map);
	}

	@Override
	@Transactional
	public void save(UserEntity user) {
		user.setCreateTime(new Date());
		//sha256加密
		String salt = RandomStringUtils.randomAlphanumeric(20);
		user.setPassword(new Sha256Hash(user.getPassword(), salt).toHex());
		user.setSalt(salt);
		userDao.save(user);
		
		//检查角色是否越权
		checkRole(user);
		
		//保存用户与角色关系
		userRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional
	public void update(UserEntity user) {
		if(StringUtils.isBlank(user.getPassword())){
			user.setPassword(null);
		}else{
			user.setPassword(new Sha256Hash(user.getPassword(), user.getSalt()).toHex());
		}
		userDao.update(user);
		
		//检查角色是否越权
		checkRole(user);
		
		//保存用户与角色关系
		userRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] userId) {
		userDao.deleteBatch(userId);
	}

	@Override
	@Transactional
	public int updatePassword(Long userId, String password, String newPassword) {
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("password", password);
		map.put("newPassword", newPassword);
		return userDao.updatePassword(map);
	}
	
	/**
	 * 检查角色是否越权
	 */
	private void checkRole(UserEntity user){
		//如果不是超级管理员，则需要判断用户的角色是否自己创建
		if(user.getCreateUserId() == Constant.SUPER_ADMIN){
			return ;
		}
		
		//查询用户创建的角色列表
		List<Long> roleIdList = roleService.queryRoleIdList(user.getCreateUserId());
		
		//判断是否越权
		if(!roleIdList.containsAll(user.getRoleIdList())){
			throw new ResultException("新增用户所选角色，不是本人创建");
		}
	}
}
