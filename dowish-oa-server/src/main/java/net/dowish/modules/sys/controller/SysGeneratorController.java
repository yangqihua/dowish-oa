package net.dowish.modules.sys.controller;

import net.dowish.common.utils.Apis;
import net.dowish.common.utils.PageUtils;
import net.dowish.common.utils.Query;
import net.dowish.common.xss.XssHttpServletRequestWrapper;
import net.dowish.modules.sys.service.SysGeneratorService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 * 
 */
@Controller
@RequestMapping("/sys/generator")
public class SysGeneratorController {
	@Autowired
	private SysGeneratorService sysGeneratorService;
	
	/**
	 * 列表
	 */
	@ResponseBody
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
	@PostMapping("/code")
	@RequiresPermissions("sys:generator:code")
	public Apis code(HttpServletRequest request) throws IOException{
		String[] tableNames = new String[]{};
		//获取表名，不进行xss过滤
		HttpServletRequest orgRequest = XssHttpServletRequestWrapper.getOrgRequest(request);
		String tables = orgRequest.getParameter("tables");
//		tableNames = JSON.parseArray(tables).toArray(tableNames);
		
//		byte[] data = sysGeneratorService.generatorCode(tableNames);
//
//		response.reset();
//        response.setHeader("Content-Disposition", "attachment; filename=\"dowish.zip\"");
//        response.addHeader("Content-Length", "" + data.length);
//        response.setContentType("application/octet-stream; charset=UTF-8");
//
//        IOUtils.write(data, response.getOutputStream());
		return Apis.ok();
	}
}
