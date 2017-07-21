package net.dowish.modules.sys.service;

import net.dowish.modules.sys.entity.MenuEntity;

import java.util.List;
import java.util.Map;


/**
 * 菜单管理
 */
public interface MenuService {
	
	/**
	 * 根据父菜单，查询子菜单
	 * @param parentId 父菜单ID
	 * @param menuIdList  用户菜单ID
	 */
	List<MenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);

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
	 * 获取用户菜单列表
	 */
	List<MenuEntity> getUserMenuList(Long userId);

	/**
	 * 获取用户菜单列表
	 */
	List<MenuEntity> getUserAllTreeMenuList(Long userId);
	
	/**
	 * 查询菜单
	 */
	MenuEntity queryObject(Long menuId);
	
	/**
	 * 查询菜单列表
	 */
	List<MenuEntity> queryList(Map<String, Object> map);
	
	/**
	 * 查询总数
	 */
	int queryTotal(Map<String, Object> map);
	
	/**
	 * 保存菜单
	 */
	void save(MenuEntity menu);
	
	/**
	 * 修改
	 */
	void update(MenuEntity menu);
	
	/**
	 * 删除
	 */
	void deleteBatch(Long[] menuIds);
	
	/**
	 * 查询用户的权限列表
	 */
	List<MenuEntity> queryUserList(Long userId);
}
