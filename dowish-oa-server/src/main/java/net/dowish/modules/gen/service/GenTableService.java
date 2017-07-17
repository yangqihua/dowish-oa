/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package net.dowish.modules.gen.service;

import java.util.List;
import java.util.Map;

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

	public String generatorCode(GenTable genTable,boolean isReplaceFile) {
		//查询表信息
//		GenTable table = queryTable(tableName);
		//查询列信息
//		List<GenTableColumn> columns = queryColumns(genTable);
		//生成代码
//		String mesg = GenUtils.generatorCode(genTable,isReplaceFile);
		String mesg = "ok";
		return mesg;
	}


	public GenTable getTableMapper(String tableName) {
		GenTable genTable = genTableDao.queryTable(tableName);

		List<GenTableColumn> columnList = genTableDao.queryColumns(tableName);

		genTable.setColumnList(columnList);
//		for (GenTableColumn column : columnList){
//			boolean b = false;
//			for (GenTableColumn e : genTable.getColumnList()){
//				if (e.getColumnName().equals(column.getColumnName())){
//					b = true;
//				}
//			}
//			if (!b){
//				genTable.getColumnList().add(column);
//			}
//		}

		// 获取主键
		genTable.setPkList(genTableDao.findTablePK(genTable.getTableName()));

		// 初始化列属性字段
		GenUtils.initColumnField(genTable);

		return genTable;

	}





}
