package com.eCommerce.product.model;

import java.util.UUID;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
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

  @EmbeddedId
  private ProductPk productPk;

  private String    name;
  private Double    price;

  private String    description;
  private int       amount;

  @PrePersist
  private void generateId() {
    if (productPk == null) {
      productPk = new ProductPk();
    }
    if (productPk.getIdProduct() == null || productPk.getIdProduct().isEmpty()) {
      productPk.setIdProduct(UUID.randomUUID().toString()); // Generar UUID como String
    }
  }

}
