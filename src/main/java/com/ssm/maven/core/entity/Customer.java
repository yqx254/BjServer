package com.ssm.maven.core.entity;

import java.io.Serializable;

/**
 * @author fstar
 */
public class Customer implements Serializable {
        private String id;
        private String name;
        private String mobile;
        private Long date;
        private String dateStr;
        private String require;
        private int empId;
        private String empName;
        private Long createdAt;
        private Long updatedAt;
        private Long deletedAt;
        private String keyword;
        private int deleteFlag;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile() {
        return mobile;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public Long getDate() {
        return date;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public int getEmpId() {
        return empId;
    }

    public void setCreatedAt(Long createdAt) {
        this.createdAt = createdAt;
    }

    public Long getCreatedAt() {
        return createdAt;
    }

    public void setDeletedAt(Long deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Long getDeletedAt() {
        return deletedAt;
    }

    public void setUpdatedAt(Long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getUpdatedAt() {
        return updatedAt;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setRequire(String require) {
        this.require = require;
    }

    public String getRequire() {
        return require;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpName() {
        return empName;
    }

    public void setDateStr(String dateStr) {
        this.dateStr = dateStr;
    }

    public String getDateStr() {
        return dateStr;
    }
}
