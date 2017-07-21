package net.dowish.modules.sys.dao;

import net.dowish.common.base.dao.BaseDao;
import net.dowish.modules.sys.entity.MenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单管理
 */
@Mapper
public interface MenuDao extends BaseDao<MenuEntity> {
	
	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 */
	List<MenuEntity> queryListParentId(Long parentId);
	
	/**
	 * 获取不包含按钮的菜单列表
	 */
	List<MenuEntity> queryNotButtonList();
	
	/**
	 * 查询用户的权限列表
	 */
	List<MenuEntity> queryUserList(Long userId);
}
