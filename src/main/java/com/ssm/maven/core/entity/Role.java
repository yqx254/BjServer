package com.ssm.maven.core.entity;

import java.io.Serializable;

/**
 * @author Administrator
 */
public class Role implements Serializable {
    private String roleId;
    private String roleName;
    private long createdAt;
    private long updatedAt;
    private int deleteFlag;
    private long deletedAt;

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeletedAt(long deletedAt) {
        this.deletedAt = deletedAt;
    }

    public long getDeletedAt() {
        return deletedAt;
    }
}
