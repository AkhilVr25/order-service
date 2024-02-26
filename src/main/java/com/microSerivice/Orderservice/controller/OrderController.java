package com.microSerivice.Orderservice.controller;

import com.microSerivice.Orderservice.dto.OrderRequest;
import com.microSerivice.Orderservice.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    public String placeOrder (@RequestBody OrderRequest orderRequest){
        orderService.placeOrder(orderRequest);
        return "Order placed Successfully";
    }

}
