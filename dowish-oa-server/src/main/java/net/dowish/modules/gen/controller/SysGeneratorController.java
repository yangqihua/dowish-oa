package net.dowish.modules.gen.controller;

import net.dowish.common.utils.Apis;
import net.dowish.common.utils.PageUtils;
import net.dowish.common.utils.Query;
import net.dowish.common.xss.XssHttpServletRequestWrapper;
import net.dowish.modules.gen.service.SysGeneratorService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 * 
 */
@RestController
@RequestMapping("/sys/generator")
public class SysGeneratorController {
	@Autowired
	private SysGeneratorService sysGeneratorService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:generator:list")
	public Apis list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<Map<String, Object>> list = sysGeneratorService.queryList(query);
		int total = sysGeneratorService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(list, total, query.getLimit(), query.getPage());
		
		return Apis.ok().put("page", pageUtil);
	}
	
	/**
	 * 生成代码
	 */
	@RequestMapping("/code")
	@RequiresPermissions("sys:generator:code")
	public Apis code(HttpServletRequest request, HttpServletResponse response) throws IOException{
//		String[] tableNames = new String[]{};
		//获取表名，不进行xss过滤
		HttpServletRequest orgRequest = XssHttpServletRequestWrapper.getOrgRequest(request);
		String table = orgRequest.getParameter("table");

		boolean isReplaceFile = true;
		String message = sysGeneratorService.generatorCode(table,isReplaceFile);
		return Apis.ok(message);
	}
}
