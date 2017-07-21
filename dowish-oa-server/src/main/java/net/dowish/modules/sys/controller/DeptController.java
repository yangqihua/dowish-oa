package net.dowish.modules.sys.controller;

import net.dowish.common.base.controller.BaseController;
import net.dowish.common.utils.Apis;
import net.dowish.common.utils.Constant;
import net.dowish.modules.sys.entity.DeptEntity;
import net.dowish.modules.sys.service.DeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 部门管理
 */
@RestController
@RequestMapping("/sys/dept")
public class DeptController extends BaseController {
	@Autowired
	private DeptService deptService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:dept:list")
	public Apis list(){
		Map<String, Object> map = new HashMap<>();
		//如果不是超级管理员，则只能查询本部门及子部门数据
		if(getUserId() != Constant.SUPER_ADMIN){
			map.put("deptFilter", deptService.getSubDeptIdList(getDeptId()));
		}
		List<DeptEntity> deptList = deptService.queryList(map);

		return Apis.ok().put("deptList",deptList);
	}

	/**
	 * 选择部门(添加、修改菜单)
	 */
	@RequestMapping("/select")
//	@RequiresPermissions("sys:dept:select")
	public Apis select(){
		Map<String, Object> map = new HashMap<>();
		//如果不是超级管理员，则只能查询本部门及子部门数据
		if(getUserId() != Constant.SUPER_ADMIN){
			map.put("deptFilter", deptService.getSubDeptIdList(getDeptId()));
		}
		List<DeptEntity> deptList = deptService.queryList(map);

		//添加一级部门
//		if(getUserId() == Constant.SUPER_ADMIN){
//			DeptEntity root = new DeptEntity();
//			root.setDeptId(0L);
//			root.setName("一级部门");
//			root.setParentId(-1L);
//			root.setOpen(true);
//			deptList.add(root);
//		}

		return Apis.ok().put("deptList", deptList);
	}

	/**
	 * 上级部门Id(管理员则为0)
	 */
	@RequestMapping("/info")
	@RequiresPermissions("sys:dept:list")
	public Apis info(){
		long deptId = 0;
		if(getUserId() != Constant.SUPER_ADMIN){
			DeptEntity dept = deptService.queryObject(getDeptId());
			deptId = dept.getParentId();
		}

		return Apis.ok().put("deptId", deptId);
	}
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{deptId}")
	@RequiresPermissions("sys:dept:info")
	public Apis info(@PathVariable("deptId") Long deptId){
		DeptEntity dept = deptService.queryObject(deptId);
		
		return Apis.ok().put("dept", dept);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("sys:dept:save")
	public Apis save(@RequestBody DeptEntity dept){
		deptService.save(dept);
		
		return Apis.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("sys:dept:update")
	public Apis update(@RequestBody DeptEntity dept){
		deptService.update(dept);
		
		return Apis.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("sys:dept:delete")
	public Apis delete(@RequestParam long deptId){
		//判断是否有子部门
		List<Long> deptList = deptService.queryDetpIdList(deptId);
		if(deptList.size() > 0){
			return Apis.error("请先删除子部门");
		}

		deptService.delete(deptId);
		
		return Apis.ok();
	}
	
}
