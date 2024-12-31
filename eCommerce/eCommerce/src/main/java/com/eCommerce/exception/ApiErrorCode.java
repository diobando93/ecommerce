package com.eCommerce.exception;

import org.springframework.http.HttpStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ApiErrorCode {

  CART_NOT_FOUND("CAR001", "Cart with ID '%s' not found", HttpStatus.CONFLICT), PRODUCT_NOT_FOUND("PRO001", "Product with ID '%s' not found",
      HttpStatus.CONFLICT), EMPTY_CART_LIST("CAR002", "We can't add products list empty", HttpStatus.CONFLICT), DIFFERENT_CARTS("CAR003",
          "The list have different carts", HttpStatus.CONFLICT), INVALID_QUANTITY("PRO002",
              "The quantity of Product with ID '%s' must be greater than 0", HttpStatus.CONFLICT), INSUFFICIENT_STOCK("PRO003",
                  "The quantity of Product with ID '%s' not in stock", HttpStatus.CONFLICT), CART_ID_NULL("CAR004", "IdCart must not be null",
                      HttpStatus.CONFLICT), PRODUCT_ID_NULL("PRO002", "IdProduct must not be null",
                          HttpStatus.CONFLICT), QUANTITY_NULL("PRO005", "Quantity of product must not be null", HttpStatus.CONFLICT);
  ;

  private final String     code;
  private final String     message;
  private final HttpStatus status;

  public String getMessage(Object... args) {
    return String.format(message, args);
  }
}
