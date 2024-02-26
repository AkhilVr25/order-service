package com.microSerivice.Orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/*@Entity
@Table(name = "t_order_Line_items")*/
@Document(value = "t_order_Line_items")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItems {

    private String id;
    private String skuCode;
    private BigDecimal price;
    private Integer quantity;

}
