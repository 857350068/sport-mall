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
        // 1. 创建管理员用户（同时检查用户名和邮箱唯一性）
        String adminUsername = "admin";
        String adminEmail = "admin@sportmall.com";
        if (!userRepository.existsByUsername(adminUsername) && !userRepository.existsByEmail(adminEmail)) {
            User admin = new User();
            admin.setUsername(adminUsername);
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setEmail(adminEmail);
            admin.setRealName("系统管理员");
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(UserStatus.ACTIVE);
            userRepository.save(admin);
            System.out.println("✅ 管理员用户创建成功: " + adminUsername + " / 123456");
        } else {
            System.out.println("ℹ️ 管理员用户已存在，跳过创建");
        }

        // 2. 创建基础测试用户（同时检查用户名和邮箱唯一性）
        String testUsername = "test";
        String testEmail = "test@sportmall.com";
        if (!userRepository.existsByUsername(testUsername) && !userRepository.existsByEmail(testEmail)) {
            User testUser = new User();
            testUser.setUsername(testUsername);
            testUser.setPassword(passwordEncoder.encode("123456"));
            testUser.setEmail(testEmail);
            testUser.setRealName("测试用户");
            testUser.setPhone("13800138000");
            testUser.setRole(UserRole.USER);
            testUser.setStatus(UserStatus.ACTIVE);
            userRepository.save(testUser);
            System.out.println("✅ 基础测试用户创建成功: " + testUsername + " / 123456");
        } else {
            System.out.println("ℹ️ 基础测试用户已存在，跳过创建");
        }

        // 3. 创建更多测试用户（检查邮箱唯一性，邮箱是数据库唯一约束的核心）
        createTestUsers();
    }

    /**
     * 创建更多测试用户（修复邮箱重复问题）
     */
    private void createTestUsers() {
        String[] usernames = {"user1", "user2", "user3", "user4", "user5"};
        String[] realNames = {"张三", "李四", "王五", "赵六", "钱七"};
        String[] emails = {"user1@test.com", "user2@test.com", "user3@test.com", "user4@test.com", "user5@test.com"};

        for (int i = 0; i < usernames.length; i++) {
            // 关键修改：检查邮箱是否已存在（数据库唯一约束的字段）
            if (!userRepository.existsByEmail(emails[i])) {
                User user = new User();
                user.setUsername(usernames[i]);
                user.setPassword(passwordEncoder.encode("123456"));
                user.setEmail(emails[i]);
                user.setRealName(realNames[i]);
                user.setPhone("1380013800" + i);
                user.setRole(UserRole.USER);
                user.setStatus(UserStatus.ACTIVE);
                userRepository.save(user);
                System.out.println("✅ 创建测试用户: " + usernames[i] + " (" + emails[i] + ")");
            } else {
                System.out.println("ℹ️ 测试用户邮箱已存在，跳过: " + emails[i]);
            }
        }

        System.out.println("✅ 测试用户数据初始化完成");
    }
}
