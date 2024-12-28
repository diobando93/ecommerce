package com.eCommerce.cart.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.eCommerce.orders.dto.OrderDtoOut;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDtoOut {

  private String            idCart;
  private List<OrderDtoOut> orders;
  private Double            totalPrice;
  private LocalDateTime     createdAt;
  private LocalDateTime     updatedAt;
  private String            status;

}
