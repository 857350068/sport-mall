package com.sportmall.service;

import com.sportmall.entity.CartItem;
import com.sportmall.entity.Product;
import com.sportmall.entity.User;
import com.sportmall.repository.CartItemRepository;
import com.sportmall.repository.ProductRepository;
import com.sportmall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * 购物车服务类
 */
@Service
@Transactional
public class CartService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    /**
     * 添加商品到购物车
     */
    public CartItem addToCart(Long userId, Long productId, Integer quantity) {
        User user = userRepository.findById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        Product product = productRepository.findById(productId)
                .filter(p -> !p.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("商品不存在"));

        // 检查库存
        if (!product.canBePurchased(quantity)) {
            throw new IllegalArgumentException("商品库存不足");
        }

        // 检查购物车中是否已存在该商品
        return cartItemRepository.findByUserAndProduct(user, product)
                .map(cartItem -> {
                    // 如果已存在，增加数量
                    cartItem.setQuantity(cartItem.getQuantity() + quantity);
                    return cartItemRepository.save(cartItem);
                })
                .orElseGet(() -> {
                    // 如果不存在，创建新的购物车项
                    CartItem cartItem = new CartItem();
                    cartItem.setUser(user);
                    cartItem.setProduct(product);
                    cartItem.setQuantity(quantity);
                    return cartItemRepository.save(cartItem);
                });
    }

    /**
     * 更新购物车商品数量
     */
    public CartItem updateCartItemQuantity(Long cartItemId, Integer quantity) {
        CartItem cartItem = getCartItemById(cartItemId);

        // 检查库存
        if (!cartItem.getProduct().canBePurchased(quantity)) {
            throw new IllegalArgumentException("商品库存不足");
        }

        cartItem.setQuantity(quantity);
        return cartItemRepository.save(cartItem);
    }

    /**
     * 从购物车中移除商品
     */
    public void removeFromCart(Long cartItemId) {
        CartItem cartItem = getCartItemById(cartItemId);
        cartItemRepository.delete(cartItem);
    }

    /**
     * 根据ID获取购物车项
     */
    @Transactional(readOnly = true)
    public CartItem getCartItemById(Long cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .filter(c -> !c.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("购物车项不存在"));
    }

    /**
     * 获取用户购物车所有商品
     */
    @Transactional(readOnly = true)
    public List<CartItem> getUserCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        return cartItemRepository.findByUser(user);
    }

    /**
     * 获取用户选中的购物车商品
     */
    @Transactional(readOnly = true)
    public List<CartItem> getUserSelectedCartItems(Long userId) {
        User user = userRepository.findById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        return cartItemRepository.findSelectedByUser(user);
    }

    /**
     * 计算购物车总金额
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateCartTotal(Long userId) {
        List<CartItem> cartItems = getUserCartItems(userId);
        BigDecimal total = BigDecimal.ZERO;

        for (CartItem cartItem : cartItems) {
            if (cartItem.getSelected() && cartItem.isAvailable()) {
                total = total.add(cartItem.getSubtotal());
            }
        }

        return total;
    }

    /**
     * 选择/取消选择购物车商品
     */
    public void selectCartItem(Long cartItemId, Boolean selected) {
        CartItem cartItem = getCartItemById(cartItemId);
        cartItem.setSelected(selected);
        cartItemRepository.save(cartItem);
    }

    /**
     * 全选/取消全选购物车商品
     */
    public void selectAllCartItems(Long userId, Boolean selected) {
        List<CartItem> cartItems = getUserCartItems(userId);
        for (CartItem cartItem : cartItems) {
            cartItem.setSelected(selected);
            cartItemRepository.save(cartItem);
        }
    }

    /**
     * 清空购物车
     */
    public void clearCart(Long userId) {
        User user = userRepository.findById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        cartItemRepository.clearByUser(user);
    }

    /**
     * 获取购物车商品数量
     */
    @Transactional(readOnly = true)
    public Long getCartItemCount(Long userId) {
        User user = userRepository.findById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        return cartItemRepository.countByUser(user);
    }
}