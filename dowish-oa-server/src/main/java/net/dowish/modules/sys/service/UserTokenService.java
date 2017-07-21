package net.dowish.modules.sys.service;

import net.dowish.modules.sys.entity.UserEntity;
import net.dowish.modules.sys.entity.UserTokenEntity;
import net.dowish.common.utils.Apis;

/**
 * 用户Token
 */
public interface UserTokenService {

	UserTokenEntity queryByUserId(Long userId);

	UserTokenEntity queryByToken(String token);
	
	void save(UserTokenEntity token);
	
	void update(UserTokenEntity token);

	void delete(Long userId);

	/**
	 * 生成token
	 * @param userId  用户ID
	 */
	Apis createToken(UserEntity userEntity);

}
