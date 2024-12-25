package com.eCommerce.product.controller;

import java.util.List;
import com.eCommerce.product.model.Product;
import com.eCommerce.product.service.ProductServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  ProductServiceImpl productServiceImpl;

  @GetMapping
  public ResponseEntity<List<Product>> getAllProducts() {
    List<Product> productList = productServiceImpl.getAllProducts();
    if (productList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.ok(productList);
    }
  }

}
