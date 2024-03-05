package com.microSerivice.Orderservice.controller;

import com.microSerivice.Orderservice.dto.OrderRequest;
import com.microSerivice.Orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/Order")
@AllArgsConstructor
public class OrderController {

    private final OrderService orderService;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name = "inventory",fallbackMethod = "fallbackMethod")
   @TimeLimiter(name = "inventory")
   @Retry(name = "inventory")
   public CompletableFuture<String>placeOrder(@RequestBody OrderRequest orderRequest){
       return CompletableFuture.supplyAsync(()-> orderService.placeOrder(orderRequest));
    }

  //  public String placeOrder(@RequestBody OrderRequest orderRequest){
   //        return orderService.placeOrder(orderRequest);

 //          }

    public CompletableFuture<String> fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
        return CompletableFuture .supplyAsync(()->"Something Went wrong , please the order after some time!");
    }

}
