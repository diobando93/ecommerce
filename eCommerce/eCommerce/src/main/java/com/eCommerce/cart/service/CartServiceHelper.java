package com.eCommerce.cart.service;

import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.product.model.Product;

public interface CartServiceHelper {

  public Cart searchCartById(String idCart);

  public Product searchProductById(String idProduct);

  public boolean chkStockOfProdcut(Product product, int quantity);

  public CartDtoOut createOrUpdateOrder(Cart cart, Product product, int quantity);

  public CartDtoOut convertToCartDto(Cart cart);

}
