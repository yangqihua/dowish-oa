package net.dowish.modules.sys.controller;

import net.dowish.modules.sys.entity.LogEntity;
import net.dowish.modules.sys.service.LogService;
import net.dowish.common.utils.Page;
import net.dowish.common.utils.Query;
import net.dowish.common.utils.Apis;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;


/**
 * 系统日志
 */
@Controller
@RequestMapping("/sys/log")
public class LogController {
	@Autowired
	private LogService logService;
	
	/**
	 * 列表
	 */
	@ResponseBody
	@RequestMapping("/list")
	@RequiresPermissions("sys:log:list")
	public Apis list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<LogEntity> sysLogList = logService.queryList(query);
		int total = logService.queryTotal(query);
		
		Page pageUtil = new Page(sysLogList, total, query.getLimit(), query.getPage());
		return Apis.ok().put("page", pageUtil);
	}
	
}
