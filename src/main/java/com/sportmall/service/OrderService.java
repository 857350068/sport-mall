package com.sportmall.service;

import com.sportmall.entity.CartItem;
import com.sportmall.entity.Order;
import com.sportmall.entity.OrderItem;
import com.sportmall.entity.Product;
import com.sportmall.entity.User;
import com.sportmall.enums.OrderStatus;
import com.sportmall.repository.OrderRepository;
import com.sportmall.repository.ProductRepository;
import com.sportmall.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 订单服务类
 */
@Service
@Transactional
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private CartService cartService;

    /**
     * 创建订单
     */
    public Order createOrder(Long userId, String receiverName, String receiverPhone, 
                           String receiverAddress, String remark, List<OrderItemRequest> items) {
        User user = userRepository.findById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUser(user);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);
        order.setRemark(remark);
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        // 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemRequest itemRequest : items) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .filter(p -> !p.isDeleted())
                    .orElseThrow(() -> new IllegalArgumentException("商品不存在: " + itemRequest.getProductId()));

            // 检查库存
            if (!product.canBePurchased(itemRequest.getQuantity())) {
                throw new IllegalArgumentException("商品库存不足: " + product.getName());
            }

            BigDecimal itemSubtotal = product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity()));
            totalAmount = totalAmount.add(itemSubtotal);
        }

        order.setTotalAmount(totalAmount);
        order.setActualAmount(totalAmount);
        order.calculateActualAmount();

        // 保存订单
        Order savedOrder = orderRepository.save(order);

        // 创建订单项并减少库存
        for (OrderItemRequest itemRequest : items) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .filter(p -> !p.isDeleted())
                    .orElseThrow(() -> new IllegalArgumentException("商品不存在: " + itemRequest.getProductId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.calculateSubtotal();

            // 减少商品库存
            product.reduceStock(itemRequest.getQuantity());
            productRepository.save(product);
        }

        return savedOrder;
    }

    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * 根据ID获取订单
     */
    @Transactional(readOnly = true)
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .filter(o -> !o.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("订单不存在"));
    }

    /**
     * 根据订单号获取订单
     */
    @Transactional(readOnly = true)
    public Order getOrderByOrderNo(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo);
        if (order == null || order.isDeleted()) {
            throw new IllegalArgumentException("订单不存在");
        }
        return order;
    }

    /**
     * 获取用户订单列表
     */
    @Transactional(readOnly = true)
    public Page<Order> getUserOrders(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        return orderRepository.findByUser(user, pageable);
    }

    /**
     * 根据状态获取用户订单
     */
    @Transactional(readOnly = true)
    public Page<Order> getUserOrdersByStatus(Long userId, OrderStatus status, Pageable pageable) {
        User user = userRepository.findById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        return orderRepository.findByUserAndStatus(user, status, pageable);
    }

    /**
     * 获取所有订单（分页）
     */
    @Transactional(readOnly = true)
    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepository.findAllNotDeleted(pageable);
    }

    /**
     * 多条件查询订单
     */
    @Transactional(readOnly = true)
    public Page<Order> findOrdersByConditions(String orderNo, OrderStatus status, Long userId,
                                            LocalDateTime startDate, LocalDateTime endDate, 
                                            Pageable pageable) {
        return orderRepository.findByConditions(orderNo, status, userId, startDate, endDate, pageable);
    }

    /**
     * 订单支付
     */
    public Order payOrder(Long orderId) {
        Order order = getOrderById(orderId);
        
        if (!order.canBePaid()) {
            throw new IllegalArgumentException("订单状态不允许支付");
        }

        order.pay();
        return orderRepository.save(order);
    }

    /**
     * 订单发货
     */
    public Order shipOrder(Long orderId) {
        Order order = getOrderById(orderId);
        
        if (!order.canBeShipped()) {
            throw new IllegalArgumentException("订单状态不允许发货");
        }

        order.ship();
        return orderRepository.save(order);
    }

    /**
     * 确认收货
     */
    public Order deliverOrder(Long orderId) {
        Order order = getOrderById(orderId);
        
        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new IllegalArgumentException("订单状态不允许确认收货");
        }

        order.deliver();
        return orderRepository.save(order);
    }

    /**
     * 完成订单
     */
    public Order completeOrder(Long orderId) {
        Order order = getOrderById(orderId);
        
        if (!order.canBeCompleted()) {
            throw new IllegalArgumentException("订单状态不允许完成");
        }

        order.complete();
        return orderRepository.save(order);
    }

    /**
     * 取消订单
     */
    public Order cancelOrder(Long orderId, String reason) {
        Order order = getOrderById(orderId);
        
        if (!order.canBeCancelled()) {
            throw new IllegalArgumentException("订单状态不允许取消");
        }

        order.cancel(reason);
        return orderRepository.save(order);
    }

    /**
     * 申请退款
     */
    public Order refundOrder(Long orderId) {
        Order order = getOrderById(orderId);
        
        if (!order.canBeRefunded()) {
            throw new IllegalArgumentException("订单状态不允许退款");
        }

        order.refund();
        return orderRepository.save(order);
    }

    /**
     * 处理超时未支付的订单
     */
    public void processExpiredOrders() {
        LocalDateTime oneHourAgo = LocalDateTime.now().minusHours(1);
        List<Order> expiredOrders = orderRepository.findExpiredOrders(oneHourAgo);

        for (Order order : expiredOrders) {
            try {
                order.autoCancel();
                orderRepository.save(order);
            } catch (Exception e) {
                System.err.println("自动取消订单失败: " + order.getOrderNo() + ", 错误: " + e.getMessage());
            }
        }
    }
    
    /**
     * 从购物车创建订单
     */
    public Order createOrderFromCart(Long userId, String remark) {
        User user = userRepository.findById(userId)
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));

        // 获取用户选中的购物车商品
        List<CartItem> cartItems = cartService.getUserSelectedCartItems(userId);
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("购物车中没有选中的商品");
        }

        // 创建订单
        Order order = new Order();
        order.setOrderNo(generateOrderNo());
        order.setUser(user);
        // 使用用户信息作为默认收货信息
        order.setReceiverName(user.getRealName() != null ? user.getRealName() : user.getUsername());
        order.setReceiverPhone(user.getPhone());
        order.setReceiverAddress("默认地址"); // 实际项目中应该从用户地址管理中获取
        order.setRemark(remark);
        order.setStatus(OrderStatus.PENDING_PAYMENT);

        // 计算总金额
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            
            // 检查库存
            if (!product.canBePurchased(cartItem.getQuantity())) {
                throw new IllegalArgumentException("商品库存不足: " + product.getName());
            }

            BigDecimal itemSubtotal = product.getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            totalAmount = totalAmount.add(itemSubtotal);
        }

        order.setTotalAmount(totalAmount);
        order.setActualAmount(totalAmount);

        // 保存订单
        Order savedOrder = orderRepository.save(order);

        // 创建订单项并减少库存
        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(savedOrder);
            orderItem.setProduct(product);
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setProductPrice(product.getPrice());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getMainImage());
            orderItem.calculateSubtotal();

            // 减少商品库存
            product.reduceStock(cartItem.getQuantity());
            productRepository.save(product);
        }
        
        // 清空购物车中已下单的商品
        for (CartItem cartItem : cartItems) {
            cartService.removeFromCart(cartItem.getId());
        }

        return savedOrder;
    }

    /**
     * 模拟支付处理
     */
    public Order processPayment(String orderId, String paymentMethod) {
        Order order = getOrderByOrderNo(orderId);
        
        if (!order.canBePaid()) {
            throw new IllegalArgumentException("订单状态不允许支付");
        }

        order.pay();
        return orderRepository.save(order);
    }

    /**
     * 删除订单（逻辑删除）
     */
    public void deleteOrder(Long orderId) {
        Order order = getOrderById(orderId);
        order.delete();
        orderRepository.save(order);
    }

    /**
     * 订单项请求类
     */
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;

        public OrderItemRequest() {}

        public OrderItemRequest(Long productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
    }
}