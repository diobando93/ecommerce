package com.eCommerce.product.controller;

import java.util.List;
import com.eCommerce.exception.ApiException;
import com.eCommerce.product.model.Product;
import com.eCommerce.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private ProductService productService;

  /**
   * Retrieves all products from the system. Returns a list of all available products. If no products are found, returns a NO_CONTENT status.
   *
   * @return ResponseEntity containing a list of products if found, or NO_CONTENT if empty
   * @throws ApiException
   *           if there's an error accessing the product repository
   */
  @Operation(summary = "Get all products", description = "Retrieves a list of all available products in the system")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Products found successfully",
          content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Product.class)))),
      @ApiResponse(responseCode = "204", description = "No products available"),
      @ApiResponse(responseCode = "500", description = "Internal server error") })
  @GetMapping
  public ResponseEntity<List<Product>> getAllProducts() {
    List<Product> productList = productService.getAllProducts();
    if (productList.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    } else {
      return ResponseEntity.ok(productList);
    }
  }

}
