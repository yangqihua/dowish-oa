package net.dowish.modules.sys.service.impl;

import net.dowish.common.utils.Constant;
import net.dowish.modules.sys.dao.MenuDao;
import net.dowish.modules.sys.dao.UserDao;
import net.dowish.modules.sys.dao.UserTokenDao;
import net.dowish.modules.sys.entity.MenuEntity;
import net.dowish.modules.sys.entity.UserEntity;
import net.dowish.modules.sys.entity.UserTokenEntity;
import net.dowish.modules.sys.service.ShiroService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ShiroServiceImpl implements ShiroService {
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private UserTokenDao userTokenDao;

    @Override
    public Set<String> getUserPermissions(long userId) {
        List<String> permsList;

        //系统管理员，拥有最高权限
        if(userId == Constant.SUPER_ADMIN){
            List<MenuEntity> menuList = menuDao.queryList(new HashMap<>());
            permsList = new ArrayList<>(menuList.size());
            for(MenuEntity menu : menuList){
                permsList.add(menu.getPerms());
            }
        }else{
            permsList = userDao.queryAllPerms(userId);
        }
        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for(String perms : permsList){
            if(StringUtils.isBlank(perms)){
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }
        return permsSet;
    }

    @Override
    public UserTokenEntity queryByToken(String token) {
        return userTokenDao.queryByToken(token);
    }

    @Override
    public UserEntity queryUser(Long userId) {
        return userDao.queryObject(userId);
    }
}
