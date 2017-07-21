package net.dowish.modules.sys.dao;

import net.dowish.common.base.dao.BaseDao;
import net.dowish.modules.sys.entity.UserTokenEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统用户Token
 */
@Mapper
public interface UserTokenDao extends BaseDao<UserTokenEntity> {
    
    UserTokenEntity queryByUserId(Long userId);

    UserTokenEntity queryByToken(String token);
	
}
