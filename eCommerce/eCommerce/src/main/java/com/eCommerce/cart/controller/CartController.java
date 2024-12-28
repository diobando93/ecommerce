package com.eCommerce.cart.controller;

import java.util.List;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/cart")
@Tag(name = "Cart", description = "Endpoints to manage cart operations")
public class CartController {

  @Autowired
  private CartService cartService;

  @Operation(summary = "Create a new cart", description = "Creates a new cart and returns its details.")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Cart created successfully"),
      @ApiResponse(responseCode = "500", description = "Internal server error") })
  @PostMapping
  public ResponseEntity<Cart> createCart() {
    Cart cart = cartService.createCart();
    return ResponseEntity.ok(cart);
  }

  @Operation(summary = "Add product to cart", description = "Adds a product to a specific cart with the specified quantity.")
  @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Product added to cart successfully"),
      @ApiResponse(responseCode = "400", description = "Invalid input provided"),
      @ApiResponse(responseCode = "404", description = "Cart or Product not found") })
  @PostMapping("/addProductToCart")
  public ResponseEntity<CartDtoOut> addProductToCart(@RequestBody List<CartDtoIn> cartDtoInList) {
    CartDtoOut cartDtoOut = cartService.addProductsToCart(cartDtoInList);
    return ResponseEntity.ok(cartDtoOut);
  }

  // @Operation(summary = "Update product quantity in cart", description = "Updates the quantity of a product in a specific cart.")
  // @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Quantity updated successfully"),
  // @ApiResponse(responseCode = "400", description = "Invalid input provided"),
  // @ApiResponse(responseCode = "404", description = "Cart or Product not found") })
  // @PutMapping("/updateCart")
  // public ResponseEntity<Void> updateProductQuantity(@RequestBody CartDtoOut request) {
  // cartService.updateProductQuantity(request.getIdCart(), request.getIdProduct(), request.getQuantity());
  // return ResponseEntity.ok().build();
  // }

  @Operation(summary = "Get cart by ID", description = "Retrieve a cart by its ID")
  @ApiResponses(
      value = { @ApiResponse(responseCode = "200", description = "Cart found"), @ApiResponse(responseCode = "404", description = "Cart not found") })
  @GetMapping("/{idCart}")
  public ResponseEntity<CartDtoOut> getCart(@PathVariable String idCart) {
    CartDtoOut cartDtoOut = cartService.getCart(idCart);
    return ResponseEntity.ok(cartDtoOut);
  }

  @Operation(summary = "Delete cart by ID", description = "Delete a cart by its ID")
  @ApiResponses(
      value = { @ApiResponse(responseCode = "200", description = "Cart found"), @ApiResponse(responseCode = "404", description = "Cart not found") })
  @DeleteMapping("/{idCart}")
  public ResponseEntity<Void> deleteCart(@PathVariable String idCart) {
    cartService.deleteCart(idCart);
    return ResponseEntity.ok().build();
  }

}
