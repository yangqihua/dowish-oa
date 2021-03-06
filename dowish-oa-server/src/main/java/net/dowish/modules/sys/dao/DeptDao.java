package net.dowish.modules.sys.dao;

import net.dowish.common.base.dao.BaseDao;
import net.dowish.modules.sys.entity.DeptEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 部门管理
 */
@Mapper
public interface DeptDao extends BaseDao<DeptEntity> {

    /**
     * 查询子部门ID列表
     * @param parentId  上级部门ID
     */
    List<Long> queryDetpIdList(Long parentId);
}
