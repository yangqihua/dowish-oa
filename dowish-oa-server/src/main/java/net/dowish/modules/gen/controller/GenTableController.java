/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package net.dowish.modules.gen.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import net.dowish.common.utils.Apis;
import net.dowish.common.utils.PageUtils;
import net.dowish.common.utils.Query;
import net.dowish.common.xss.XssHttpServletRequestWrapper;
import net.dowish.modules.gen.entity.GenConfig;
import net.dowish.modules.gen.entity.GenTable;
import net.dowish.modules.gen.service.GenTableService;
import net.dowish.modules.gen.utils.GenUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 业务表Controller
 *
 */
@Slf4j
@RestController
@RequestMapping("/sys/generator")
public class GenTableController{

	@Autowired
	private GenTableService genTableService;


	@RequestMapping(value = "/mapper")
//	@RequiresPermissions("gen:primary:code")
	public Apis mapper(@RequestParam String tableName, Model model) {
		GenTable genTable = genTableService.getTableMapper(tableName);
		GenConfig config = GenUtils.getConfig();
		return Apis.ok().put("genTable", genTable)
				   .put("genConfig",config );
	}


	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("gen:primary:list")
	public Apis list(@RequestParam Map<String, Object> params){
		//查询列表数据
		Query query = new Query(params);
		List<GenTable> genTables = genTableService.queryList(query);
		int total = genTableService.queryTotal(query);

		PageUtils pageUtil = new PageUtils(genTables, total, query.getLimit(), query.getPage());

		return Apis.ok().put("page", pageUtil);
	}

	/**
	 * 生成代码
	 */
	@RequestMapping("/code")
//	@RequiresPermissions("gen:primary:code")
	public Apis code(@RequestBody GenTable genTable, HttpServletRequest request) throws IOException {
		log.info("genTable : {}",genTable);
		//获取表名，不进行xss过滤
		HttpServletRequest orgRequest = XssHttpServletRequestWrapper.getOrgRequest(request);
		String table = orgRequest.getParameter("table");

		String message = genTableService.generatorCode(genTable);
		return Apis.ok(message);
	}

}
