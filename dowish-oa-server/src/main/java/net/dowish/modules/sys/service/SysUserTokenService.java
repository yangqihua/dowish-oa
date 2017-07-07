package net.dowish.modules.sys.service;

import net.dowish.modules.sys.entity.SysUserTokenEntity;
import net.dowish.common.utils.Apis;

/**
 * 用户Token
 * 
 *
 * @date 2017-03-23 15:22:07
 */
public interface SysUserTokenService {

	SysUserTokenEntity queryByUserId(Long userId);

	SysUserTokenEntity queryByToken(String token);
	
	void save(SysUserTokenEntity token);
	
	void update(SysUserTokenEntity token);

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	Apis createToken(long userId);

}
