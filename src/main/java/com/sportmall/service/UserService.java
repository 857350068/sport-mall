package com.sportmall.service;

import com.sportmall.entity.User;
import com.sportmall.enums.UserRole;
import com.sportmall.enums.UserStatus;
import com.sportmall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户服务类
 * 实现Spring Security的UserDetailsService接口
 */
@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Spring Security用户认证
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
    }

    /**
     * 用户注册
     */
    public User register(String username, String password, String email) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("用户名已存在");
        }
        
        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("邮箱已存在");
        }

        // 创建用户
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(email);
        user.setRole(UserRole.USER);
        user.setStatus(UserStatus.ACTIVE);

        return userRepository.save(user);
    }

    /**
     * 创建管理员用户
     */
    public User createAdmin(String username, String password, String email, String realName) {
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("用户名已存在");
        }
        
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("邮箱已存在");
        }

        User admin = new User();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setEmail(email);
        admin.setRealName(realName);
        admin.setRole(UserRole.ADMIN);
        admin.setStatus(UserStatus.ACTIVE);

        return userRepository.save(admin);
    }

    /**
     * 用户登录（更新最后登录时间）
     */
    public void updateLastLoginTime(String username) {
        userRepository.findByUsername(username).ifPresent(user -> {
            user.updateLastLoginTime();
            userRepository.save(user);
        });
    }

    /**
     * 修改密码
     */
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = getUserById(userId);
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new IllegalArgumentException("原密码错误");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    /**
     * 生成密码重置令牌
     */
    public void generateResetToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("邮箱不存在"));

        user.generateResetToken();
        userRepository.save(user);
        
        // TODO: 发送重置密码邮件
        System.out.println("密码重置链接: /reset-password?token=" + user.getResetToken());
    }

    /**
     * 通过重置令牌重置密码
     */
    public void resetPassword(String token, String newPassword) {
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new IllegalArgumentException("重置令牌无效"));

        if (!user.isResetTokenValid()) {
            throw new IllegalArgumentException("重置令牌已过期");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.clearResetToken();
        userRepository.save(user);
    }

    /**
     * 根据ID获取用户
     */
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .filter(user -> !user.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
    }

    /**
     * 根据用户名获取用户
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * 根据用户名获取用户（直接返回User对象）
     */
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
    }

    /**
     * 根据邮箱获取用户
     */
    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 更新用户信息
     */
    public User updateUser(Long userId, String realName, String phone, String email) {
        User user = getUserById(userId);

        // 检查邮箱是否被其他用户使用
        if (!user.getEmail().equals(email) && userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("邮箱已被其他用户使用");
        }

        // 检查手机号是否被其他用户使用
        if (phone != null && !phone.equals(user.getPhone()) && userRepository.existsByPhone(phone)) {
            throw new IllegalArgumentException("手机号已被其他用户使用");
        }

        user.setRealName(realName);
        user.setPhone(phone);
        user.setEmail(email);

        return userRepository.save(user);
    }

    /**
     * 获取所有用户（分页）
     */
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    /**
     * 分页查询用户
     */
    @Transactional(readOnly = true)
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAllActive(pageable);
    }

    /**
     * 搜索用户
     */
    @Transactional(readOnly = true)
    public Page<User> searchUsers(String keyword, Pageable pageable) {
        return userRepository.searchUsers(keyword, pageable);
    }

    /**
     * 条件查询用户
     */
    @Transactional(readOnly = true)
    public Page<User> findUsersByConditions(UserRole role, UserStatus status, String keyword, Pageable pageable) {
        return userRepository.findByConditions(role, status, keyword, pageable);
    }

    /**
     * 逻辑删除用户
     */
    public void deleteUser(Long userId) {
        User user = getUserById(userId);
        
        // 管理员不能删除自己
        if (user.getRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException("管理员账户不能被删除");
        }

        user.delete();
        userRepository.save(user);
    }

    /**
     * 批量删除用户
     */
    public void deleteUsers(List<Long> userIds) {
        for (Long userId : userIds) {
            deleteUser(userId);
        }
    }

    /**
     * 恢复已删除的用户
     */
    public void restoreUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        
        user.restore();
        userRepository.save(user);
    }

    /**
     * 更改用户状态
     */
    public void changeUserStatus(Long userId, UserStatus status) {
        User user = getUserById(userId);
        user.setStatus(status);
        userRepository.save(user);
    }

    /**
     * 更改用户角色
     */
    public void changeUserRole(Long userId, UserRole role) {
        User user = getUserById(userId);
        user.setRole(role);
        userRepository.save(user);
    }

    /**
     * 获取所有管理员
     */
    @Transactional(readOnly = true)
    public List<User> getAdmins() {
        return userRepository.findByRole(UserRole.ADMIN);
    }

    /**
     * 获取统计信息
     */
    @Transactional(readOnly = true)
    public List<Object[]> getUserStatsByRole() {
        return userRepository.countByRole();
    }

    @Transactional(readOnly = true)
    public List<Object[]> getUserStatsByStatus() {
        return userRepository.countByStatus();
    }
}