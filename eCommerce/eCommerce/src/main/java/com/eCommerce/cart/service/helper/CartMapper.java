package com.eCommerce.cart.service.helper;

import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.orders.dto.OrderDtoOut;
import com.eCommerce.orders.model.Order;
import org.springframework.stereotype.Component;

@Component
public class CartMapper {

  public CartDtoOut toDto(Cart cart) {
    CartDtoOut cartDtoOut = new CartDtoOut();
    cartDtoOut.setIdCart(cart.getIdCart());
    cartDtoOut.setOrders(cart.getOrders().stream()
        .map(order -> new OrderDtoOut(order.getProduct().getIdProduct(), order.getQuantity(), order.getSubTotal())).toList());
    cartDtoOut.setTotalPrice(cart.getOrders().stream().mapToDouble(Order::getSubTotal).sum());
    cartDtoOut.setCreatedAt(cart.getCreatedAt());
    cartDtoOut.setUpdatedAt(cart.getUpdatedAt());
    cartDtoOut.setStatus(cart.getStatus());
    return cartDtoOut;
  }

}
