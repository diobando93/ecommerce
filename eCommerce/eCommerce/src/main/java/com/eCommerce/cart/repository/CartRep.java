package com.eCommerce.cart.repository;

import java.time.LocalDateTime;
import java.util.List;
import com.eCommerce.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartRep extends JpaRepository<Cart, String> {

  @Query("SELECT c FROM Cart c WHERE c.updatedAt < :cutoffTime")
  public List<Cart> searchByLastActivityBefore(@Param("cutoffTime") LocalDateTime cutoffTime);

}
