package net.dowish.modules.job.dao;

import net.dowish.modules.job.entity.ScheduleJobLogEntity;
import net.dowish.common.base.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 定时任务日志
 */
@Mapper
public interface ScheduleJobLogDao extends BaseDao<ScheduleJobLogEntity> {
	
}
