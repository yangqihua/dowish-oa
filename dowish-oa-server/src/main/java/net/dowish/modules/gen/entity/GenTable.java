/*
 * Created by yangqihua on 2017/7/15.
 */
package net.dowish.modules.gen.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import com.google.common.collect.Lists;

/**
 * 业务表Entity
 */
@Data
public class GenTable implements Serializable {

	//引擎
	private String engine;
	private Date createTime;

	// 表名
	private String tableName;
	// 表描述、方案名称、菜单名称
	private String comments;
	// 类名称
	private String className;
	// 关联父表名
	private String parentTableName;
	// 关联父表外键名
	private String parentTableFkName;



	// 分类：1：增删查改单表，2：增删查改一对多，3：仅持久层，4：树结构（左树右属性），5：树结构（左树右表）
	private String category;
	// 生成包路径
	private String packageName;
	// 生成模块名
	private String moduleName;
	// 生成功能作者
	private String functionAuthor;



	// 当前表的列
	private List<GenTableColumn> columnList = Lists.newArrayList();
	// 当前表主键列表
	private List<String> pkList;
	// 父表对象
	private GenTable parent;
	// 子表列表
	private List<GenTable> childList = Lists.newArrayList();




	
}


