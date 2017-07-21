package net.dowish.modules.sys.dao;

import net.dowish.common.base.dao.BaseDao;
import net.dowish.modules.sys.entity.LogEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统日志
 */
@Mapper
public interface LogDao extends BaseDao<LogEntity> {
	
}
