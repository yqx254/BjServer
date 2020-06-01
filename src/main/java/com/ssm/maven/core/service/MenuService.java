package com.ssm.maven.core.service;

import com.ssm.maven.core.entity.Menu;

import java.util.List;

public interface MenuService {
    /**
     * 获取菜单
     * @return 菜单对象列表
     * @param roleId  角色ID
     */
    List<Menu> getMenuList(String roleId);
}
