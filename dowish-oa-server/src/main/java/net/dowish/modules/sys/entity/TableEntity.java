package net.dowish.modules.sys.entity;

import lombok.Data;

import java.util.List;

/**
 * 表数据
 * 
 * @author yangqihua
 * @email 904693433@qq.com
 * @date 2017-07-05 上午12:02:55
 */

@Data
public class TableEntity {
	//表的名称
	private String tableName;
	//表的备注
	private String comments;
	//表的主键
	private ColumnEntity pk;
	//表的列名(不包含主键)
	private List<ColumnEntity> columns;
	
	//类名(第一个字母大写)，如：sys_user => SysUser
	private String className;
	//类名(第一个字母小写)，如：sys_user => sysUser
	private String camelClassName;
}
