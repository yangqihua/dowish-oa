package net.dowish.modules.auto.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import net.dowish.modules.auto.dao.StudentDao;
import net.dowish.modules.auto.entity.StudentEntity;
import net.dowish.modules.auto.service.StudentService;



@Service("studentService")
public class StudentServiceImpl implements StudentService {
	@Autowired
	private StudentDao studentDao;
	
	@Override
	public StudentEntity queryObject(Integer ${pk.attrname}){
		return studentDao.queryObject(${pk.attrname});
	}
	
	@Override
	public List<StudentEntity> queryList(Map<String, Object> map){
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
	public void delete(Integer ${pk.attrname}){
		studentDao.delete(${pk.attrname});
	}
	
	@Override
	public void deleteBatch(Integer[] ${pk.attrname}s){
		studentDao.deleteBatch(${pk.attrname}s);
	}
	
}
