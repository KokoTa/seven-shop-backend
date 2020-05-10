package com.example.shop.api.v1;

import com.example.shop.bo.PageCounter;
import com.example.shop.core.annotation.ScopeLevel;
import com.example.shop.core.local.LocalUser;
import com.example.shop.dto.OrderDTO;
import com.example.shop.logic.OrderChecker;
import com.example.shop.model.Order;
import com.example.shop.service.OrderService;
import com.example.shop.util.CommonUtil;
import com.example.shop.vo.OrderIdVO;
import com.example.shop.vo.OrderPureVO;
import com.example.shop.vo.PagingDozer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Value("${condition.pay-time-limit}")
    private Long payTimeLimit;

    @ScopeLevel
    @PostMapping("")
    public OrderIdVO placeOrder(@RequestBody @Validated OrderDTO orderDTO) {
        Long uid = LocalUser.getUser().getId();
        OrderChecker orderChecker = orderService.isOk(uid, orderDTO);
        Long orderId = orderService.placeOrder(uid, orderDTO, orderChecker);
        return new OrderIdVO(orderId);
    }

    @ScopeLevel
    @GetMapping("/status/unpaid")
    public PagingDozer<Order, OrderPureVO> getUnpaid(@RequestParam(defaultValue = "0") Integer start,
            @RequestParam(defaultValue = "10") Integer count) {
        PageCounter page = CommonUtil.convertToPageParameter(start, count);
        Page<Order> orderPage = orderService.getUnpaid(page.getPageNo(), page.getPageSize());
        PagingDozer<Order, OrderPureVO> pagingDozer = new PagingDozer<>(orderPage, OrderPureVO.class);
        pagingDozer.getItems().forEach((o) -> ((OrderPureVO) o).setPeriod(payTimeLimit));
        return pagingDozer;
    }

    @ScopeLevel
    @GetMapping("/by/status/{status}")
    public PagingDozer<Order, OrderPureVO> getByStatus(@PathVariable Integer status,
            @RequestParam(defaultValue = "0") Integer start, @RequestParam(defaultValue = "10") Integer count) {
        PageCounter page = CommonUtil.convertToPageParameter(start, count);
        Page<Order> orderPage = orderService.getByStatus(status, page.getPageNo(), page.getPageSize());
        PagingDozer<Order, OrderPureVO> pagingDozer = new PagingDozer<>(orderPage, OrderPureVO.class);
        pagingDozer.getItems().forEach((o) -> ((OrderPureVO) o).setPeriod(payTimeLimit));
        return pagingDozer;
    }

    @ScopeLevel
    @GetMapping("/detail/{id}")
    public OrderPureVO getDetailById(@PathVariable Long id) {
        Order order = orderService.getOrderDetail(id);
        OrderPureVO orderPureVO = new OrderPureVO(order, payTimeLimit);
        return orderPureVO;
    }
}
