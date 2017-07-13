package net.dowish.modules.auto.controller;

import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.dowish.modules.auto.entity.StudentEntity;
import net.dowish.modules.auto.service.StudentService;
import net.dowish.modules.auto.utils.PageUtils;
import net.dowish.modules.auto.utils.Query;
import net.dowish.modules.auto.utils.R;


/**
 * 学生
 * 
 * @author yangqihua
 * @email yangqihua@dowish.net
 * @date 2017-07-13 15:11:02
 */
@RestController
@RequestMapping("student")
public class StudentController {
	@Autowired
	private StudentService studentService;
	
	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("student:list")
	public R list(@RequestParam Map<String, Object> params){
		//查询列表数据
        Query query = new Query(params);

		List<StudentEntity> studentList = studentService.queryList(query);
		int total = studentService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(studentList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{${pk.attrname}}")
	@RequiresPermissions("student:info")
	public R info(@PathVariable("${pk.attrname}") Integer ${pk.attrname}){
		StudentEntity student = studentService.queryObject(${pk.attrname});
		
		return R.ok().put("student", student);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("student:save")
	public R save(@RequestBody StudentEntity student){
		studentService.save(student);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("student:update")
	public R update(@RequestBody StudentEntity student){
		studentService.update(student);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("student:delete")
	public R delete(@RequestBody Integer[] ${pk.attrname}s){
		studentService.deleteBatch(${pk.attrname}s);
		
		return R.ok();
	}
	
}
