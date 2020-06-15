package com.ssm.maven.core.service;

import java.util.List;
import java.util.Map;

import com.ssm.maven.core.entity.User;

/**
 * @author 1034683568@qq.com
 * @project_name ssm-maven
 * @date 2017-3-1
 */
public interface UserService {

    /**
     * @param user
     * @return
     */
    public User login(User user);

    /**
     * @param map
     * @return
     */
    public List<User> findUser(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    public Long getTotalUser(Map<String, Object> map);

    /**
     * @param user
     * @return
     */
    public int updateUser(User user);

    /**
     * @param user
     * @return
     */
    public int addUser(User user);

    /**
     * @param id
     * @return
     */
    public int deleteUser(Integer id);

    /**
     * 密码加盐
     * @param password 密码
     * @param salt 盐
     * @return 加过盐的密码
     */
    public String saltPwd(String password, String salt);


    /**
     * 生成加盐串
     */
    public String getSalt();
}
