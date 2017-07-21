package net.dowish.modules.sys.service.impl;

import net.dowish.modules.sys.dao.LogDao;
import net.dowish.modules.sys.entity.LogEntity;
import net.dowish.modules.sys.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;



@Service("sysLogService")
public class LogServiceImpl implements LogService {
	@Autowired
	private LogDao logDao;
	
	@Override
	public LogEntity queryObject(Long id){
		return logDao.queryObject(id);
	}
	
	@Override
	public List<LogEntity> queryList(Map<String, Object> map){
		return logDao.queryList(map);
	}
	
	@Override
	public int queryTotal(Map<String, Object> map){
		return logDao.queryTotal(map);
	}
	
	@Override
	public void save(LogEntity sysLog){
		logDao.save(sysLog);
	}
	
	@Override
	public void update(LogEntity sysLog){
		logDao.update(sysLog);
	}
	
	@Override
	public void delete(Long id){
		logDao.delete(id);
	}
	
	@Override
	public void deleteBatch(Long[] ids){
		logDao.deleteBatch(ids);
	}
	
}
