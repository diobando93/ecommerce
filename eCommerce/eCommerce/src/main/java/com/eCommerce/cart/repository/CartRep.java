package com.eCommerce.cart.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import com.eCommerce.cart.model.Cart;
import org.springframework.stereotype.Repository;

@Repository
public class CartRep {

  private final ConcurrentHashMap<String, Cart> storage = new ConcurrentHashMap<>();

  public Cart save(Cart cart) {
    cart.initVariables();
    storage.put(cart.getIdCart(), cart);
    return cart;
  }

  public Optional<Cart> findById(String id) {
    return Optional.ofNullable(storage.get(id));
  }

  public List<Cart> searchByLastActivityBefore(LocalDateTime cutoffTime) {
    return storage.values().stream().filter(cart -> cart.getUpdatedAt().isBefore(cutoffTime)).collect(Collectors.toList());
  }

  public void deleteById(String id) {
    storage.remove(id);
  }

  public boolean existsById(String id) {
    return storage.containsKey(id);
  }

}
