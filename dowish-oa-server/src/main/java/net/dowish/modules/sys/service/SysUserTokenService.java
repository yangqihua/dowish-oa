package net.dowish.modules.sys.service;

import net.dowish.modules.sys.entity.SysUserEntity;
import net.dowish.modules.sys.entity.SysUserTokenEntity;
import net.dowish.common.utils.Apis;

/**
 * 用户Token
 */
public interface SysUserTokenService {

	SysUserTokenEntity queryByUserId(Long userId);

	SysUserTokenEntity queryByToken(String token);
	
	void save(SysUserTokenEntity token);
	
	void update(SysUserTokenEntity token);

	void delete(Long userId);

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	Apis createToken(SysUserEntity userEntity);

}
