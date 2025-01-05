package com.eCommerce.cart.service.helper;

import java.util.List;
import java.util.Optional;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.exception.ApiErrorCode;
import com.eCommerce.exception.ApiException;
import com.eCommerce.orders.model.Order;
import com.eCommerce.product.model.Product;
import org.springframework.stereotype.Component;

@Component
public class CartValidator {

  public void validateCartDtoInList(List<CartDtoIn> cartDtoInList) {
    if (cartDtoInList.isEmpty()) {
      throw new ApiException(ApiErrorCode.EMPTY_CART_LIST.getMessage(), ApiErrorCode.EMPTY_CART_LIST.getStatus());
    }

    boolean differentCarts = cartDtoInList.stream().map(CartDtoIn::getIdCart).distinct().count() > 1;
    if (differentCarts) {
      throw new ApiException(ApiErrorCode.DIFFERENT_CARTS.getMessage(), ApiErrorCode.DIFFERENT_CARTS.getStatus());
    }
  }

  public void validateCartDtoData(CartDtoIn cartDtoIn) {
    if (cartDtoIn.getIdCart() == null) {
      throw new ApiException(ApiErrorCode.CART_ID_NULL.getMessage(), ApiErrorCode.CART_ID_NULL.getStatus());
    }
    if (cartDtoIn.getIdProduct() == null) {
      throw new ApiException(ApiErrorCode.PRODUCT_ID_NULL.getMessage(), ApiErrorCode.PRODUCT_ID_NULL.getStatus());
    }
    if (cartDtoIn.getQuantity() == null) {
      throw new ApiException(ApiErrorCode.QUANTITY_NULL.getMessage(), ApiErrorCode.QUANTITY_NULL.getStatus());
    }
  }

  public void validateQuantityAndStock(Product product, int quantity) {
    if (quantity <= 0) {
      throw new ApiException(ApiErrorCode.INVALID_QUANTITY.getMessage(product.getIdProduct()), ApiErrorCode.INVALID_QUANTITY.getStatus());
    }
    if (product.getAmount() < quantity) {
      throw new ApiException(ApiErrorCode.INSUFFICIENT_STOCK.getMessage(product.getIdProduct()), ApiErrorCode.INSUFFICIENT_STOCK.getStatus());
    }
  }

  public void validateCartToDelete(String idCart, Boolean idCartNotExist) {
    if (idCart == null) {
      throw new ApiException(ApiErrorCode.CART_ID_NULL.getMessage(), ApiErrorCode.CART_ID_NULL.getStatus());
    }

    if (Boolean.TRUE.equals(idCartNotExist)) {
      throw new ApiException(ApiErrorCode.CART_NOT_FOUND.getMessage(idCart), ApiErrorCode.CART_NOT_FOUND.getStatus());
    }
  }

  public void validateOrder(Optional<Order> existingOrder, Cart cart, Product product) {
    if (existingOrder.isEmpty()) {
      throw new ApiException(ApiErrorCode.ORDER_NOT_EXISTS.getMessage(cart.getIdCart(), product.getIdProduct()),
          ApiErrorCode.ORDER_NOT_EXISTS.getStatus());
    }
  }

  public void validateUpdateQuantity(Product product, int quantity) {
    if (quantity < 0 || (quantity > 0 && product.getAmount() < quantity)) {
      throw new ApiException(ApiErrorCode.INVALID_QUANTITY.getMessage(product.getIdProduct()), ApiErrorCode.INVALID_QUANTITY.getStatus());
    }

  }

}
