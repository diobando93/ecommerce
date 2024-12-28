package com.eCommerce.orders.model;

import com.eCommerce.cart.model.Cart;
import com.eCommerce.product.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Orders {

  @EmbeddedId
  private OrdersPk orderPk;

  @ManyToOne
  @MapsId("idCart")
  @JoinColumn(name = "id_cart", nullable = false)
  private Cart     cart;

  @ManyToOne
  @MapsId("idProduct")
  @JoinColumn(name = "id_product", nullable = false)
  private Product  product;

  @Column(name = "quantity", nullable = false)
  private int      quantity;

  @Column(name = "subTotal", nullable = false)
  private double   subTotal;

}
