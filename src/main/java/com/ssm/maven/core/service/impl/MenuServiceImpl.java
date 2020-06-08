package com.ssm.maven.core.service.impl;

import com.ssm.maven.core.dao.MenuDao;
import com.ssm.maven.core.dao.RoleMenuRelDao;
import com.ssm.maven.core.entity.Menu;
import com.ssm.maven.core.service.MenuService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @author fstar
 */
@Service("MenuService")
public class MenuServiceImpl implements MenuService {

    @Resource
    private MenuDao menuDao;

    @Resource
    private RoleMenuRelDao roleMenuRelDao;

    @Override
    public List<Menu> getMenuList(String roleId) {
        List<Menu> menuList;
            menuList = roleMenuRelDao.getRoleMenu(roleId);
        List<Menu> rootMenuList = new ArrayList<>(64);
        menuList.forEach(menu ->{
            if("0".equals(menu.getParentId())){
                rootMenuList.add(menu);
            }
        });
        menuList.removeAll(rootMenuList);
        rootMenuList.forEach(menu ->{
            buildMenuList(menu,menuList);
        });
        return rootMenuList;
    }

    private void buildMenuList(Menu rootNode,
                               List<Menu> subMenu){
        subMenu.forEach(menu -> {
            if(Objects.equals(menu.getParentId(), rootNode.getId())){
                rootNode.getSubMenu().add(menu);
                buildMenuList(menu, subMenu);
            }
        });
    }
}
