package com.eCommerce.cart.service;

import java.util.Optional;
import com.eCommerce.cart.model.Cart;

public interface CartService {

  /**
   * Creates a new empty cart.
   * 
   * @return the created {@link Cart}.
   */
  Cart createCart();

  /**
   * Adds a product to the cart. If the product already exists in the cart, its quantity is updated.
   * 
   * @param idCart
   *          ID of the cart.
   * @param idProduct
   *          ID of the product.
   * @param quantity
   *          Quantity to add.
   */
  void addProductToCart(String idCart, String idProduct, int quantity);

  /**
   * Updates the quantity of a product in the cart.
   * 
   * @param idCart
   *          ID of the cart.
   * @param idProduct
   *          ID of the product.
   * @param quantity
   *          New quantity of the product.
   */
  void updateProductQuantity(String idCart, String idProduct, int quantity);

  /**
   * Removes a product from the cart.
   * 
   * @param idCart
   *          ID of the cart.
   * @param idProduct
   *          ID of the product.
   */
  void removeProductFromCart(String idCart, String idProduct);

  /**
   * Retrieves a cart by its ID.
   * 
   * @param idCart
   *          ID of the cart.
   * @return an {@link Cart} containing the cart if found, or empty if not.
   */
  Optional<Cart> getCart(String idCart);

  /**
   * Deletes a cart and all its relationships.
   * 
   * @param idCart
   *          ID of the cart.
   */
  void deleteCart(String idCart);

}
