package net.dowish.modules.student.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import net.dowish.common.base.service.BaseService;
import java.util.List;
import java.util.Map;

import net.dowish.modules.student.dao.StudentDao;
import net.dowish.modules.student.entity.StudentEntity;
import net.dowish.modules.student.service.StudentService;



@Service("studentService")
public class StudentServiceImpl extends BaseService implements StudentService {
	@Autowired
	private StudentDao studentDao;
	
	@Override
	public StudentEntity queryObject(Long id){
		return studentDao.queryObject(id);
	}
	
	@Override
	public List<StudentEntity> queryList(Map<String, Object> map){

		map.put("sqlFilter",dataScopeFilter("student","student"));
		return studentDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return studentDao.queryTotal(map);
	}
	
	@Override
	public void save(StudentEntity student){
		studentDao.save(student);
	}
	
	@Override
	public void update(StudentEntity student){
		studentDao.update(student);
	}
	
	@Override
	public void delete(Long id){
		studentDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		studentDao.deleteBatch(ids);
	}
	
}
