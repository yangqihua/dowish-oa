package net.dowish.modules.api.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;



/**
 * 用户
 */
@Data
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//用户ID
	private Long userId;
	//用户名
	private String username;
	//手机号
	private String mobile;
	//密码
	transient private String password;
	//创建时间
	private Date createTime;
}
