package com.example.demo.service;

import com.example.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

@Service
public class UserService {
    
    // 模拟用户数据库，实际项目中应该使用数据库存储
    private Map<String, User> users = new HashMap<>();
    
    @PostConstruct
    public void init() {
        // 初始化一些测试用户
        users.put("admin", new User("admin", "123456"));
        users.put("user", new User("user", "123456"));
    }
    
    /**
     * 验证用户登录
     * @param username 用户名
     * @param password 密码
     * @return 如果验证成功返回用户对象，否则返回null
     */
    public User authenticate(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }
    
    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户对象
     */
    public User getUserByUsername(String username) {
        return users.get(username);
    }
}