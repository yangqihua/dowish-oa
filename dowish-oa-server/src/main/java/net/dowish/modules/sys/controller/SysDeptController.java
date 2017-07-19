package net.dowish.modules.sys.controller;

import net.dowish.common.base.controller.BaseController;
import net.dowish.common.utils.Apis;
import net.dowish.common.utils.Constant;
import net.dowish.modules.sys.entity.SysDeptEntity;
import net.dowish.modules.sys.service.SysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 部门管理
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-06-20 15:23:47
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends BaseController {
	@Autowired
	private SysDeptService sysDeptService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:dept:list")
	public Apis list(){
		Map<String, Object> map = new HashMap<>();
		//如果不是超级管理员，则只能查询本部门及子部门数据
		if(getUserId() != Constant.SUPER_ADMIN){
			map.put("deptFilter", sysDeptService.getSubDeptIdList(getDeptId()));
		}
		List<SysDeptEntity> deptList = sysDeptService.queryList(map);

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
			map.put("deptFilter", sysDeptService.getSubDeptIdList(getDeptId()));
		}
		List<SysDeptEntity> deptList = sysDeptService.queryList(map);

		//添加一级部门
		if(getUserId() == Constant.SUPER_ADMIN){
			SysDeptEntity root = new SysDeptEntity();
			root.setDeptId(0L);
			root.setName("一级部门");
			root.setParentId(-1L);
			root.setOpen(true);
			deptList.add(root);
		}

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
			SysDeptEntity dept = sysDeptService.queryObject(getDeptId());
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
		SysDeptEntity dept = sysDeptService.queryObject(deptId);
		
		return Apis.ok().put("dept", dept);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
//	@RequiresPermissions("sys:dept:save")
	public Apis save(@RequestBody SysDeptEntity dept){
		sysDeptService.save(dept);
		
		return Apis.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
//	@RequiresPermissions("sys:dept:update")
	public Apis update(@RequestBody SysDeptEntity dept){
		sysDeptService.update(dept);
		
		return Apis.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
//	@RequiresPermissions("sys:dept:delete")
	public Apis delete(@RequestParam long deptId){
		//判断是否有子部门
		List<Long> deptList = sysDeptService.queryDetpIdList(deptId);
		if(deptList.size() > 0){
			return Apis.error("请先删除子部门");
		}

		sysDeptService.delete(deptId);
		
		return Apis.ok();
	}
	
}
