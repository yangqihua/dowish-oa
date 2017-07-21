package net.dowish.modules.sys.controller;

import net.dowish.common.annotation.SysLog;
import net.dowish.common.base.controller.BaseController;
import net.dowish.common.utils.Apis;
import net.dowish.modules.sys.entity.Dict;
import net.dowish.modules.sys.service.SysConfigService;
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
@RequestMapping("/sys/config")
public class SysConfigController extends BaseController {
	@Autowired
	private SysConfigService sysConfigService;
	
	/**
	 * 所有配置列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:config:list")
	public Apis list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<Dict> configList = sysConfigService.queryList(query);
		int total = sysConfigService.queryTotal(query);
		
		Page pageUtil = new Page(configList, total, query.getLimit(), query.getPage());
		
		return Apis.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 配置信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("sys:config:info")
	public Apis info(@PathVariable("id") Long id){
		Dict config = sysConfigService.queryObject(id);
		
		return Apis.ok().put("config", config);
	}
	
	/**
	 * 保存配置
	 */
	@SysLog("保存配置")
	@RequestMapping("/save")
	@RequiresPermissions("sys:config:save")
	public Apis save(@RequestBody Dict config){
		ValidatorUtils.validateEntity(config);

		sysConfigService.save(config);
		
		return Apis.ok();
	}
	
	/**
	 * 修改配置
	 */
	@SysLog("修改配置")
	@RequestMapping("/update")
	@RequiresPermissions("sys:config:update")
	public Apis update(@RequestBody Dict config){
		ValidatorUtils.validateEntity(config);
		
		sysConfigService.update(config);
		
		return Apis.ok();
	}
	
	/**
	 * 删除配置
	 */
	@SysLog("删除配置")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:config:delete")
	public Apis delete(@RequestBody Long[] ids){
		sysConfigService.deleteBatch(ids);
		
		return Apis.ok();
	}

}
