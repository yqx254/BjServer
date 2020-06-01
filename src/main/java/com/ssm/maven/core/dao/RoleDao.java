package com.ssm.maven.core.dao;

import com.ssm.maven.core.entity.Role;

import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public interface RoleDao {
    /**
     *  find role info by role id
     * @param id role id
     * @return Role role
     */
     Role findRoleById(String id);

    /**
     *  get role list to manage
     * @param query  query map
     * @return List <Role>
     */
    List<Role> roleList(Map<String,Object> query);

    /**
     * get total role
     * @param query query map
     * @return total
     */
    Long roleTotal(Map<String , Object> query);
    /**
     *  delete role
     * @param id role id
     */
    void deleteRole(String id);

    /**
     *  add role
     * @param role Role object
     */
    void addRole(Role role);

    /**
     *  update role
     * @param role Role object
     */
    void updateRole(Role role);
}
