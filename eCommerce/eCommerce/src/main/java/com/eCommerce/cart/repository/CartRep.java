package com.eCommerce.cart.repository;

import com.eCommerce.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRep extends JpaRepository<Cart, String> {

}
