package com.eCommerce.product.service;

import java.util.List;
import com.eCommerce.product.model.Product;

public interface ProodcutService {

  /**
   * Search all products
   * 
   * @return list of {@link Product} objects
   */

  public List<Product> getAllProducts();

}
