package com.eCommerce.product.service;

import java.util.List;
import com.eCommerce.product.model.Product;
import com.eCommerce.product.repository.ProductRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("productService")
public class ProductServiceImpl implements ProodcutService {

  @Autowired
  ProductRep productRep;

  @Override
  public List<Product> getAllProducts() {
    return productRep.findAll();
  }

}
