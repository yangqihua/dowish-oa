package net.dowish.modules.sys.controller;

import net.dowish.common.annotation.SysLog;
import net.dowish.common.base.controller.BaseController;
import net.dowish.common.utils.Apis;
import net.dowish.modules.sys.entity.Dict;
import net.dowish.modules.sys.service.DictService;
import net.dowish.common.utils.Page;
import net.dowish.common.utils.Query;
import net.dowish.common.security.validator.ValidatorUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 系统配置信息
 */
@RestController
@RequestMapping("/sys/dict")
public class DictController extends BaseController {
	@Autowired
	private DictService dictService;
	
	/**
	 * 所有配置列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:dict:list")
	public Apis list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<Dict> configList = dictService.queryList(query);
		int total = dictService.queryTotal(query);
		
		Page pageUtil = new Page(configList, total, query.getLimit(), query.getPage());
		
		return Apis.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 配置信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:dict:info")
	public Apis info(@PathVariable("id") Long id){
		Dict config = dictService.queryObject(id);
		
		return Apis.ok().put("config", config);
	}
	
	/**
	 * 保存配置
	 */
	@SysLog("保存配置")
	@RequestMapping("/save")
	@RequiresPermissions("sys:dict:save")
	public Apis save(@RequestBody Dict config){
		ValidatorUtils.validateEntity(config);

		dictService.save(config);
		
		return Apis.ok();
	}
	
	/**
	 * 修改配置
	 */
	@SysLog("修改配置")
	@RequestMapping("/update")
	@RequiresPermissions("sys:dict:update")
	public Apis update(@RequestBody Dict config){
		ValidatorUtils.validateEntity(config);
		
		dictService.update(config);
		
		return Apis.ok();
	}
	
	/**
	 * 删除配置
	 */
	@SysLog("删除配置")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:dict:delete")
	public Apis delete(@RequestBody Long[] ids){
		dictService.deleteBatch(ids);
		
		return Apis.ok();
	}

}
