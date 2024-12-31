package com.eCommerce.exception;

public class ErrorMessages {

  public static final String CART_NOT_FOUND       = "Cart with ID '%s' not found";
  public static final String PRODUCT_NOT_FOUND    = "Product with ID '%s' not found";
  public static final String EMPTY_CART_LIST      = "We can't add products list empty";
  public static final String DIFFERENT_CARTS      = "The list have different carts";
  public static final String CART_ID_NULL         = "IdCart must not be null";
  public static final String PRODUCT_ID_NULL      = "IdProduct must not be null";
  public static final String QUANTITY_NULL        = "Quantity of product must not be null";
  public static final String QUANTITY_NEGATIVE    = "The quantity of Product with ID '%s' must be greater than 0";
  public static final String PRODUCT_NOT_IN_STOCK = "The quantity of Product with ID '%s' not in stock";
  public static final String ORDER_NOT_EXISTS     = "Order for idCart/idProduct: '%s/%s' not exists";

}
