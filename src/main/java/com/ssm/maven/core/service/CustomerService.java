package com.ssm.maven.core.service;

import com.ssm.maven.core.entity.Customer;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author fstar
 */
public interface CustomerService extends Serializable {
    /**
     *  获取客户列表
     * @param map 查询条件
     * @return 客户集合
     */
    List<Customer> customerList(Map<String, Object> map);

    /**
     *  获取客户总数
     * @param map 查询条件
     * @return 客户总数
     */
    Long customerTotal(Map<String, Object> map);

    /**
     *  查询指定客户
     * @param id 用户ID
     * @return 用户对象
     */
    Customer findCustomer(String id);

    /**
     * 新增客户
     * @param customer 客户对象
     */
    void addCustomer(Customer customer);
    /**
     *  更新客户
     * @param customer 客户对象
     */
    void updateCustomer(Customer customer);

    /**
     *  删除客户
     * @param id 客户ID
     */
    void deleteCustomer(String id);
}
