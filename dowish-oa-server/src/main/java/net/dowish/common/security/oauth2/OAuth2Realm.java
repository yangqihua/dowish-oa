package net.dowish.common.security.oauth2;

import net.dowish.modules.sys.entity.UserEntity;
import net.dowish.modules.sys.entity.UserTokenEntity;
import net.dowish.modules.sys.service.ShiroService;
import net.dowish.modules.sys.service.UserTokenService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * 认证
 */
@Component
public class OAuth2Realm extends AuthorizingRealm {
	@Autowired
	private ShiroService shiroService;

	@Autowired
	private UserTokenService userTokenService;

	@Override
	public boolean supports(AuthenticationToken token) {
		return token instanceof OAuth2Token;
	}

	/**
	 * 授权(验证权限时调用)
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		UserEntity user = (UserEntity) principals.getPrimaryPrincipal();
		Long userId = user.getUserId();

		//用户权限列表
		Set<String> permsSet = shiroService.getUserPermissions(userId);

		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		info.setStringPermissions(permsSet);
		return info;
	}

	/**
	 * 认证(登录时调用)
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		String accessToken = (String) token.getPrincipal();

		//根据accessToken，查询用户信息
		UserTokenEntity tokenEntity = shiroService.queryByToken(accessToken);
		//token失效
		if (tokenEntity == null || tokenEntity.getExpireTime().getTime() < System.currentTimeMillis()) {
			if (tokenEntity != null) {
				userTokenService.delete(tokenEntity.getUserId());

			}
			throw new IncorrectCredentialsException("token失效，请重新登录");
		}

		//查询用户信息
		UserEntity user = shiroService.queryUser(tokenEntity.getUserId());
		//账号锁定
		if (user.getStatus() == 0) {
			throw new LockedAccountException("账号已被锁定,请联系管理员");
		}

		SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, accessToken, getName());
		return info;
	}
}
