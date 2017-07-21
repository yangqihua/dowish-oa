package net.dowish.modules.sys.entity;


import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 角色
 */
@Data
public class RoleEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
	 * 角色ID
	 */
	private Long roleId;

	/**
	 * 角色名称
	 */
	@NotBlank(message="角色名称不能为空")
	private String roleName;

	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 创建者ID
	 */
	private Long createUserId;
	
	private List<Long> menuIdList;
	
	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 部门ID
	 */
//	@NotNull(message="部门不能为空")
	private Long deptId;

	/**
	 * 部门名称
	 */
	private String deptName;

	@NotNull(message="部门不能为空")
	private List<Long> deptIdList;

}
