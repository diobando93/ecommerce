package com.eCommerce.product.model;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product {

  @Id
  @Column(name = "id_product", nullable = false, unique = true)
  private String idProduct;

  private String name;
  private Double price;

  private String description;
  private int    amount;

  @PrePersist
  private void generateId() {
    if (idProduct == null || idProduct.isEmpty()) {
      idProduct = UUID.randomUUID().toString();
    }

  }

}
