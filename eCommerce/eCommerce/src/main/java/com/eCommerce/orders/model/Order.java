package com.eCommerce.orders.model;

import com.eCommerce.cart.model.Cart;
import com.eCommerce.product.model.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Order {

  private OrderPk orderPk;

  private Cart    cart;

  private Product product;

  private int     quantity;

  private double  subTotal;

}
