package com.eCommerce.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDtoIn {

  private String idCart;
  private String idProduct;
  private int    quantity;
}
