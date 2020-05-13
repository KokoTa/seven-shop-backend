package com.example.shop.service;

import com.example.shop.core.local.LocalUser;
import com.example.shop.exception.http.ForbiddenException;
import com.example.shop.exception.http.NotFoundException;
import com.example.shop.exception.http.ParameterException;
import com.example.shop.model.Order;
import com.example.shop.repository.OrderRepository;
import com.example.shop.util.CommonUtil;
import com.example.shop.util.HttpRequestProxy;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.service.WxPayService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class WxPaymentService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WxPayService wxPayService;

    @Value("${wx.notify_url}")
    private String notifyUrl;


    public WxPayMpOrderResult preOrder(Long oid) {
        // 检查用户订单是否存在
        Long uid = LocalUser.getUser().getId();
        Order order = orderRepository.findFirstByUserIdAndId(uid, oid).orElseThrow(() -> new NotFoundException(50012));

        // 订单是否合法
        if (order.isValid()) throw new ForbiddenException(50013);

        // 生成微信请求对象
        WxPayUnifiedOrderRequest preOrderParams = makePreOrderParams(order);
        WxPayMpOrderResult wxPayUnifiedOrderResult = null;

        try {
            // 调用统一下单接口
            wxPayUnifiedOrderResult = this.wxPayService.createOrder(preOrderParams);
        } catch (Exception e)  {
            throw new ParameterException(50014);
        }

        // 更新订单的预订单ID
        orderService.updateOrderPrePayId(order.getId(), wxPayUnifiedOrderResult.getPackageValue());

        return wxPayUnifiedOrderResult;
    }

    /**
     * 组装提交给微信的参数
     * @param order 订单信息
     * @return
     */
    private WxPayUnifiedOrderRequest makePreOrderParams(Order order) {
        WxPayUnifiedOrderRequest map = new WxPayUnifiedOrderRequest();

        // 模拟提交
        map.setOutTradeNo("123123");
        map.setBody("test body");
        map.setDeviceInfo("test device");
        map.setFeeType("CNY");
        map.setTradeType("JSAPI");
        map.setTotalFee(CommonUtil.yuanToFenPlainString(new BigDecimal("0.01")));
        map.setOpenid("omMmwwXNMDb4_s4rNk2_-xr2j33o");
        map.setSpbillCreateIp(HttpRequestProxy.getRemoteRealIp());
        map.setNotifyUrl(notifyUrl);

        return map;
    }
}
