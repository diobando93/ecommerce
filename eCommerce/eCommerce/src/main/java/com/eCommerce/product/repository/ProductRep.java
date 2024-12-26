package com.eCommerce.product.repository;

import com.eCommerce.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRep extends JpaRepository<Product, String> {

}
