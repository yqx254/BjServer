package com.ssm.maven.core.dao;

import com.ssm.maven.core.entity.Employee;


import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Administrator
 */
public interface EmployeeDao extends Serializable {
    /**
     *  返回目标数据
     * @param map 暂时不明
     * @return 返回雇员List
     */
    public List<Employee> findEmployee(Map<String,Object> map);

    /**
     *  查询总数
     * @param map 暂时不明
     * @return 返回总数
     */
    public Long getTotalEmployee(Map<String, Object> map);

    /**
     * 新增雇员
     * @param employee 上传的雇员对象
     * @return int
     */
    public int addEmployee(Employee employee);

    /**
     * 编辑雇员
     * @param employee 上传的雇员对象
     * @return int
     */
    public int updateEmployee(Employee employee);
    /**
     * 查找雇员
     * @param id 前端上传的ID
     * @return 查询结果：雇员对象
     */
    public Employee findEmployeeById(String id);

    /**
     * 删除雇员
     * @param id 雇员ID
     * @return 操作结果
     */
    public int deleteEmployee(String id);

    /**
     *  获取经纪人姓名
     * @param id 经纪人ID
     * @return String 经纪人姓名
     */
    String findEmpName(String id);
}
