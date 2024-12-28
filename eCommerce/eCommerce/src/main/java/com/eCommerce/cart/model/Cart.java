package com.eCommerce.cart.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import com.eCommerce.orders.model.Orders;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "cart")
public class Cart {

  @Id
  @Column(name = "id_cart", nullable = false, unique = true)
  private String        idCart;

  @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
  private List<Orders>  orders = new ArrayList<>();

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(name = "updated_at", nullable = false)
  private LocalDateTime updatedAt;

  @Column(name = "total_price", nullable = false)
  private Double        totalPrice;

  @Column(name = "status", nullable = false)
  private String        status;

  @PrePersist
  private void generateIdAndTimestamp() {
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

}
