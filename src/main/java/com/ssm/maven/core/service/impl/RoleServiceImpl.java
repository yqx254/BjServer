package com.ssm.maven.core.service.impl;

import com.ssm.maven.core.dao.RoleDao;
import com.ssm.maven.core.entity.Role;
import com.ssm.maven.core.service.RoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author fstar
 */
@Service("RoleService")
public class RoleServiceImpl implements RoleService {
    @Resource
    private RoleDao roleDao;
    @Override
    public List<Role> roleList(Map<String, Object> query) {

        return roleDao.roleList(query);
    }

    /**
     * get role total
     *
     * @param query query map
     * @return role count
     */
    @Override
    public Long roleTotal(Map<String, Object> query) {
        return roleDao.roleTotal(query);
    }

    @Override
    public Role findRoleById(String id) {
        return roleDao.findRoleById(id);
    }

    @Override
    public void addRole(Role role) {
        roleDao.addRole(role);
    }

    @Override
    public void updateRole(Role role) {
        roleDao.updateRole(role);
    }

    @Override
    public void deleteRole(String id) {
        roleDao.deleteRole(id);
    }
}
