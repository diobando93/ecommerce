package com.eCommerce.cart.service;

import java.util.List;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.dto.CartDtoOut;

public interface CartService {

  /**
   * Creates a new empty cart
   * 
   * @return {@link CartDtoOut} with cart details
   */
  CartDtoOut createCart();

  /**
   * Adds products to cart or updates quantities if they exist
   * 
   * @param cartDtoInList
   *          List of {@link CartDtoIn} to add/update
   * @return {@link CartDtoOut} with updated cart details
   */
  CartDtoOut addProductsToCart(List<CartDtoIn> cartDtoInList);

  /**
   * Updates product quantity in cart
   * 
   * @param cartDtoIn
   *          {@link CartDtoIn} with cart and product details to update
   * @return {@link CartDtoOut} with updated cart details
   */
  CartDtoOut updateCart(CartDtoIn cartDtoIn);

  /**
   * Gets cart by ID
   * 
   * @param idCart
   *          Cart identifier
   * @return {@link CartDtoOut} with cart details
   */
  CartDtoOut getCart(String idCart);

  /**
   * Deletes cart and associated orders
   * 
   * @param idCart
   *          Cart identifier
   */
  void deleteCart(String idCart);

}
