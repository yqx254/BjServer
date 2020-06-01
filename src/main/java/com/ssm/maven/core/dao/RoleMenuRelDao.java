package com.ssm.maven.core.dao;

import com.ssm.maven.core.entity.Menu;

import java.util.List;

/**
 * @author fstar
 */
public interface RoleMenuRelDao {
    /**
     * 用角色ID查权限菜单
     * @param roleId 角色ID
     * @return 菜单集合
     */
    List<Menu> getRoleMenu(String roleId);
}
