package net.dowish.modules.sys.service.impl;

import com.qiniu.util.StringUtils;
import net.dowish.modules.sys.dao.DeptDao;
import net.dowish.modules.sys.entity.DeptEntity;
import net.dowish.modules.sys.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



@Service("sysDeptService")
public class DeptServiceImpl implements DeptService {
	@Autowired
	private DeptDao deptDao;
	
	@Override
	public DeptEntity queryObject(Long deptId){
		return deptDao.queryObject(deptId);
	}
	
	@Override
	public List<DeptEntity> queryList(Map<String, Object> map){
		return deptDao.queryList(map);
	}
	
	@Override
	public void save(DeptEntity sysDept){
		deptDao.save(sysDept);
	}
	
	@Override
	public void update(DeptEntity sysDept){
		deptDao.update(sysDept);
	}
	
	@Override
	public void delete(Long deptId){
		deptDao.delete(deptId);
	}

	@Override
	public List<Long> queryDetpIdList(Long parentId) {
		return deptDao.queryDetpIdList(parentId);
	}

	@Override
	public String getSubDeptIdList(Long deptId){
		//部门及子部门ID列表
		List<Long> deptIdList = new ArrayList<>();

		//获取子部门ID
		List<Long> subIdList = queryDetpIdList(deptId);
		getDeptTreeList(subIdList, deptIdList);

		//添加本部门
		deptIdList.add(deptId);

		String deptFilter = StringUtils.join(deptIdList, ",");
		return deptFilter;
	}

	/**
	 * 递归
	 */
	public void getDeptTreeList(List<Long> subIdList, List<Long> deptIdList){
		for(Long deptId : subIdList){
			List<Long> list = queryDetpIdList(deptId);
			if(list.size() > 0){
				getDeptTreeList(list, deptIdList);
			}

			deptIdList.add(deptId);
		}
	}

}
