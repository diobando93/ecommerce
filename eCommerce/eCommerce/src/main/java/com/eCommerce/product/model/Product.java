package com.eCommerce.product.model;

import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class Product {

  private String idProduct;

  private String name;
  private Double price;

  private String description;
  private int    amount;

  public void generateId() {
    if (idProduct == null || idProduct.isEmpty()) {
      idProduct = UUID.randomUUID().toString();
    }

  }

}
