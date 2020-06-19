package com.ssm.maven.core.dao;

import java.util.List;
import java.util.Map;

import com.ssm.maven.core.entity.User;
import org.springframework.stereotype.Repository;

/**
 * @author 1034683568@qq.com
 * @project_name ssm-maven
 * @date 2017-3-1
 */
@Repository
public interface UserDao {

    /**
     * 登录
     *
     * @param user
     * @return
     */
    public User login(User user);

    /**
     * 查找用户列表
     *
     * @param map
     * @return
     */
    public List<User> findUsers(Map<String, Object> map);

    /**
     * @param map
     * @return
     */
    public Long getTotalUser(Map<String, Object> map);

    /**
     * 实体修改
     *
     * @param user
     * @return
     */
    public int updateUser(User user);

    /**
     * 添加用户
     *
     * @param user
     * @return
     */
    public int addUser(User user);

    /**
     * 删除用户
     *
     * @param id
     * @return
     */
    public int deleteUser(Integer id);

    /**
     *  获取加盐串，用于登录验证
     * @param username 用户名
     * @return 加盐串
     */
    public String getSalt(String username);

    /**
     *  获取用户姓名，用于新建用户时的重复校验
     *  扩展一下就可以用作他途
     * @param username 用户名
     * @return 用户名
     */
    String getUser(String username);
}
