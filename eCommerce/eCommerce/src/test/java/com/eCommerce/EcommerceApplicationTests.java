package com.eCommerce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Collections;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.cart.repository.CartRep;
import com.eCommerce.cart.service.CartServiceHelper;
import com.eCommerce.cart.service.CartServiceImpl;
import com.eCommerce.exception.ApiException;
import com.eCommerce.orders.repository.OrderRep;
import com.eCommerce.product.model.Product;
import com.eCommerce.product.repository.ProductRep;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest
class EcommerceApplicationTests {

  @Mock
  private CartRep           cartRep;

  @Mock
  private ProductRep        productRep;

  @Mock
  private OrderRep          orderRep;

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
  void createCart_ShouldReturnNewCart() {
    // Arrange
    // Arrange
    Cart mockCart = new Cart();
    when(cartServiceHelper.createCart()).thenReturn(mockCart);

    // Act
    Cart result = cartService.createCart();

    // Assert
    assertNotNull(result);
    verify(cartServiceHelper, times(1)).createCart();
  }

  @Test
  void addProductsToCart_ShouldCallHelperMethods() {
    CartDtoIn cartDtoIn = new CartDtoIn("1", "101", 2);
    Cart mockCart = new Cart();
    Product mockProduct = new Product();
    mockProduct.setAmount(10);

    // Mock todos los métodos necesarios
    when(cartServiceHelper.chkCartDtoInList(Collections.singletonList(cartDtoIn))).thenReturn(true);
    when(cartServiceHelper.chkDatainCartDto(cartDtoIn)).thenReturn(true); // Agregar este mock
    when(cartServiceHelper.searchCartById("1")).thenReturn(mockCart);
    when(cartServiceHelper.searchProductById("101")).thenReturn(mockProduct);

    // Act
    cartService.addProductsToCart(Collections.singletonList(cartDtoIn));

    // Assert
    verify(cartServiceHelper, times(1)).chkCartDtoInList(Collections.singletonList(cartDtoIn));
    verify(cartServiceHelper, times(1)).chkDatainCartDto(cartDtoIn);
    verify(cartServiceHelper, times(1)).searchCartById("1");
    verify(cartServiceHelper, times(1)).searchProductById("101");
    verify(cartServiceHelper, times(1)).createOrUpdateCart(mockCart, mockProduct, 2);
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
    when(cartServiceHelper.searchCartById("1")).thenThrow(new ApiException("Cart not found", HttpStatus.CONFLICT));

    // Act & Assert
    ApiException exception = assertThrows(ApiException.class, () -> cartService.getCart("1"));
    assertEquals("Cart not found", exception.getMessage());
  }

}
