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

import javax.swing.table.TableColumn;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 代码生成器   工具类
 */
@Slf4j
public class GenUtils {

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
					|| StringUtils.startsWithIgnoreCase(column.getJdbcType(), "INT")
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
			} else {
				column.setShowType("input");
			}

			// 设置java字段名
			column.setJavaField(StringUtils.toCamelCase(column.getColumnName()));

			if (column.getIsPk() || "create_user_id".equals(column.getColumnName())) {

				// 插入字段
				column.setIsInsert(false);

				// 编辑字段
				column.setIsEdit(false);

				// 列表字段
				column.setIsList(false);

				// 查询字段
				column.setIsQuery(false);

			} else {
				// 插入字段
				column.setIsInsert(true);

				// 编辑字段
				column.setIsEdit(true);

				// 列表字段
				column.setIsList(true);

				// 查询字段
				column.setIsQuery(true);

			}
			// 查询字段类型
			column.setQueryType("=");

		}
	}

	public static List<String> getTemplateList(GenConfig config, String category) {
		List<String> templateList = Lists.newArrayList();
		if (config != null && config.getCategoryList() != null && category != null) {
			for (GenCategory e : config.getCategoryList()) {
				if (category.equals(e.getValue())) {
					templateList = e.getTemplate();
					final String path = e.getPath();
					templateList = templateList.stream().map(template -> path + template).collect(Collectors.toList());
					break;
				}
			}
		}
		return templateList;
	}


	/**
	 * 获取数据模型
	 *
	 * @param genTable
	 * @param genTable
	 * @return
	 */
	public static Map<String, Object> getDataModel(GenTable genTable) {
		Map<String, Object> model = Maps.newHashMap();

		model.put("packageName", StringUtils.lowerCase(genTable.getPackageName()));
		model.put("lastPackageName", StringUtils.substringAfterLast((String) model.get("packageName"), "."));
		model.put("moduleName", StringUtils.lowerCase(genTable.getModuleName()));
		model.put("className", StringUtils.uncapitalize(genTable.getClassName()));
		model.put("ClassName", StringUtils.capitalize(genTable.getClassName()));

		model.put("functionAuthor", StringUtils.isNotBlank(genTable.getFunctionAuthor()) ? genTable.getFunctionAuthor() : "yangqihua");
		model.put("functionVersion", DateUtils.getDate());
		return model;
	}

	/**
	 * 生成代码
	 */
	public static String generatorCode(GenTable genTable) {
		//配置信息
		GenConfig config = getConfig();
		List<String> templateList = getTemplateList(config, genTable.getCategory());

		Map<String, Object> dataModel = getDataModel(genTable);

		log.info("templateList:{}", templateList);

		//没主键，则第一个字段为主键
		if (genTable.getPkList() == null || genTable.getPkList().size() == 0) {
			List<GenTableColumn> pks = Lists.newArrayList();
			pks.add(genTable.getColumnList().get(0));
			genTable.setPkList(pks);
		}

		//设置velocity资源加载器
		Properties prop = new Properties();
		prop.put("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
		Velocity.init(prop);

		//封装模板数据
		Map<String, Object> map = new HashMap<>();
		map.put("tableName", genTable.getTableName()); //表名
		map.put("comments", genTable.getComments());   //中文模块名
		map.put("pk", genTable.getPkList().get(0));    //主键
		map.put("classname", StringUtils.uncapitalize(genTable.getClassName())); // class驼峰变量
		map.put("className", StringUtils.capitalize(genTable.getClassName()));   // class类
		map.put("pathName", genTable.getModuleName().toLowerCase() + "/" + genTable.getClassName().toLowerCase());   // url pattern
		map.put("columns", genTable.getColumnList()); // 所有列
		map.put("package", StringUtils.lowerCase(genTable.getPackageName())); // 包名
		map.put("author", StringUtils.isNotBlank(genTable.getFunctionAuthor()) ? genTable.getFunctionAuthor() : "yangqihua"); //作者
		map.put("email", "904693433@qq.com"); // email
		map.put("datetime", DateUtils.format(new Date(), DateUtils.DATE_TIME_PATTERN)); // 日期

		VelocityContext context = new VelocityContext(map);
		StringBuilder result = new StringBuilder();
		// 获取模板列表
		for (String template : templateList) {
			//渲染模板
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template, "UTF-8");
			tpl.merge(context, sw);

			String fileName = getFileName(template, genTable.getClassName(), genTable.getPackageName(), genTable.getModuleName());

			// 创建并写入文件
			if (FileUtils.createFile(fileName)) {
				FileUtils.writeToFile(fileName, sw.toString(), true);
				result.append("创建 ").append(fileName).append(" 文件成功<br/>");
			} else {
				result.append("文件 ").append(fileName).append(" 已存在<br/>");
			}
		}
		return result.toString();
	}

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
	public static String getFileName(String template, String className, String packageName, String moduleName) {
		String javaPath = (SystemPathUtils.getServerMainDir() + File.separator + "java" + File.separator + packageName + File.separator).replace(".", File.separator);
		String xmlPath = (SystemPathUtils.getServerMainDir() + "resources" + File.separator + "mapper" + File.separator + moduleName + File.separator).replace(".", File.separator);
		String webPath = (SystemPathUtils.getWebPagesDir() + "modules" + File.separator + moduleName + File.separator).replace(".", File.separator);

		if (template.contains("Entity.java.vm")) {
			return javaPath + "entity" + File.separator + className + "Entity.java";
		}

		if (template.contains("Dao.java.vm")) {
			return javaPath + "dao" + File.separator + className + "Dao.java";
		}

		if (template.contains("Service.java.vm")) {
			return javaPath + "service" + File.separator + className + "Service.java";
		}

		if (template.contains("ServiceImpl.java.vm")) {
			return javaPath + "service" + File.separator + "impl" + File.separator + className + "ServiceImpl.java";
		}

		if (template.contains("Controller.java.vm")) {
			return javaPath + "controller" + File.separator + className + "Controller.java";
		}

		if (template.contains("Dao.xml.vm")) {
			return xmlPath + className.toLowerCase() + "Dao.xml";
		}

		if (template.contains("list.html.vm")) {
			return javaPath + "static" + File.separator + className.toLowerCase() + ".html";
		}

		if (template.contains("list.js.vm")) {
			return javaPath + "static" + File.separator + className.toLowerCase() + ".js";
		}

		if (template.contains("menu.sql.vm")) {
			return javaPath + "static" + File.separator + className.toLowerCase() + "_menu.sql";
		}

		return null;
	}
}
