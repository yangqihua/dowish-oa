package net.dowish.modules.sys.service;

import net.dowish.modules.sys.entity.UserEntity;
import net.dowish.modules.sys.entity.UserTokenEntity;

import java.util.Set;

/**
 * shiro相关接口
 */
public interface ShiroService {
    /**
     * 获取用户权限列表
     */
    Set<String> getUserPermissions(long userId);

    UserTokenEntity queryByToken(String token);

    /**
     * 根据用户ID，查询用户
     * @param userId
     */
    UserEntity queryUser(Long userId);
}
