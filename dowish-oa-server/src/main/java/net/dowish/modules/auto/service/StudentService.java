package net.dowish.modules.auto.service;

import net.dowish.modules.auto.entity.StudentEntity;

import java.util.List;
import java.util.Map;

/**
 * 学生
 * 
 * @author yangqihua
 * @email yangqihua@dowish.net
 * @date 2017-07-13 15:11:02
 */
public interface StudentService {
	
	StudentEntity queryObject(Integer ${pk.attrname});
	
	List<StudentEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(StudentEntity student);
	
	void update(StudentEntity student);
	
	void delete(Integer ${pk.attrname});
	
	void deleteBatch(Integer[] ${pk.attrname}s);
}
