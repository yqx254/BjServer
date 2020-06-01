package com.ssm.maven.core.service;

import com.ssm.maven.core.entity.Role;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public interface RoleService extends Serializable {
    /**
     *  get role list
     * @param query query map
     * @return role list
     */
    List<Role> roleList(Map<String, Object> query);

    /**
     *  get role total
     * @param query query map
     * @return role count
     */
    Long roleTotal(Map<String, Object> query);
    /**
     *  find role by id
     * @param id role id
     * @return Role object
     */
    Role findRoleById(String id );
    /**
     *  add role
     * @param role role object
     */
    void addRole(Role role);

    /**
     * update role
     * @param role role object
     */
    void  updateRole(Role role);

    /**
     * delete role
     * @param id String
     */
    void  deleteRole(String id);
}
