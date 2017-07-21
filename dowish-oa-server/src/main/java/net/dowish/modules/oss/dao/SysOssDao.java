package net.dowish.modules.oss.dao;

import net.dowish.modules.oss.entity.SysOssEntity;
import net.dowish.common.base.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * 文件上传
 */
@Mapper
public interface SysOssDao extends BaseDao<SysOssEntity> {
	
}
