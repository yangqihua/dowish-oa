/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package net.dowish.modules.gen.entity;

import lombok.Data;
import lombok.Setter;
import net.dowish.modules.sys.entity.Dict;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 生成方案Entity
 */
@XmlRootElement(name="category")
@Setter
public class GenCategory extends Dict {

	private List<String> template;

	private List<String> childTableTemplate;

	private String path;

	// 根路径
	@XmlAttribute(name = "path")
	public String getPath() {
		return path;
	}

	// 主表模板
	@XmlElement(name = "template")
	public List<String> getTemplate() {
		return template;
	}

	// 子表模板
	@XmlElementWrapper(name = "childTable")
	@XmlElement(name = "template")
	public List<String> getChildTableTemplate() {
		return childTableTemplate;
	}

}


