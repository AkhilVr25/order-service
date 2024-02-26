package com.microSerivice.Orderservice.service;

import com.microSerivice.Orderservice.dto.InventoryResponse;
import com.microSerivice.Orderservice.dto.OrderLineItemsDto;
import com.microSerivice.Orderservice.dto.OrderRequest;
import com.microSerivice.Orderservice.model.Order;
import com.microSerivice.Orderservice.model.OrderLineItems;
import com.microSerivice.Orderservice.repository.OrderRepository;

import io.micrometer.observation.Observation;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient webClient;
    public void placeOrder(OrderRequest orderRequest){
       Order order =  new Order();
       order.setOrderNumber(UUID.randomUUID().toString());

      List<OrderLineItems> orderLineItems =orderRequest.getOrderLineItemsDtoList()
               .stream()
               .map(this::mapToDto)
               .toList();
      order.setOrderLineItemsList(orderLineItems);

     List<String> skuCodes = order.getOrderLineItemsList()
              .stream()
              .map(OrderLineItems:: getSkuCode)
              .toList();

      // Call the inventory service , and place order if the product is in stock


       InventoryResponse[] inventoryResponseArray =  webClient.get()
                        .uri("http://localhost:8082/api/inventory",
                                uriBuilder ->
                                        uriBuilder.queryParam("skuCode",skuCodes).build())
                                .retrieve()
                                        .bodyToMono(InventoryResponse[].class)
                                                .block();
       boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
               .allMatch(InventoryResponse::isInStock);

        if (allProductsInStock) {
            orderRepository.save(order);
        } else {
            throw  new IllegalArgumentException("Product is not in Stock , Please try again later ");
        }
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItems.getPrice());
        orderLineItems.setQuantity(orderLineItems.getQuantity());
        orderLineItems.setSkuCode(orderLineItems.getSkuCode());
        return orderLineItems;
    }
}
