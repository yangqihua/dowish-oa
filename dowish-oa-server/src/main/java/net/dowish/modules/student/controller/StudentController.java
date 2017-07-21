package net.dowish.modules.student.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.dowish.common.utils.Apis;
import net.dowish.common.utils.Page;
import net.dowish.common.utils.Query;
import net.dowish.common.security.validator.ValidatorUtils;

import net.dowish.modules.student.entity.StudentEntity;
import net.dowish.modules.student.service.StudentService;


/**
 * 学生 Controller
 * 
 * @author yangqihua
 * @email 904693433@qq.com
 * @date 2017-07-21 14:23:51
 */
@RestController
@RequestMapping("/student/student")
public class StudentController {
	@Autowired
	private StudentService studentService;
	
	/**
	 * 学生 列表
	 *
	 * @param map 至少包含了分页参数page、limit，可附加 Student 的属性作为查询条件.
	 * Student 的属性包括：
	 * id ：Long id
	 * 姓名 ：String name
	 * 地址 ：String address
	 * create_user_id ：Long createUserId
	 *
	 * @return 包含了 学生 的分页列表
	 */
	@GetMapping("/list")
	@RequiresPermissions("student:student:list")
	public Apis list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);
		List<StudentEntity> studentList = studentService.queryList(query);
		int total = studentService.queryTotal(query);
		Page page = new Page(studentList, total, query.getLimit(), query.getPage());
		return Apis.ok() .put("page", page);
	}
	
	
	/**
	 * 学生 详情
	 */
	@GetMapping("/info/{id}")
	@RequiresPermissions("student:student:info")
	public Apis info(@PathVariable("id") Long id){
		StudentEntity student = studentService.queryObject(id);
		return Apis.ok() .put("student", student);
	}
	
	/**
	 * 学生 保存
	 */
	@PostMapping("/save")
	@RequiresPermissions("student:student:save")
	public Apis save(@RequestBody StudentEntity student){
		ValidatorUtils.validateEntity(student);
		studentService.save(student);
		return Apis.ok() ;
	}
	
	/**
	 * 学生 修改
	 */
	@PostMapping("/update")
	@RequiresPermissions("student:student:update")
	public Apis update(@RequestBody StudentEntity student){
		ValidatorUtils.validateEntity(student);
		studentService.update(student);
		return Apis.ok() ;
	}
	
	/**
	 * 学生 删除
	 */
	@PostMapping("/delete")
	@RequiresPermissions("student:student:delete")
	public Apis delete(@RequestBody Long[] ids){
		studentService.deleteBatch(ids);
		return Apis.ok() ;
	}
	
}
