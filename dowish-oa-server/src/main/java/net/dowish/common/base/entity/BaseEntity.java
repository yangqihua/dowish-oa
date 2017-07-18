package net.dowish.common.base.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Maps;
import net.dowish.common.utils.Page;
import net.dowish.modules.api.entity.UserEntity;
import net.dowish.modules.sys.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;

import javax.xml.bind.annotation.XmlTransient;
import java.io.Serializable;
import java.util.Map;

/**
 * Created by yangqihua on 2017/7/18.
 */
public abstract class BaseEntity<T> implements Serializable {


	/**
	 * 当前用户
	 */
	protected SysUserEntity currentUser;

	/**
	 * 自定义SQL（SQL标识，SQL内容）
	 */
	protected Map<String, String> sqlMap;

	/**
	 * 当前实体分页对象
	 */
//	protected Page<T> page;

	public void setCurrentUser(SysUserEntity currentUser) {
		this.currentUser = currentUser;
	}

//	@JsonIgnore
//	@XmlTransient
//	public Page<T> getPage() {
//		if (page == null){
//			page = new Page<T>();
//		}
//		return page;
//	}
//
//	public Page<T> setPage(Page<T> page) {
//		this.page = page;
//		return page;
//	}

	@JsonIgnore
	@XmlTransient
	public Map<String, String> getSqlMap() {
		if (sqlMap == null){
			sqlMap = Maps.newHashMap();
		}
		return sqlMap;
	}

	public void setSqlMap(Map<String, String> sqlMap) {
		this.sqlMap = sqlMap;
	}



	@JsonIgnore
	@XmlTransient
	public SysUserEntity getCurrentUser() {
		if(currentUser == null){
			SysUserEntity userEntity = (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
			return userEntity;
		}
		return currentUser;
	}
}
