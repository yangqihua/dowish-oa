package net.dowish.modules.sys.controller;

import net.dowish.common.annotation.SysLog;
import net.dowish.common.base.controller.BaseController;
import net.dowish.common.exception.ResultException;
import net.dowish.common.utils.Apis;
import net.dowish.common.utils.Constant;
import net.dowish.modules.sys.entity.MenuEntity;
import net.dowish.modules.sys.service.ShiroService;
import net.dowish.modules.sys.service.MenuService;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * 系统菜单
 * 
 */
@RestController
@RequestMapping("/sys/menu")
public class MenuController extends BaseController {
	@Autowired
	private MenuService menuService;
	@Autowired
	private ShiroService shiroService;
	
	/**
	 * 所有菜单列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:menu:list")
	public List<MenuEntity> list(){
		List<MenuEntity> menuList = menuService.queryList(new HashMap<>());
		return menuList;
//		return Apis.ok().put("menuList",menuList);
	}
	
	/**
	 * 选择菜单(添加、修改菜单)
	 */
	@RequestMapping("/select")
	@RequiresPermissions("sys:menu:select")
	public Apis select(){
		//查询列表数据
		List<MenuEntity> menuList = menuService.queryNotButtonList();
		
		//添加顶级菜单
		MenuEntity root = new MenuEntity();
		root.setMenuId(0L);
		root.setName("一级菜单");
		root.setParentId(-1L);
		root.setOpen(true);
		menuList.add(root);
		
		return Apis.ok().put("menuList", menuList);
	}
	
	/**
	 * 角色授权菜单
	 */
	@RequestMapping("/perms")
	@RequiresPermissions("sys:menu:perms")
	public Apis perms(){
		//查询列表数据
//		List<MenuEntity> menuList = menuService.getUserAllTreeMenuList(getUserId());
		
		List<MenuEntity> menuList;
		if(getUserId() == Constant.SUPER_ADMIN){
			menuList = menuService.queryList(new HashMap<>());
		}else{
			menuList = menuService.queryUserList(getUserId());
		}
		
		return Apis.ok().put("menuList", menuList);
	}
	
	/**
	 * 菜单信息
	 */
	@RequestMapping("/info/{menuId}")
	@RequiresPermissions("sys:menu:info")
	public Apis info(@PathVariable("menuId") Long menuId){
		MenuEntity menu = menuService.queryObject(menuId);
		return Apis.ok().put("menu", menu);
	}
	
	/**
	 * 保存
	 */
	@SysLog("保存菜单")
	@RequestMapping("/save")
	@RequiresPermissions("sys:menu:save")
	public Apis save(@RequestBody MenuEntity menu){
		//数据校验
		verifyForm(menu);
		
		menuService.save(menu);
		
		return Apis.ok();
	}
	
	/**
	 * 修改
	 */
	@SysLog("修改菜单")
	@RequestMapping("/update")
	@RequiresPermissions("sys:menu:update")
	public Apis update(@RequestBody MenuEntity menu){
		//数据校验
		verifyForm(menu);
				
		menuService.update(menu);
		
		return Apis.ok();
	}
	
	/**
	 * 删除
	 */
	@SysLog("删除菜单")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:menu:delete")
	public Apis delete(@RequestBody Map<String ,Long> map){
		Long menuId = map.get("menuId");
		if(menuId <= 30){
			return Apis.error("系统菜单，不能删除");
		}

		//判断是否有子菜单或按钮
		List<MenuEntity> menuList = menuService.queryListParentId(menuId);
		if(menuList.size() > 0){
			return Apis.error("请先删除子菜单或按钮");
		}

		menuService.deleteBatch(new Long[]{menuId});
		
		return Apis.ok();
	}
	
	/**
	 * 用户菜单列表
	 */
	@RequestMapping("/user")
	public Apis user(){
		List<MenuEntity> menuList = menuService.getUserMenuList(getUserId());
		Set<String> permissions = shiroService.getUserPermissions(getUserId());
		return Apis.ok().put("menuList", menuList).put("permissions", permissions);
	}
	
	/**
	 * 验证参数是否正确
	 */
	private void verifyForm(MenuEntity menu){
		if(StringUtils.isBlank(menu.getName())){
			throw new ResultException("菜单名称不能为空");
		}
		
		if(menu.getParentId() == null){
			throw new ResultException("上级菜单不能为空");
		}
		
		//菜单
		if(menu.getType() == Constant.MenuType.MENU.getValue()){
			if(StringUtils.isBlank(menu.getUrl())){
				throw new ResultException("菜单URL不能为空");
			}
		}
		
		//上级菜单类型
		int parentType = Constant.MenuType.CATALOG.getValue();
		if(menu.getParentId() != 0){
			MenuEntity parentMenu = menuService.queryObject(menu.getParentId());
			parentType = parentMenu.getType();
		}
		
		//目录、菜单
		if(menu.getType() == Constant.MenuType.CATALOG.getValue() ||
				menu.getType() == Constant.MenuType.MENU.getValue()){
			if(parentType != Constant.MenuType.CATALOG.getValue()){
				throw new ResultException("上级菜单只能为目录类型");
			}
			return ;
		}
		
		//按钮
		if(menu.getType() == Constant.MenuType.BUTTON.getValue()){
			if(parentType != Constant.MenuType.MENU.getValue()){
				throw new ResultException("上级菜单只能为菜单类型");
			}
			return ;
		}
	}
}
