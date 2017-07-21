package net.dowish.modules.student.service;

import net.dowish.modules.student.entity.StudentEntity;

import java.util.List;
import java.util.Map;

/**
 * 学生
 * 
 * @author yangqihua
 * @email 904693433@qq.com
 * @date 2017-07-21 20:22:01
 */
public interface StudentService  {
	
	StudentEntity queryObject(Long id);
	
	List<StudentEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(StudentEntity student);
	
	void update(StudentEntity student);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
