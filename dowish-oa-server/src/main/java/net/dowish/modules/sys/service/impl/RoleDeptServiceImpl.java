package net.dowish.modules.sys.service.impl;

import net.dowish.modules.sys.dao.RoleDeptDao;
import net.dowish.modules.sys.service.RoleDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 角色与部门对应关系
 */
@Service("sysRoleDeptService")
public class RoleDeptServiceImpl implements RoleDeptService {
	@Autowired
	private RoleDeptDao roleDeptDao;

	@Override
	@Transactional
	public void saveOrUpdate(Long roleId, List<Long> deptIdList) {
		//先删除角色与部门关系
		roleDeptDao.delete(roleId);

		if(deptIdList.size() == 0){
			return ;
		}

		//保存角色与菜单关系
		Map<String, Object> map = new HashMap<>();
		map.put("roleId", roleId);
		map.put("deptIdList", deptIdList);
		roleDeptDao.save(map);
	}

	@Override
	public List<Long> queryDeptIdList(Long roleId) {
		return roleDeptDao.queryDeptIdList(roleId);
	}

}
