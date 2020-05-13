package com.example.shop.api.v1;

import com.example.shop.core.annotation.ScopeLevel;
import com.example.shop.service.WxPaymentService;
import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.github.binarywang.wxpay.bean.order.WxPayMpOrderResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;

@RequestMapping("payment")
@RestController
@Validated
public class PaymentController {

    @Autowired
    private WxPaymentService wxPaymentService;

    @Autowired
    private WxPayService wxPayService;

    /**
     * 获取微信的预订单，获取 PrepayId
     * @param oid 订单号
     * @return
     */
    @ScopeLevel
    @PostMapping("/pay/order/{id}")
    public WxPayMpOrderResult preWxOrder(@PathVariable(name = "id") @Positive Long oid) {
        return wxPaymentService.preOrder(oid);
    }

    /**
     * 微信回调，微信发过来的是 xml 数据
     */
    @PostMapping("/pay/order/notify")
    public WxPayOrderNotifyResult wxNotify(@RequestBody String xmlData) throws WxPayException {
        return wxPayService.parseOrderNotifyResult(xmlData);
    }
}
