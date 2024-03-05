package com.microSerivice.Orderservice.service;

import com.microSerivice.Orderservice.dto.InventoryResponse;
import com.microSerivice.Orderservice.dto.OrderLineItemsDto;
import com.microSerivice.Orderservice.dto.OrderRequest;
import com.microSerivice.Orderservice.model.Order;
import com.microSerivice.Orderservice.model.OrderLineItems;
import com.microSerivice.Orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webclientBuilder;
  //  private final Tracer tracer;

    public String placeOrder(OrderRequest orderRequest){
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

     //log.info("calling inventory service");
/*
        Span inventoryServiceLookup= tracer.nextSpan().name("InventoryServiceLookup");
        try (Tracer.SpanInScope inScope = tracer.withSpan(inventoryServiceLookup.start())) {

            inventoryServiceLookup.tag("call", "inventory service");

*/

            // Call the inventory service , and place order if the product is in stock


            InventoryResponse[] inventoryResponseArray = webclientBuilder.build().get()
                    .uri("http://inventory-service/api/inventory",
                            uriBuilder ->
                                    uriBuilder.queryParam("skuCode", skuCodes).build())
                    .retrieve()
                    .bodyToMono(InventoryResponse[].class)
                    .block();
            boolean allProductsInStock = Arrays.stream(inventoryResponseArray)
                    .allMatch(InventoryResponse::isInStock);

            if (allProductsInStock) {
                orderRepository.save(order);
                return "Order place Successfully";
            } else {
                throw new IllegalArgumentException("Product is not in Stock , Please try again later ");
            }
  /*      } finally {
            inventoryServiceLookup.end();
        }*/
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItems.getPrice());
        orderLineItems.setQuantity(orderLineItems.getQuantity());
        orderLineItems.setSkuCode(orderLineItems.getSkuCode());
        return  orderLineItems;
    }
}
