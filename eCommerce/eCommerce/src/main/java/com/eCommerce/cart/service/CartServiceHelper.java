package com.eCommerce.cart.service;

import java.util.List;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.product.model.Product;

public interface CartServiceHelper {

  public Cart searchCartById(String idCart);

  public Product searchProductById(String idProduct);

  public boolean chkStockOfProdcut(Product product, int quantity);

  public void createOrUpdateCart(Cart cart, Product product, int quantity);

  public CartDtoOut convertCartToDtoOut(Cart cart);

  public boolean chkCartDtoInList(List<CartDtoIn> cartDtoInList);

  public boolean chkDatainCartDto(CartDtoIn cartDtoIn);

  public void updateCart(Cart cart, Product product, int quantity);

  public void deleteCart(String idCart);

  public void removeProductFromCart(String idCart, String idProduct);

  public Cart createCart();

}
