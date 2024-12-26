package com.eCommerce.cart.controller;

import java.util.Optional;
import com.eCommerce.cart.dto.CartDto;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartController {

  @Autowired
  private CartService cartService;

  @PostMapping
  public ResponseEntity<Cart> createCart() {
    Cart cart = cartService.createCart();
    return ResponseEntity.ok(cart);
  }

  @PostMapping("/addProductToCart")
  public ResponseEntity<Void> addProductToCart(@RequestBody CartDto request) {
    cartService.addProductToCart(request.getIdCart(), request.getIdProduct(), request.getQuantity());
    return ResponseEntity.ok().build();
  }

  @PutMapping("/updateCart")
  public ResponseEntity<Void> updateProductQuantity(@RequestBody CartDto request) {
    cartService.updateProductQuantity(request.getIdCart(), request.getIdProduct(), request.getQuantity());
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{idCart}")
  public ResponseEntity<Cart> getCart(@PathVariable String idCart) {
    Optional<Cart> cart = cartService.getCart(idCart);
    return ResponseEntity.ok(cart.get());
  }

  @DeleteMapping("/{idCart}")
  public ResponseEntity<Void> deleteCart(@PathVariable String idCart) {
    cartService.deleteCart(idCart);
    return ResponseEntity.ok().build();
  }

}
