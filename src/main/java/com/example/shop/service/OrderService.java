package com.example.shop.service;

import com.example.shop.core.enumeration.OrderStatus;
import com.example.shop.core.local.LocalUser;
import com.example.shop.core.money.IMoney;
import com.example.shop.dto.OrderDTO;
import com.example.shop.dto.SkuInfoDTO;
import com.example.shop.exception.http.NotFoundException;
import com.example.shop.exception.http.ParameterException;
import com.example.shop.logic.CouponChecker;
import com.example.shop.logic.OrderChecker;
import com.example.shop.model.*;
import com.example.shop.repository.CouponRepository;
import com.example.shop.repository.OrderRepository;
import com.example.shop.repository.SkuRepository;
import com.example.shop.repository.UserCouponRepository;
import com.example.shop.util.CommonUtil;
import com.example.shop.util.OrderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private SkuService skuService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private UserCouponRepository userCouponRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private SkuRepository skuRepository;

    @Autowired
    private IMoney upRound;

    @Value("${condition.max-sku-limit}")
    private Integer maxSkuLimit;

    @Value("${condition.pay-time-limit}")
    private Integer payTimeLimit;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 订单数据是否正确
     */
    public OrderChecker isOk(Long uid, OrderDTO orderDTO) {
        // 总价如果 <=0 则报错
        if (orderDTO.getFinalTotalPrice().compareTo(new BigDecimal("0")) <= 0) {
            throw new ParameterException(50001);
        }

        List<Long> skuIdList = orderDTO.getSkuInfoList().stream().map(SkuInfoDTO::getId).collect(Collectors.toList());
        List<Sku> skuList = skuService.getSkuListByIds(skuIdList);

        Long couponId = orderDTO.getCouponId();
        CouponChecker couponChecker = null;

        if (couponId != null) {
            // 报错表示没有这张优惠券
            Coupon coupon = couponRepository.findById(couponId).orElseThrow(() -> new NotFoundException(40003));
            // 报错表示用户没有这张优惠券
            userCouponRepository.findFirstByUserIdAndCouponIdAndOrderIdIsNull(uid, couponId)
                    .orElseThrow(() -> new NotFoundException(40007));
            // 优惠券校验器
            couponChecker = new CouponChecker(coupon, upRound);
        }

        // 订单校验器
        OrderChecker orderChecker = new OrderChecker(orderDTO, skuList, couponChecker, maxSkuLimit);
        orderChecker.isOK();
        return orderChecker;
    }

    /**
     * 创建订单
     */
    @Transactional
    public Long placeOrder(Long uid, OrderDTO orderDTO, OrderChecker orderChecker) {
        String orderNo = OrderUtil.makeOrderNo(); // 生成订单号
        Date placedTime = Calendar.getInstance().getTime(); // 这里不能以数据库生成的 createTime 作为订单创建时间，会有误差
        Date expiredTime = CommonUtil.getExpiredTime(payTimeLimit).getTime(); // 订单过期时间

        // 构建订单
        Order order = Order.builder().orderNo(orderNo).totalPrice(orderDTO.getTotalPrice())
                .finalTotalPrice(orderDTO.getFinalTotalPrice()).userId(uid).totalCount(orderChecker.getTotalCount())
                .snapImg(orderChecker.getSnapImg()).snapTitle(orderChecker.getSnapTitle())
                .status(OrderStatus.UNPAID.value()).placedTime(placedTime).expiredTime(expiredTime).build();

        // json 数据不能直接用构造器赋值，要手动触发 set 来进行数据格式的转换
        order.setSnapItems(orderChecker.getOrderSkuList());
        order.setSnapAddress(orderDTO.getAddress());

        // 创建订单
        orderRepository.save(order);
        // 减去库存
        reduceStock(orderChecker);
        // 核销优惠券
        Long couponId = -1L;
        if (orderDTO.getCouponId() != null) {
            changeCouponStatus(orderDTO.getCouponId(), order.getId(), uid);
            couponId = orderDTO.getCouponId();
        }
        // 信息加入到延迟消息队列（未付款，时间到后要归还库存和优惠券）
        sendToRedis(uid, order.getId(), couponId);

        return order.getId();
    }

    /**
     * 获取未支付订单
     * 
     * @param page
     * @param size
     * @return
     */
    public Page<Order> getUnpaid(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Long uid = LocalUser.getUser().getId();
        Date now = new Date();
        return orderRepository.findAllByExpiredTimeGreaterThanAndStatusAndUserId(now, OrderStatus.UNPAID.value(), uid,
                pageable);
    }

    /**
     * 根据状态查询订单（不提供未支付订单的查询）
     * 
     * @param status
     * @param page
     * @param size
     * @return
     */
    public Page<Order> getByStatus(Integer status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createTime").descending());
        Long uid = LocalUser.getUser().getId();

        if (status == OrderStatus.UNPAID.value())
            throw new ParameterException(50011);
        if (status == OrderStatus.All.value())
            return orderRepository.findByUserId(uid, pageable);
        return orderRepository.findByUserIdAndStatus(uid, status, pageable);
    }

    /**
     * 获取订单详情
     * @param id 订单ID
     * @return
     */
    public Order getOrderDetail(Long id) {
        Long uid = LocalUser.getUser().getId();
        Optional<Order> order = orderRepository.findFirstByUserIdAndId(uid, id);
        return order.orElseThrow(() -> new ParameterException(50012));
    }

    /**
     * 更新订单的预订单ID
     * @param orderId
     * @param prePayId
     */
    public void updateOrderPrePayId(Long orderId, String prePayId) {
        Optional<Order> order = orderRepository.findById(orderId);
        order.ifPresent(o -> {
            o.setPrepayId(prePayId);
            orderRepository.save(o);
        });
        order.orElseThrow(() -> new ParameterException(50015));
    }

    /**
     * 减库存
     * @param orderChecker 订单检查器
     */
    private void reduceStock(OrderChecker orderChecker) {
        List<OrderSku> orderSkuList = orderChecker.getOrderSkuList();
        for (OrderSku orderSku : orderSkuList) {
            Integer result = skuRepository.reduceStock(orderSku.getId(), orderSku.getCount());
            if (result != 1)
                throw new ParameterException(50003);
        }
    }

    /**
     * 改变优惠券状态为已使用
     * @param couponId 优惠券ID
     * @param orderId 订单ID
     * @param uid 用户ID
     */
    private void changeCouponStatus(Long couponId, Long orderId, Long uid) {
        Integer result = userCouponRepository.changeCouponStatus(couponId, orderId, uid);
        if (result != 1)
            throw new ParameterException(50006);
    }

    /**
     * 把订单信息发送给 redis
     * @param oid 订单ID
     * @param uid 用户ID
     * @param couponId 优惠券ID
     */
    private void sendToRedis(Long uid, Long oid, Long couponId) {
        // 主要存储 key，value 值随意
        String key = uid.toString() + "," + oid.toString() + "," + couponId.toString();
        String value = "1";
        // 这里报错可以用 try catch 包裹起来，因为使用了事务，这里如果抛出错误，会让用户无法下单，会损失不少订单量
        // 且由于这里的错误不影响下单主体逻辑，因此错误可以直接本地消化掉，记入日志即可
        // 另外可以设置一个定时器，每天凌晨对数据库里所有的订单进行扫描，更新每个订单的状态，防止 redis 宕机导致订单状态未更新的问题
        stringRedisTemplate.opsForValue().set(key, value, payTimeLimit, TimeUnit.SECONDS);
    }
}
