package com.example.shop.api.v1;

import com.example.shop.core.annotation.ScopeLevel;
import com.example.shop.service.WxPaymentService;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
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
//        返回的数据格式
//        app_id: "wxdcb0da10fa48b2d0"
//        nonce_str: "1589356667849"
//        package_value: "prepay_id=wx13155747046342ee3dd8d6231667978000"
//        pay_sign: "344D08B3D2CA877A17A3D7DF0838D953"
//        sign_type: "MD5"
//        time_stamp: "1589356667"
        return wxPaymentService.preOrder(oid);
    }

    /**
     * 支付成功后微信会调用该接口，更新订单状态
     */
    @PostMapping("/pay/order/notify")
    public String wxNotify(@RequestBody String xmlData) throws WxPayException {
        WxPayOrderNotifyResult result = wxPayService.parseOrderNotifyResult(xmlData);
        try {
            this.wxPaymentService.dealOrder(result.getOutTradeNo());
        } catch (Exception e) {
            return WxPayNotifyResponse.fail("失败");
        }
        System.out.println(result);
        return WxPayNotifyResponse.success("成功");
    }
}
