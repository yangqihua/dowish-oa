package net.dowish.modules.sys.service.impl;

import net.dowish.common.utils.Apis;
import net.dowish.modules.sys.dao.SysUserTokenDao;
import net.dowish.modules.sys.entity.SysUserEntity;
import net.dowish.modules.sys.entity.SysUserTokenEntity;
import net.dowish.modules.sys.service.SysUserTokenService;
import net.dowish.modules.sys.oauth2.TokenGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service("sysUserTokenService")
public class SysUserTokenServiceImpl implements SysUserTokenService {
	@Autowired
	private SysUserTokenDao sysUserTokenDao;
	//12小时后过期
	private final static int EXPIRE = 3600 * 12;

	@Override
	public SysUserTokenEntity queryByUserId(Long userId) {
		return sysUserTokenDao.queryByUserId(userId);
	}

	@Override
	public SysUserTokenEntity queryByToken(String token) {
		return sysUserTokenDao.queryByToken(token);
	}

	@Override
	public void save(SysUserTokenEntity token) {
		sysUserTokenDao.save(token);
	}

	@Override
	public void update(SysUserTokenEntity token) {
		sysUserTokenDao.update(token);
	}

	@Override
	public void delete(Long userId) {
		sysUserTokenDao.delete(userId);
	}

	@Override
	public Apis createToken(SysUserEntity user) {
		long userId = user.getUserId();

		//生成一个token
		String token = TokenGenerator.generateValue();

		//当前时间
		Date now = new Date();
		//过期时间
		Date expireTime = new Date(now.getTime() + EXPIRE * 1000);

		//判断是否生成过token
		SysUserTokenEntity tokenEntity = queryByUserId(userId);
		if (tokenEntity == null) {
			tokenEntity = new SysUserTokenEntity();
			tokenEntity.setUserId(userId);
			tokenEntity.setToken(token);
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//保存token
			save(tokenEntity);
		} else {
			//生成过的话，只更新过期时间，这样就不会被挤掉
//			tokenEntity.setToken(token);
			if (tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
				tokenEntity.setToken(token);
			}
			tokenEntity.setUpdateTime(now);
			tokenEntity.setExpireTime(expireTime);

			//更新token
			update(tokenEntity);
		}

		user.setPassword(null);
		user.setSalt(null);

		return Apis.ok().put("token", tokenEntity.getToken()).put("expire", EXPIRE).put("user", user);
	}
}
