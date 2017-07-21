package net.dowish.modules.student.entity;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;


/**
 * 学生
 * 
 * @author yangqihua
 * @email 904693433@qq.com
 * @date 2017-07-21 14:23:51
 */
@Data
public class StudentEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
 	/**
	 *  id
	 */
	private Long id;

 	/**
	 *  姓名
	 */
	private String name;

 	/**
	 *  地址
	 */
	private String address;

 	/**
	 *  create_user_id
	 */
	private Long createUserId;


}
