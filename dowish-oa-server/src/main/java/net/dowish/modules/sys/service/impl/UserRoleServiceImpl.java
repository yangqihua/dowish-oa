package net.dowish.modules.sys.service.impl;

import net.dowish.modules.sys.dao.UserRoleDao;
import net.dowish.modules.sys.service.UserRoleService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * 用户与角色对应关系
 */
@Service("sysUserRoleService")
public class UserRoleServiceImpl implements UserRoleService {
	@Autowired
	private UserRoleDao userRoleDao;

	@Override
	public void saveOrUpdate(Long userId, List<Long> roleIdList) {
		if(roleIdList.size() == 0){
			return ;
		}
		
		//先删除用户与角色关系
		userRoleDao.delete(userId);
		
		//保存用户与角色关系
		Map<String, Object> map = new HashMap<>();
		map.put("userId", userId);
		map.put("roleIdList", roleIdList);
		userRoleDao.save(map);
	}

	@Override
	public List<Long> queryRoleIdList(Long userId) {
		return userRoleDao.queryRoleIdList(userId);
	}

	@Override
	public void delete(Long userId) {
		userRoleDao.delete(userId);
	}
}
