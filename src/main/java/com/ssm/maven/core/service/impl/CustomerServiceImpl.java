package com.ssm.maven.core.service.impl;

import com.ssm.maven.core.dao.CustomerDao;
import com.ssm.maven.core.entity.Customer;
import com.ssm.maven.core.service.CustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author fstar
 */
@Service("CustomerService")
public class CustomerServiceImpl implements CustomerService {
    @Resource
    private CustomerDao customerDao;
    /**
     * 获取客户列表
     *
     * @param map 查询条件
     * @return 客户集合
     */
    @Override
    public List<Customer> customerList(Map<String, Object> map) {
        return customerDao.customerList(map);
    }

    /**
     * 获取客户总数
     *
     * @param map 查询条件
     * @return 客户总数
     */
    @Override
    public Long customerTotal(Map<String, Object> map) {
        return customerDao.customerTotal(map);
    }

    /**
     * 查询指定客户
     *
     * @param id 用户ID
     * @return 用户对象
     */
    @Override
    public Customer findCustomer(String id) {
        return customerDao.findCustomer(id);
    }

    /**
     * 新增客户
     *
     * @param customer 客户对象
     */
    @Override
    public void addCustomer(Customer customer) {
        customerDao.addCustomer(customer);
    }

    /**
     * 更新客户
     *
     * @param customer 客户对象
     */
    @Override
    public void updateCustomer(Customer customer) {
        customerDao.updateCustomer(customer);
    }

    /**
     * 删除客户
     *
     * @param id 客户ID
     */
    @Override
    public void deleteCustomer(String id) {
        customerDao.deleteCustomer(id);
    }
}
