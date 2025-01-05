package com.eCommerce.product.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import com.eCommerce.product.model.Product;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRep {

  private final ConcurrentHashMap<String, Product> storage = new ConcurrentHashMap<>();

  public Product save(Product product) {
    product.generateId();
    storage.put(product.getIdProduct(), product);
    return product;
  }

  public Optional<Product> findById(String id) {
    return Optional.ofNullable(storage.get(id));
  }

  public List<Product> findAll() {
    return new ArrayList<>(storage.values());
  }

  public void deleteById(String id) {
    storage.remove(id);
  }

  public boolean existsById(String id) {
    return storage.containsKey(id);
  }

}
