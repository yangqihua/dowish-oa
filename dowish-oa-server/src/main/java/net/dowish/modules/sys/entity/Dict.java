package net.dowish.modules.sys.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.xml.bind.annotation.XmlAttribute;
import java.io.Serializable;

/**
 * 字典配置信息
 */
@Setter
public class Dict implements Serializable{

	@Getter
	private Long id;

	@Getter
	@NotBlank(message="参数名不能为空")
	private String key;

	@Getter
	private String remark;

	@NotBlank(message="参数值不能为空")
	private String value;


	private String label;	// 标签名


	private String description;// 描述


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

}
