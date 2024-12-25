package com.eCommerce.product.repository;

import com.eCommerce.product.model.Product;
import com.eCommerce.product.model.ProductPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRep extends JpaRepository<Product, ProductPk> {

}
