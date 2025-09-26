package com.sportmall.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sportmall.entity.User;
import com.sportmall.enums.UserRole;
import com.sportmall.enums.UserStatus;
import com.sportmall.repository.UserRepository;

/**
 * 数据初始化服务
 * 在应用启动时创建默认数据
 */
@Service
public class DataInitializationService implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        initializeUsers();
    }

    /**
     * 初始化用户数据
     */
    private void initializeUsers() {
        // 检查是否已有管理员用户
        if (!userRepository.existsByUsername("admin")) {
            // 创建管理员用户
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setEmail("admin@sportmall.com");
            admin.setRealName("系统管理员");
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            userRepository.save(admin);
            
            System.out.println("✅ 管理员用户创建成功: admin / 123456");
        }

        // 检查是否已有测试用户
        if (!userRepository.existsByUsername("test")) {
            // 创建测试用户
            User testUser = new User();
            testUser.setUsername("test");
            testUser.setPassword(passwordEncoder.encode("123456"));
            testUser.setEmail("test@sportmall.com");
            testUser.setRealName("测试用户");
            testUser.setPhone("13800138000");
            testUser.setRole(UserRole.USER);
            testUser.setStatus(UserStatus.ACTIVE);
            userRepository.save(testUser);
            
            System.out.println("✅ 测试用户创建成功: test / 123456");
        }

        // 创建更多测试用户
        createTestUsers();
    }

    /**
     * 创建更多测试用户
     */
    private void createTestUsers() {
        String[] usernames = {"user1", "user2", "user3", "user4", "user5"};
        String[] realNames = {"张三", "李四", "王五", "赵六", "钱七"};
        String[] emails = {"user1@test.com", "user2@test.com", "user3@test.com", "user4@test.com", "user5@test.com"};
        
        for (int i = 0; i < usernames.length; i++) {
            if (!userRepository.existsByUsername(usernames[i])) {
                User user = new User();
                user.setUsername(usernames[i]);
                user.setPassword(passwordEncoder.encode("123456"));
                user.setEmail(emails[i]);
                user.setRealName(realNames[i]);
                user.setPhone("1380013800" + i);
                user.setRole(UserRole.USER);
                user.setStatus(UserStatus.ACTIVE);
                userRepository.save(user);
            }
        }
        
        System.out.println("✅ 测试用户数据初始化完成");
    }
}