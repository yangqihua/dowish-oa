/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package net.dowish.modules.gen.service;

import java.util.List;
import java.util.Map;

import net.dowish.common.utils.StringUtils;
import net.dowish.modules.gen.dao.GenTableDao;
import net.dowish.modules.gen.entity.GenTable;
import net.dowish.modules.gen.entity.GenTableColumn;
import net.dowish.modules.gen.utils.GenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 业务表Service
 */
@Service
@Transactional(readOnly = true)
public class GenTableService {

	@Autowired
	private GenTableDao genTableDao;

	public List<GenTable> queryList(Map<String, Object> map) {
		return genTableDao.queryList(map);
	}

	public int queryTotal(Map<String, Object> map) {
		return genTableDao.queryTotal(map);
	}

	public GenTable queryTable(String tableName) {
		return genTableDao.queryTable(tableName);
	}

	public String generatorCode(GenTable genTable) {
		//生成代码
		return GenUtils.generatorCode(genTable);
	}


	public GenTable getTableMapper(String tableName) {
		GenTable genTable = genTableDao.queryTable(tableName);

		List<GenTableColumn> columnList = genTableDao.queryColumns(tableName);

		genTable.setColumnList(columnList);

		// 设置描述
		if (StringUtils.isBlank(genTable.getComments())){
			genTable.setComments(genTable.getTableName());
		}
		// 设置类名
		genTable.setClassName(StringUtils.toCapitalizeCamelCase(genTable.getTableName()));

		// 设置包名
		genTable.setPackageName("net.dowish.modules");

		// 设置作者
		genTable.setFunctionAuthor("yangqihua");

		// 设置模块名英文
		genTable.setModuleName(genTable.getTableName());

		// 设置生成类型
		genTable.setCategory("curd");

		// 设置主键
		genTable.setPkList(genTableDao.findTablePK(genTable.getTableName()));

		// 初始化列属性字段
		GenUtils.initColumnField(genTable);

		return genTable;

	}





}
