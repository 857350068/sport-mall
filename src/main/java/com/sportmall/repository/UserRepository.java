package com.sportmall.repository;

import com.sportmall.entity.User;
import com.sportmall.enums.UserRole;
import com.sportmall.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 用户Repository接口
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 根据用户名查找用户（未删除）
     */
    @Query("SELECT u FROM User u WHERE u.username = :username AND u.deleted = false")
    Optional<User> findByUsername(@Param("username") String username);

    /**
     * 根据邮箱查找用户（未删除）
     */
    @Query("SELECT u FROM User u WHERE u.email = :email AND u.deleted = false")
    Optional<User> findByEmail(@Param("email") String email);

    /**
     * 根据手机号查找用户（未删除）
     */
    @Query("SELECT u FROM User u WHERE u.phone = :phone AND u.deleted = false")
    Optional<User> findByPhone(@Param("phone") String phone);

    /**
     * 根据重置令牌查找用户
     */
    @Query("SELECT u FROM User u WHERE u.resetToken = :resetToken AND u.deleted = false")
    Optional<User> findByResetToken(@Param("resetToken") String resetToken);

    /**
     * 检查用户名是否存在（未删除）
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.username = :username AND u.deleted = false")
    boolean existsByUsername(@Param("username") String username);

    /**
     * 检查邮箱是否存在（未删除）
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.email = :email AND u.deleted = false")
    boolean existsByEmail(@Param("email") String email);

    /**
     * 检查手机号是否存在（未删除）
     */
    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.phone = :phone AND u.deleted = false")
    boolean existsByPhone(@Param("phone") String phone);

    /**
     * 根据角色查找用户（未删除）
     */
    @Query("SELECT u FROM User u WHERE u.role = :role AND u.deleted = false")
    List<User> findByRole(@Param("role") UserRole role);

    /**
     * 根据状态查找用户（未删除）
     */
    @Query("SELECT u FROM User u WHERE u.status = :status AND u.deleted = false")
    List<User> findByStatus(@Param("status") UserStatus status);

    /**
     * 分页查询所有用户（未删除）
     */
    @Query("SELECT u FROM User u WHERE u.deleted = false")
    Page<User> findAllActive(Pageable pageable);

    /**
     * 根据关键词搜索用户（用户名、邮箱、真实姓名）
     */
    @Query("SELECT u FROM User u WHERE u.deleted = false AND " +
           "(u.username LIKE %:keyword% OR u.email LIKE %:keyword% OR u.realName LIKE %:keyword%)")
    Page<User> searchUsers(@Param("keyword") String keyword, Pageable pageable);

    /**
     * 根据多个条件查询用户
     */
    @Query("SELECT u FROM User u WHERE u.deleted = false " +
           "AND (:role IS NULL OR u.role = :role) " +
           "AND (:status IS NULL OR u.status = :status) " +
           "AND (:keyword IS NULL OR u.username LIKE %:keyword% OR u.email LIKE %:keyword% OR u.realName LIKE %:keyword%)")
    Page<User> findByConditions(@Param("role") UserRole role, 
                               @Param("status") UserStatus status, 
                               @Param("keyword") String keyword, 
                               Pageable pageable);

    /**
     * 统计各角色用户数量
     */
    @Query("SELECT u.role, COUNT(u) FROM User u WHERE u.deleted = false GROUP BY u.role")
    List<Object[]> countByRole();

    /**
     * 统计各状态用户数量
     */
    @Query("SELECT u.status, COUNT(u) FROM User u WHERE u.deleted = false GROUP BY u.status")
    List<Object[]> countByStatus();
}