package com.eCommerce;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.cart.repository.CartRep;
import com.eCommerce.cart.service.CartServiceImpl;
import com.eCommerce.cart.service.helper.CartMapper;
import com.eCommerce.cart.service.helper.CartValidator;
import com.eCommerce.exception.ApiErrorCode;
import com.eCommerce.exception.ApiException;
import com.eCommerce.orders.model.Order;
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
  private CartRep         cartRep;
  @Mock
  private ProductRep      productRep;
  @Mock
  private CartMapper      cartMapper;
  @Mock
  private CartValidator   cartValidator;

  @InjectMocks
  private CartServiceImpl cartService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createCart_Success() {
    Cart cart = new Cart();
    CartDtoOut cartDto = new CartDtoOut();
    when(cartRep.save(any())).thenReturn(cart);
    when(cartMapper.toDto(cart)).thenReturn(cartDto);

    CartDtoOut result = cartService.createCart();

    assertNotNull(result);
    verify(cartRep).save(any());
    verify(cartMapper).toDto(cart);
  }

  @Test
  void addProductsToCart_Success() {
    CartDtoIn dto = new CartDtoIn("1", "101", 2);
    Cart cart = new Cart();
    Product product = new Product();
    CartDtoOut cartDto = new CartDtoOut();
    product.setIdProduct("101");
    product.setPrice(33.6);

    when(cartRep.findById("1")).thenReturn(Optional.of(cart));
    when(productRep.findById("101")).thenReturn(Optional.of(product));
    when(cartMapper.toDto(cart)).thenReturn(cartDto);

    CartDtoOut result = cartService.addProductsToCart(List.of(dto));

    assertNotNull(result);
    verify(cartValidator).validateCartDtoInList(any());
    verify(cartValidator).validateCartDtoData(dto);
    verify(cartValidator).validateQuantityAndStock(product, 2);
    verify(cartRep).save(cart);
  }

  @Test
  void updateCart_Success() {
    CartDtoIn dto = new CartDtoIn("1", "101", 2);
    Cart cart = new Cart();
    Product product = new Product();
    product.setIdProduct("101");
    product.setPrice(33.6);

    Order order = new Order();
    order.setProduct(product);
    cart.addOrder(order);

    CartDtoOut cartDto = new CartDtoOut();

    when(cartRep.findById("1")).thenReturn(Optional.of(cart));
    when(productRep.findById("101")).thenReturn(Optional.of(product));
    when(cartMapper.toDto(cart)).thenReturn(cartDto);

    CartDtoOut result = cartService.updateCart(dto);

    assertNotNull(result);
    verify(cartValidator).validateCartDtoData(dto);
    verify(cartValidator).validateUpdateQuantity(product, 2);
    verify(cartValidator).validateOrder(any(), eq(cart), eq(product));
    verify(cartRep).save(cart);
  }

  @Test
  void deleteCart_Success() {
    String cartId = "1";
    when(cartRep.existsById(cartId)).thenReturn(true);

    cartService.deleteCart(cartId);

    verify(cartValidator).validateCartToDelete(cartId, false);
    verify(cartRep).deleteById(cartId);
  }

  @Test
  void validateCartNotFound_ThrowsException() {
    String cartId = "1";
    when(cartRep.findById(cartId)).thenReturn(Optional.empty());

    ApiException exception = assertThrows(ApiException.class, () -> cartService.getCart(cartId));

    assertEquals(ApiErrorCode.CART_NOT_FOUND.getMessage(cartId), exception.getMessage());
  }

  @Test
  void getCart_Success() {
    Cart cart = new Cart();
    CartDtoOut cartDto = new CartDtoOut();

    when(cartRep.findById("1")).thenReturn(Optional.of(cart));
    when(cartMapper.toDto(cart)).thenReturn(cartDto);

    CartDtoOut result = cartService.getCart("1");

    assertNotNull(result);
    verify(cartRep).findById("1");
    verify(cartMapper).toDto(cart);
  }

  @Test
  void getCart_NotFound_ThrowsException() {
    String cartId = "1";
    when(cartRep.findById(cartId)).thenReturn(Optional.empty());

    ApiException exception = assertThrows(ApiException.class, () -> cartService.getCart(cartId));

    assertEquals(ApiErrorCode.CART_NOT_FOUND.getMessage(cartId), exception.getMessage());
    verify(cartRep).findById(cartId);
  }

  @Test
  void updateCart_InsufficientStock_ThrowsException() {
    String cartId = "1";
    String productId = "101";
    CartDtoIn dto = new CartDtoIn(cartId, productId, 100);
    Cart cart = new Cart();
    Product product = new Product();
    product.setAmount(10);

    when(cartRep.findById(cartId)).thenReturn(Optional.of(cart));
    when(productRep.findById(productId)).thenReturn(Optional.of(product));
    doThrow(new ApiException(ApiErrorCode.INSUFFICIENT_STOCK.getMessage(productId), ApiErrorCode.INSUFFICIENT_STOCK.getStatus())).when(cartValidator)
        .validateUpdateQuantity(product, 100);

    ApiException exception = assertThrows(ApiException.class, () -> cartService.updateCart(dto));

    assertEquals(ApiErrorCode.INSUFFICIENT_STOCK.getMessage(productId), exception.getMessage());
  }

  @Test
  void addProductsToCart_EmptyList_ThrowsException() {
    CartDtoIn dto = new CartDtoIn("1", "101", 2);
    List<CartDtoIn> dtos = List.of(dto);

    doThrow(new ApiException(ApiErrorCode.EMPTY_CART_LIST.getMessage(), ApiErrorCode.EMPTY_CART_LIST.getStatus())).when(cartValidator)
        .validateCartDtoInList(dtos);

    ApiException exception = assertThrows(ApiException.class, () -> cartService.addProductsToCart(dtos));

    assertEquals(ApiErrorCode.EMPTY_CART_LIST.getMessage(), exception.getMessage());
  }
}
