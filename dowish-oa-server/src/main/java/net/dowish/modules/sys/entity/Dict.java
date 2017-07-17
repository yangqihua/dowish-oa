package net.dowish.modules.sys.entity;


import lombok.Data;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * 字典配置信息
 */
@Setter
public class Dict implements Serializable{


	private Long id;

	@NotBlank(message="参数名不能为空")
	private String key;

	@NotBlank(message="参数值不能为空")

	private String value;


	private String label;	// 标签名


	private String description;// 描述


	private String remark;


	public Long getId() {
		return id;
	}

	public String getKey() {
		return key;
	}

	@XmlAttribute(name="value")
	public String getValue() {
		return value;
	}

	@XmlAttribute(name="label")
	public String getLabel() {
		return label;
	}

	@XmlAttribute(name = "description")
	public String getDescription() {
		return description;
	}

	public String getRemark() {
		return remark;
	}

}
