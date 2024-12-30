package com.eCommerce.cart.controller;

import java.util.List;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.cart.service.CartService;
import com.eCommerce.exception.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * REST Controller for managing shopping cart operations. Provides endpoints for creating, updating, and managing shopping carts and their products.
 */
@RestController
@RequestMapping("/cart")
@Tag(name = "Cart", description = "Endpoints to manage cart operations")
public class CartController {

  @Autowired
  private CartService cartService;

  /**
   * Creates a new shopping cart.
   *
   * @return ResponseEntity containing the newly created Cart
   * @throws org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR
   *           if cart creation fails
   */
  @SuppressWarnings("javadoc")
  @Operation(summary = "Create a new cart", description = "Creates a new cart and returns its details.")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Cart created successfully"),
      @ApiResponse(responseCode = "500", description = "Internal server error") })
  @PostMapping
  public ResponseEntity<Cart> createCart() {
    Cart cart = cartService.createCart();
    return ResponseEntity.ok(cart);
  }

  /**
   * Adds products to an existing cart.
   *
   * @param cartDtoInList
   *          List of CartDtoIn containing products to be added
   * @return ResponseEntity with no content if operation is successful
   * @throws ApiException
   *           if cart not found or products are invalid
   */
  @Operation(summary = "Add product to cart", description = "Adds a product to a specific cart with the specified quantity.")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Product added to cart successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input provided"),
      @ApiResponse(responseCode = "404", description = "Cart or Product not found") })
  @PostMapping("/addProductToCart")
  public ResponseEntity<Void> addProductToCart(@RequestBody List<CartDtoIn> cartDtoInList) {
    cartService.addProductsToCart(cartDtoInList);
    return ResponseEntity.ok().build();
  }

  /**
   * Updates the quantity of a product in a specific cart. If the new quantity is 0, the product will be removed from the cart. If the new quantity is
   * greater than the available stock, an error will be returned.
   *
   * @param cartDtoIn
   *          DTO containing cart ID, product ID and new quantity
   * @return ResponseEntity with no content if update is successful
   * @throws ApiException
   *           if cart not found, product not found, or invalid quantity
   * @see CartDtoIn
   */
  @Operation(summary = "Update product quantity in cart", description = "Updates the quantity of a product in a specific cart.")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Quantity updated successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input provided"),
      @ApiResponse(responseCode = "404", description = "Cart or Product not found") })
  @PutMapping("/updateCart")
  public ResponseEntity<Void> updateProductQuantity(@RequestBody CartDtoIn cartDtoIn) {
    cartService.updateCart(cartDtoIn);
    return ResponseEntity.ok().build();
  }

  /**
   * Retrieves cart information by ID.
   *
   * @param idCart
   *          unique identifier of the cart
   * @return CartDtoOut containing cart details and its items
   * @throws ApiException
   *           if cart not found
   */
  @Operation(summary = "Get cart by ID", description = "Retrieve a cart by its ID")
  @ApiResponses(
      value = { @ApiResponse(responseCode = "200", description = "Cart found"), @ApiResponse(responseCode = "404", description = "Cart not found") })
  @GetMapping("/{idCart}")
  public ResponseEntity<CartDtoOut> getCart(@PathVariable String idCart) {
    CartDtoOut cartDtoOut = cartService.getCart(idCart);
    return ResponseEntity.ok(cartDtoOut);
  }

  /**
   * Deletes a cart and all its associated orders. This operation cannot be undone. All products in the cart will be removed.
   *
   * @param idCart
   *          unique identifier of the cart to be deleted
   * @return ResponseEntity with no content if deletion is successful
   * @throws ApiException
   *           if cart not found or cart ID is null
   */
  @Operation(summary = "Delete cart by ID", description = "Delete a cart by its ID")
  @ApiResponses(
      value = { @ApiResponse(responseCode = "200", description = "Cart found"), @ApiResponse(responseCode = "404", description = "Cart not found") })
  @DeleteMapping("/{idCart}")
  public ResponseEntity<Void> deleteCart(@PathVariable String idCart) {
    cartService.deleteCart(idCart);
    return ResponseEntity.ok().build();
  }

}
