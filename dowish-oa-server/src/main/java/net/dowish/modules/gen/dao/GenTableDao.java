package net.dowish.modules.gen.dao;

import net.dowish.modules.gen.entity.GenTable;
import net.dowish.modules.gen.entity.GenTableColumn;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 代码生成器
 */
@Mapper
public interface GenTableDao {
	
	List<GenTable> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);

	GenTable queryTable(String tableName);
	
	List<GenTableColumn> queryColumns(String tableName);

	List<String> findTablePK(String tableName);
}
