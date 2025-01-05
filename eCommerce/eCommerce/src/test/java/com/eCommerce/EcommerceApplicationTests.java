package com.eCommerce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collections;
import java.util.List;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.cart.repository.CartRep;
import com.eCommerce.cart.service.CartServiceHelper;
import com.eCommerce.cart.service.CartServiceImpl;
import com.eCommerce.exception.ApiErrorCode;
import com.eCommerce.exception.ApiException;
import com.eCommerce.product.model.Product;
import com.eCommerce.product.repository.ProductRep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EcommerceApplicationTests {

  @Mock
  private CartRep           cartRep;

  @Mock
  private ProductRep        productRep;

  @Mock
  private CartServiceHelper cartServiceHelper;

  @InjectMocks
  private CartServiceImpl   cartService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void contextLoads() {
  }

  @Test
  void createCart_ShouldReturnCartDtoOut() {
    Cart mockCart = new Cart();
    CartDtoOut mockCartDtoOut = new CartDtoOut();
    when(cartServiceHelper.createCart()).thenReturn(mockCart);
    when(cartServiceHelper.convertCartToDtoOut(mockCart)).thenReturn(mockCartDtoOut);

    CartDtoOut result = cartService.createCart();

    assertNotNull(result);
    verify(cartServiceHelper).createCart();
    verify(cartServiceHelper).convertCartToDtoOut(mockCart);
  }

  @Test
  void addProductsToCart_ShouldReturnCartDtoOut() {
    CartDtoIn cartDtoIn = new CartDtoIn("1", "101", 2);
    Cart mockCart = new Cart();
    Product mockProduct = new Product();
    CartDtoOut mockCartDtoOut = new CartDtoOut();

    when(cartServiceHelper.chkCartDtoInList(Collections.singletonList(cartDtoIn))).thenReturn(true);
    when(cartServiceHelper.searchCartById("1")).thenReturn(mockCart);
    when(cartServiceHelper.chkDatainCartDto(cartDtoIn)).thenReturn(true);
    when(cartServiceHelper.searchProductById("101")).thenReturn(mockProduct);
    when(cartServiceHelper.convertCartToDtoOut(mockCart)).thenReturn(mockCartDtoOut);

    CartDtoOut result = cartService.addProductsToCart(Collections.singletonList(cartDtoIn));

    assertNotNull(result);
    verify(cartServiceHelper).createOrUpdateCart(mockCart, mockProduct, 2);
  }

  @Test
  void updateCart_ShouldReturnCartDtoOut() {
    CartDtoIn cartDtoIn = new CartDtoIn("1", "101", 2);
    Cart mockCart = new Cart();
    Product mockProduct = new Product();
    CartDtoOut mockCartDtoOut = new CartDtoOut();

    when(cartServiceHelper.chkDatainCartDto(cartDtoIn)).thenReturn(true);
    when(cartServiceHelper.searchCartById("1")).thenReturn(mockCart);
    when(cartServiceHelper.searchProductById("101")).thenReturn(mockProduct);
    when(cartServiceHelper.convertCartToDtoOut(mockCart)).thenReturn(mockCartDtoOut);

    CartDtoOut result = cartService.updateCart(cartDtoIn);

    assertNotNull(result);
    verify(cartServiceHelper).updateCart(mockCart, mockProduct, 2);
  }

  @Test
  void getCart_ShouldReturnCartDtoOut() {
    // Arrange
    Cart mockCart = new Cart();
    CartDtoOut mockCartDtoOut = new CartDtoOut();

    when(cartServiceHelper.searchCartById("1")).thenReturn(mockCart);
    when(cartServiceHelper.convertCartToDtoOut(mockCart)).thenReturn(mockCartDtoOut);

    // Act
    CartDtoOut result = cartService.getCart("1");

    // Assert
    assertNotNull(result);
    verify(cartServiceHelper, times(1)).searchCartById("1");
    verify(cartServiceHelper, times(1)).convertCartToDtoOut(mockCart);
  }

  @Test
  void getCart_ShouldThrowException_WhenCartNotFound() {
    // Arrange
    String cartId = "1";
    when(cartServiceHelper.searchCartById(cartId))
        .thenThrow(new ApiException(ApiErrorCode.CART_NOT_FOUND.getMessage(cartId), ApiErrorCode.CART_NOT_FOUND.getStatus()));

    // Act & Assert
    ApiException exception = assertThrows(ApiException.class, () -> cartService.getCart(cartId));

    assertEquals(ApiErrorCode.CART_NOT_FOUND.getMessage(cartId), exception.getMessage());
  }

  @Test
  void updateCart_ShouldThrowException_WhenProductNotInStock() {
    // Arrange
    String cartId = "1";
    String productId = "101";
    int quantity = 100; // cantidad muy alta
    CartDtoIn cartDtoIn = new CartDtoIn(cartId, productId, quantity);

    Cart mockCart = new Cart();
    Product mockProduct = new Product();
    mockProduct.setAmount(10);

    when(cartServiceHelper.chkDatainCartDto(cartDtoIn)).thenReturn(true);
    when(cartServiceHelper.searchCartById(cartId)).thenReturn(mockCart);
    when(cartServiceHelper.searchProductById(productId)).thenReturn(mockProduct);

    doThrow(new ApiException(ApiErrorCode.INSUFFICIENT_STOCK.getMessage(productId), ApiErrorCode.INSUFFICIENT_STOCK.getStatus()))
        .when(cartServiceHelper).updateCart(any(Cart.class), any(Product.class), eq(quantity));

    // Act & Assert
    ApiException exception = assertThrows(ApiException.class, () -> cartService.updateCart(cartDtoIn));

    assertEquals(ApiErrorCode.INSUFFICIENT_STOCK.getMessage(productId), exception.getMessage());
  }

  @Test
  void addProductsToCart_ShouldThrowException_WhenValidationFails() {
    // Arrange
    String cartId = "1";
    String productId = "101";
    int quantity = 2;
    CartDtoIn cartDtoIn = new CartDtoIn(cartId, productId, quantity);
    List<CartDtoIn> cartDtoInList = Collections.singletonList(cartDtoIn);

    when(cartServiceHelper.chkCartDtoInList(cartDtoInList))
        .thenThrow(new ApiException(ApiErrorCode.EMPTY_CART_LIST.getMessage(), ApiErrorCode.EMPTY_CART_LIST.getStatus()));

    // Act & Assert
    ApiException exception = assertThrows(ApiException.class, () -> cartService.addProductsToCart(cartDtoInList));

    assertEquals(ApiErrorCode.EMPTY_CART_LIST.getMessage(), exception.getMessage());
  }

  @Test
  void deleteCart_ShouldCallHelperMethod() {
    // Arrange
    String cartId = "1";

    // Act
    cartService.deleteCart(cartId);

    // Assert
    verify(cartServiceHelper).deleteCart(cartId);
  }
}
