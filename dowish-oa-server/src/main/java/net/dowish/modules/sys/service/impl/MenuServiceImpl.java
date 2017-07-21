package net.dowish.modules.sys.service.impl;

import net.dowish.common.utils.Constant;
import net.dowish.modules.sys.dao.MenuDao;
import net.dowish.modules.sys.entity.MenuEntity;
import net.dowish.modules.sys.service.MenuService;
import net.dowish.modules.sys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@Service("sysMenuService")
public class MenuServiceImpl implements MenuService {
	@Autowired
	private MenuDao menuDao;
	@Autowired
	private UserService userService;
	
	@Override
	public List<MenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
		List<MenuEntity> menuList = queryListParentId(parentId);
		if(menuIdList == null){
			return menuList;
		}
		
		List<MenuEntity> userMenuList = new ArrayList<>();
		for(MenuEntity menu : menuList){
			if(menuIdList.contains(menu.getMenuId())){
				userMenuList.add(menu);
			}
		}
		return userMenuList;
	}

	@Override
	public List<MenuEntity> queryListParentId(Long parentId) {
		return menuDao.queryListParentId(parentId);
	}

	@Override
	public List<MenuEntity> queryNotButtonList() {
		return menuDao.queryNotButtonList();
	}

	@Override
	public List<MenuEntity> getUserMenuList(Long userId) {

		//仅仅查询目录的下级菜单，菜单有三种类型
		ArrayList<Constant.MenuType> menuTypes = new ArrayList<>();
		menuTypes.add(Constant.MenuType.CATALOG);

		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN){
			return getAllMenuList(null,menuTypes);
		}
		
		//用户菜单列表
		List<Long> menuIdList = userService.queryAllMenuId(userId);
		return getAllMenuList(menuIdList,menuTypes);
	}

	@Override
	public List<MenuEntity> getUserAllTreeMenuList(Long userId) {
		//仅仅查询目录，菜单有三种类型
		ArrayList<Constant.MenuType> menuTypes = new ArrayList<>();
		menuTypes.add(Constant.MenuType.CATALOG);
		menuTypes.add(Constant.MenuType.MENU);

		//系统管理员，拥有最高权限
		if(userId == Constant.SUPER_ADMIN){
			return getAllMenuList(null,menuTypes);
		}

		//用户菜单列表
		List<Long> menuIdList = userService.queryAllMenuId(userId);
		return getAllMenuList(menuIdList,menuTypes);
	}

	@Override
	public MenuEntity queryObject(Long menuId) {
		return menuDao.queryObject(menuId);
	}

	@Override
	public List<MenuEntity> queryList(Map<String, Object> map) {

		return menuDao.queryList(map);
	}

	@Override
	public int queryTotal(Map<String, Object> map) {
		return menuDao.queryTotal(map);
	}

	@Override
	public void save(MenuEntity menu) {
		menuDao.save(menu);
	}

	@Override
	public void update(MenuEntity menu) {
		menuDao.update(menu);
	}

	@Override
	@Transactional
	public void deleteBatch(Long[] menuIds) {
		menuDao.deleteBatch(menuIds);
	}
	
	@Override
	public List<MenuEntity> queryUserList(Long userId) {
		return menuDao.queryUserList(userId);
	}

	/**
	 * 获取所有菜单列表
	 */
	private List<MenuEntity> getAllMenuList(List<Long> menuIdList, List<Constant.MenuType> menuTypes){
		//查询根菜单列表
		List<MenuEntity> menuList = queryListParentId(0L, menuIdList);
		//递归获取子菜单
		getMenuTreeList(menuList, menuIdList,menuTypes);
		
		return menuList;
	}

	/**
	 * 递归出某一种类型的menu下的子菜单，第一级为0，第二级为1，按钮级别为3
	 */
	private List<MenuEntity> getMenuTreeList(List<MenuEntity> menuList, List<Long> menuIdList, List<Constant.MenuType> menuTypes){
		List<MenuEntity> subMenuList = new ArrayList<>();
		
		for(MenuEntity entity : menuList){
			if(menuTypes.stream().anyMatch(menuType -> entity.getType()==menuType.getValue())){  //判断类型是否在list里面
				entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList,menuTypes));
			}
			subMenuList.add(entity);
		}
		
		return subMenuList;
	}

}
