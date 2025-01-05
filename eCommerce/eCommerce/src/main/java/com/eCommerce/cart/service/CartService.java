package com.eCommerce.cart.service;

import java.util.List;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.model.Cart;

public interface CartService {

  /**
   * Creates a new empty cart.
   * 
   * @return the created {@link Cart}.
   */
  CartDtoOut createCart();

  /**
   * Adds products to the cart. If the product already exists in the cart, its quantity is updated.
   * 
   * @param cartDtoInList
   * @return
   * 
   */
  CartDtoOut addProductsToCart(List<CartDtoIn> cartDtoInList);

  /**
   * Updates the quantity of a product in the cart.
   * 
   * @param cartDtoIn
   */
  CartDtoOut updateCart(CartDtoIn cartDtoIn);

  /**
   * Retrieves a cart by its ID.
   * 
   * @param idCart
   *          ID of the cart.
   * @return an {@link Cart} containing the cart if found, or empty if not.
   */
  CartDtoOut getCart(String idCart);

  /**
   * Deletes a cart and all its relationships.
   * 
   * @param idCart
   *          ID of the cart.
   */
  void deleteCart(String idCart);

}
