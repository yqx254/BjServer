package com.ssm.maven.core.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.ssm.maven.core.dao.UserDao;
import com.ssm.maven.core.entity.User;
import com.ssm.maven.core.service.UserService;

/**
 * @author 1034683568@qq.com
 * @project_name ssm-maven
 * @date 2017-3-1
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User login(User user) {
        return userDao.login(user);
    }

    @Override
    public List<User> findUser(Map<String, Object> map) {
        return userDao.findUsers(map);
    }

    @Override
    public int updateUser(User user) {
        //防止有人胡乱修改导致其他人无法正常登陆
        if ("admin".equals(user.getUserName())) {
            return 0;
        }
        return userDao.updateUser(user);
    }

    @Override
    public Long getTotalUser(Map<String, Object> map) {
        return userDao.getTotalUser(map);
    }

    @Override
    public int addUser(User user) {
        if (user.getUserName() == null || user.getPassword() == null || getTotalUser(null) > 90) {
            return 0;
        }
        return userDao.addUser(user);
    }

    @Override
    public int deleteUser(Integer id) {
        //防止有人胡乱修改导致其他人无法正常登陆
        if (1 == id) {
            return 0;
        }
        return userDao.deleteUser(id);
    }

    /**
     * 密码加盐
     *
     * @param password 密码
     * @param salt     盐
     * @return 加过盐的密码
     */
    @Override
    public String saltPwd(String password, String salt) {
        return password.substring(0, 3) +
                salt +
                password.substring(3);
    }


    @Override
    public String getSalt(){
        int saltLen = 4;
        Random random = new Random();
        String str="abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder builder = new StringBuilder();
        for(int i = 0;i < saltLen ; i ++){
            int num = random.nextInt(str.length());
            builder.append(str.charAt(num));
        }
        return builder.toString();
    }
}
