package com.eCommerce.cart.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.eCommerce.orders.model.Order;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class Cart {

  private String        idCart;

  private List<Order>   orders = new ArrayList<>();

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private Double        totalPrice;

  private String        status;

  public void initVariables() {
    if (idCart == null || idCart.isEmpty()) {
      idCart = UUID.randomUUID().toString();
    }
    if (createdAt == null && updatedAt == null) {
      createdAt = LocalDateTime.now();
      updatedAt = LocalDateTime.now();
    }
    if (status == null) {
      status = "Created";
    }
    if (totalPrice == null) {
      totalPrice = 0D;
    }

  }

  public void addOrder(Order order) {
    orders.add(order);
  }

  public void removeOrder(Order order) {
    orders.remove(order);
  }

}
