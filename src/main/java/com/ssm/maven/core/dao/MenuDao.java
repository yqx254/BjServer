package com.ssm.maven.core.dao;

import com.ssm.maven.core.entity.Menu;

import java.io.Serializable;
import java.util.List;

public interface MenuDao extends Serializable {
    /**
     *  获取菜单列表
     * @return  菜单列表
     */
        List<Menu> getMenuList();

}
