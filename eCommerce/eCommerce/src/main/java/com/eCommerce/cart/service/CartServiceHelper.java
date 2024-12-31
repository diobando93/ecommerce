package com.eCommerce.cart.service;

import java.util.List;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.exception.ApiException;
import com.eCommerce.product.model.Product;

public interface CartServiceHelper {

  /**
   * Searches for a cart by its ID.
   *
   * @param idCart
   *          unique identifier of the cart
   * @return Cart if found
   * @throws ApiException
   *           with CONFLICT status if cart not found
   */
  public Cart searchCartById(String idCart);

  /**
   * Searches for a product by its ID.
   *
   * @param idProduct
   *          unique identifier of the product
   * @return Product if found
   * @throws ApiException
   *           with CONFLICT status if cart not found
   */
  public Product searchProductById(String idProduct);

  /**
   * Validates the stock availability of a product.
   *
   * @param product
   *          Product to check
   * @param quantity
   *          Requested quantity
   * @return true if enough stock available, false otherwise
   */
  public boolean chkStockOfProdcut(Product product, int quantity);

  /**
   * Creates or updates a cart with product information. Updates quantities and calculates subtotals for orders.
   *
   * @param cart
   *          Cart to be updated
   * @param product
   *          Product to be added or updated
   * @param quantity
   *          Quantity of the product
   * @throws ApiException
   *           if quantity is invalid or product not in stock
   */
  public void createOrUpdateCart(Cart cart, Product product, int quantity);

  /**
   * Converts a Cart entity to its DTO representation. Includes calculation of total price and mapping of orders to order DTOs.
   *
   * @param cart
   *          the Cart entity to be converted
   * @return CartDtoOut containing the cart information and its orders
   */
  public CartDtoOut convertCartToDtoOut(Cart cart);

  /**
   * Validates a list of cart DTOs. Checks if the list is not empty and all cart IDs in the list refer to the same cart.
   *
   * @param cartDtoInList
   *          list of cart DTOs to validate
   * @return true if the list is valid
   * @throws ApiException
   *           if the list is empty or contains different cart IDs
   */

  public boolean chkCartDtoInList(List<CartDtoIn> cartDtoInList);

  /**
   * Validates individual cart DTO data. Ensures that cart ID, product ID, and quantity are not null.
   *
   * @param cartDtoIn
   *          the cart DTO to validate
   * @return true if all required fields are present
   * @throws ApiException
   *           if any required field is null
   */
  public boolean chkDatainCartDto(CartDtoIn cartDtoIn);

  /**
   * Updates the quantity of a product in a cart. If quantity is 0, removes the product from the cart. If quantity is greater than 0, updates the
   * order and recalculates totals.
   *
   * @param cart
   *          the cart to update
   * @param product
   *          the product to update
   * @param quantity
   *          new quantity of the product
   * @throws ApiException
   *           if quantity is invalid or product not in stock
   */
  public void updateCart(Cart cart, Product product, int quantity);

  /**
   * Deletes a cart and all its associated orders.
   *
   * @param idCart
   *          ID of the cart to delete
   * @throws ApiException
   *           if cart ID is null or cart does not exist
   */
  public void deleteCart(String idCart);

  /**
   * Creates a new empty shopping cart. Initializes the cart with default values and saves it to the database.
   *
   * @return newly created Cart entity
   */
  public Cart createCart();

}
