package com.microSerivice.Orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

/*@Entity
@Table(name = "t_Orders")*/
@Document(value = "t_Orders")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Order {
   @Id
    private String id;
    private String orderNumber;
/*    @OneToMany(cascade = CascadeType.ALL)*/
    private List<OrderLineItems>OrderLineItemsList;
}