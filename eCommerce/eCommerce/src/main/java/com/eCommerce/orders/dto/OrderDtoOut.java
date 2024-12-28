package com.eCommerce.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDtoOut {

  private String idProduct;
  private int    quantity;
  private Double subTotal;

}
