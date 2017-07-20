package net.dowish.modules.gen.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import net.dowish.common.utils.DateUtils;
import net.dowish.common.utils.FileUtils;
import net.dowish.common.utils.StringUtils;
import net.dowish.common.utils.SystemPathUtils;
import net.dowish.common.utils.mapper.JaxbMapper;
import net.dowish.modules.gen.entity.*;
import org.apache.commons.lang.WordUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代码生成器   工具类
 */
@Slf4j
public class GenUtils {

//	public static List<String> getTemplates() {
//		List<String> templates = new ArrayList<String>();
//		templates.add("gen/curd/Entity.java.vm");
//		templates.add("gen/curd/Dao.java.vm");
//		templates.add("gen/curd/Dao.xml.vm");
//		templates.add("gen/curd/Service.java.vm");
//		templates.add("gen/curd/ServiceImpl.java.vm");
//		templates.add("gen/curd/Controller.java.vm");
//		templates.add("gen/curd/list.html.vm");
//		templates.add("gen/curd/list.js.vm");
//		templates.add("gen/curd/menu.sql.vm");
//		return templates;
//	}


	public static void initColumnField(GenTable genTable) {
		for (GenTableColumn column : genTable.getColumnList()) {
			// 设置字段说明
			if (StringUtils.isBlank(column.getComments())) {
				column.setComments(column.getColumnName());
			}

			// 设置java类型
			if (StringUtils.startsWithIgnoreCase(column.getJdbcType(), "CHAR")
					|| StringUtils.startsWithIgnoreCase(column.getJdbcType(), "VARCHAR")
					|| StringUtils.startsWithIgnoreCase(column.getJdbcType(), "NARCHAR")) {
				column.setJavaType("String");
				column.setShowType("input");
			} else if (StringUtils.startsWithIgnoreCase(column.getJdbcType(), "DATETIME")
					|| StringUtils.startsWithIgnoreCase(column.getJdbcType(), "DATE")
					|| StringUtils.startsWithIgnoreCase(column.getJdbcType(), "TIMESTAMP")) {
				column.setJavaType("java.util.Date");
				column.setShowType("dateselect");
			} else if (StringUtils.startsWithIgnoreCase(column.getJdbcType(), "BIGINT")
					|| StringUtils.startsWithIgnoreCase(column.getJdbcType(), "NUMBER")
					|| StringUtils.startsWithIgnoreCase(column.getJdbcType(), "TINYINT")
					) {
				// 如果是浮点型
				String[] ss = StringUtils.split(StringUtils.substringBetween(column.getJdbcType(), "(", ")"), ",");
				if (ss != null && ss.length == 2 && Integer.parseInt(ss[1]) > 0) {
					column.setJavaType("Double");
				}
				// 如果是整形
				else if (ss != null && ss.length == 1 && Integer.parseInt(ss[0]) <= 10) {
					column.setJavaType("Integer");
				}
				// 长整形
				else {
					column.setJavaType("Long");
				}
				column.setShowType("input");
			}

			// 设置java字段名
			column.setJavaField(StringUtils.toCamelCase(column.getColumnName()));

			// 是否是主键
			column.setIsPk(genTable.getPkList().contains(column.getColumnName()));

			// 插入字段
			column.setIsInsert(true);

			// 编辑字段
			column.setIsEdit(true);

			// 列表字段
			column.setIsList(true);

			// 查询字段
			column.setIsQuery(true);

			// 查询字段类型
			column.setQueryType("=");

		}
	}

	public static List<String> getTemplateList(GenConfig config, String category){
		List<String> templateList = Lists.newArrayList();
		if (config !=null && config.getCategoryList() != null && category !=  null){
			for (GenCategory e : config.getCategoryList()){
				if (category.equals(e.getValue())){
					templateList = e.getTemplate();
					final String path = e.getPath();
					templateList = templateList.stream().map(template->path+template).collect(Collectors.toList());
					break;
				}
			}
		}
		return templateList;
	}


	/**
	 * 获取数据模型
	 * @param genTable
	 * @param genTable
	 * @return
	 */
	public static Map<String, Object> getDataModel(GenTable genTable){
		Map<String, Object> model = Maps.newHashMap();

		model.put("packageName", StringUtils.lowerCase(genTable.getPackageName()));
		model.put("lastPackageName", StringUtils.substringAfterLast((String)model.get("packageName"),"."));
		model.put("moduleName", StringUtils.lowerCase(genTable.getModuleName()));
		model.put("className", StringUtils.uncapitalize(genTable.getClassName()));
		model.put("ClassName", StringUtils.capitalize(genTable.getClassName()));

		model.put("functionAuthor", StringUtils.isNotBlank(genTable.getFunctionAuthor())?genTable.getFunctionAuthor():"yangqihua");
		model.put("functionVersion", DateUtils.getDate());
		return model;
	}

	/**
	 * 生成代码
	 */
	public static String generatorCode(GenTable genTable) {
		//配置信息
		GenConfig config = getConfig();
		List<String> templateList = getTemplateList(config,genTable.getCategory());

		Map<String, Object> dataModel = getDataModel(genTable);

		log.info("templateList:{}",templateList);
		//表信息
//		GenTableEntity genTableEntity = new GenTableEntity();
//		genTableEntity.setTableName(table.get("tableName"));
//		genTableEntity.setComments(table.get("tableComment"));
//		//表名转换成Java类名
//		String className = tableToJava(genTableEntity.getTableName(), config.getString("tablePrefix"));
//		genTableEntity.setClassName(className);
//		genTableEntity.setCamelClassName(StringUtils.uncapitalize(className));
//
//		//列信息
//		List<GenTableColumnEntity> columsList = new ArrayList<>();
//		for (Map<String, String> column : columns) {
//			GenTableColumnEntity genTableColumnEntity = new GenTableColumnEntity();
//			genTableColumnEntity.setColumnName(column.get("columnName"));
//			genTableColumnEntity.setJdbcType(column.get("dataType"));
//			genTableColumnEntity.setComments(column.get("columnComment"));
//			genTableColumnEntity.setExtra(column.get("extra"));
//
//			//列名转换成Java属性名
//			String attrName = columnToJava(genTableColumnEntity.getColumnName());
//			genTableColumnEntity.setAttrName(attrName);
//			genTableColumnEntity.setCamelAttrName(StringUtils.uncapitalize(attrName));
//
//			//列的数据类型，转换成Java类型
//			String attrType = config.getString(genTableColumnEntity.getJdbcType(), Object.class.getSimpleName());
//			genTableColumnEntity.setAttrType(attrType);
//
//			//是否主键
//			if ("PRI".equalsIgnoreCase(column.get("columnKey")) && genTableEntity.getPk() == null) {
//				genTableEntity.setPk(genTableColumnEntity);
//			}
//
//			columsList.add(genTableColumnEntity);
//		}
//		genTableEntity.setColumns(columsList);
//
//		//没主键，则第一个字段为主键
//		if (genTableEntity.getPk() == null) {
//			genTableEntity.setPk(genTableEntity.getColumns().get(0));
//		}
//
//		//设置velocity资源加载器
		Properties prop = new Properties();
		prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(prop);

		//封装模板数据
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", genTable.getTableName());
		map.put("comments", genTable.getComments());
		map.put("pk", genTable.getPkList());
		map.put("classname", StringUtils.uncapitalize(genTable.getClassName()));
		map.put("className", StringUtils.capitalize(genTable.getClassName()));
		map.put("pathName", genTable.getClassName().toLowerCase());
		map.put("columns", genTable.getColumnList());
		map.put("package", StringUtils.lowerCase(genTable.getPackageName()));
		map.put("author", StringUtils.isNotBlank(genTable.getFunctionAuthor())?genTable.getFunctionAuthor():"yangqihua");
		map.put("email", "904693433@qq.com");
		map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN));

		VelocityContext context = new VelocityContext(map);
//
//
		StringBuilder result = new StringBuilder();
		//获取模板列表
//		List<String> templates = getTemplate();
		for (String template : templateList) {
			//渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, "UTF-8");
			tpl.merge(context, sw);

			String fileName = SystemPathUtils.getServerMainDir() + getFileName(template, genTable.getClassName(), genTable.getPackageName()+File.separator+genTable.getModuleName());

//			log.info("fileName : {}",fileName);
			// 创建并写入文件
			if (FileUtils.createFile(fileName)) {
				FileUtils.writeToFile(fileName, sw.toString(), true);
				result.append("创建 ").append(fileName).append(" 文件成功<br/>");
			} else {
				result.append("创建 ").append(fileName).append(" 文件失败<br/>");
				log.debug(" file fail to create === " + fileName);
			}
		}
		return result.toString();
	}


	/**
	 * 列名转换成Java属性名
	 */
	public static String columnToJava(String columnName) {
		return WordUtils.capitalizeFully(columnName, new char[]{'_'}).replace("_", "");
	}

	/**
	 * 表名转换成Java类名
	 */
	public static String tableToJava(String tableName, String tablePrefix) {
		if (StringUtils.isNotBlank(tablePrefix)) {
			tableName = tableName.replace(tablePrefix, "");
		}
		return columnToJava(tableName);
	}

	/**
	 * 获取配置信息
	 */
//	public static Configuration getConfig() {
//		try {
//			return new PropertiesConfiguration("generator.properties");
//		} catch (ConfigurationException e) {
//			throw new ResultException("获取配置文件失败，", e);
//		}
//	}

	/**
	 * 获取代码生成配置对象
	 *
	 * @return
	 */
	public static GenConfig getConfig() {
		return fileToObject("/gen/config.xml", GenConfig.class);
	}

	/**
	 * XML文件转换为对象
	 *
	 * @param fileName
	 * @param clazz
	 * @return
	 */
	public static <T> T fileToObject(String fileName, Class<?> clazz) {
		try {
			Resource resource = new ClassPathResource(fileName);
			InputStream is = resource.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
			StringBuilder sb = new StringBuilder();
			while (true) {
				String line = br.readLine();
				if (line == null) {
					break;
				}
				sb.append(line).append("\r\n");
			}
			is.close();
			br.close();
//			log.debug("Read file content: {}", sb.toString());
			return (T) JaxbMapper.fromXml(sb.toString(), clazz);
		} catch (Exception e) {
			e.printStackTrace();
			log.warn("Error file convert: {}", e.getMessage());
		}
		return null;
	}

	/**
	 * 获取文件名
	 */
	public static String getFileName(String template, String className, String packageName) {
		String packagePath = "main" + File.separator + "java" + File.separator;
		if (StringUtils.isNotBlank(packageName)) {
			packagePath += packageName.replace(".", File.separator) + File.separator;
		}

		if (template.contains("Entity.java.vm")) {
			return packagePath + "entity" + File.separator + className + "Entity.java";
		}

		if (template.contains("Dao.java.vm")) {
			return packagePath + "dao" + File.separator + className + "Dao.java";
		}

		if (template.contains("Service.java.vm")) {
			return packagePath + "service" + File.separator + className + "Service.java";
		}

		if (template.contains("ServiceImpl.java.vm")) {
			return packagePath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
		}

		if (template.contains("Controller.java.vm")) {
			return packagePath + "controller" + File.separator + className + "Controller.java";
		}

		if (template.contains("Dao.xml.vm")) {
//			return "main" + File.separator + "resources" + File.separator + "mapper" + File.separator + className + "Dao.xml";
			return packagePath + "static" + File.separator + className + "Dao.xml";
		}

		if (template.contains("list.html.vm")) {
			return packagePath + "static" + File.separator + className.toLowerCase() + ".html";
		}

		if (template.contains("list.js.vm")) {
			return packagePath + "static" + File.separator + className.toLowerCase() + ".js";
		}

		if (template.contains("menu.sql.vm")) {
			return packagePath + "static" + File.separator + className.toLowerCase() + "_menu.sql";
		}

		return null;
	}
}
