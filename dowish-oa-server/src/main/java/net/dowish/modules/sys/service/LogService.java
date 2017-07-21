package net.dowish.modules.sys.service;

import net.dowish.modules.sys.entity.LogEntity;

import java.util.List;
import java.util.Map;

/**
 * 系统日志
 */
public interface LogService {
	
	LogEntity queryObject(Long id);
	
	List<LogEntity> queryList(Map<String, Object> map);
	
	int queryTotal(Map<String, Object> map);
	
	void save(LogEntity sysLog);
	
	void update(LogEntity sysLog);
	
	void delete(Long id);
	
	void deleteBatch(Long[] ids);
}
