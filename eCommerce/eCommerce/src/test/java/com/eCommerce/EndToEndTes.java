package com.eCommerce;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.cart.service.CartService;
import com.eCommerce.cart.service.CartServiceHelper;
import com.eCommerce.product.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class EndToEndTes {

  @Autowired
  private MockMvc           mockMvc;

  @Autowired
  private ObjectMapper      objectMapper;

  @MockBean
  private CartService       cartService;

  @MockBean
  private CartServiceHelper cartServiceHelper;

  @Test
  void addProductsToCart_ShouldAddProducts() throws Exception {
    // Arrange
    Cart mockCart = new Cart();
    mockCart.setIdCart("1");
    Product mockProduct = new Product();
    mockProduct.setIdProduct("101");
    mockProduct.setPrice(50.0);
    mockProduct.setAmount(10);

    // Mock del CartService
    doNothing().when(cartService).addProductsToCart(anyList());

    // Mock de todos los métodos del helper
    when(cartServiceHelper.createCart()).thenReturn(mockCart);
    when(cartServiceHelper.chkCartDtoInList(anyList())).thenReturn(true);
    when(cartServiceHelper.chkDatainCartDto(any(CartDtoIn.class))).thenReturn(true);
    when(cartServiceHelper.searchCartById(anyString())).thenReturn(mockCart);
    when(cartServiceHelper.searchProductById(anyString())).thenReturn(mockProduct);
    doNothing().when(cartServiceHelper).createOrUpdateCart(any(Cart.class), any(Product.class), anyInt());

    // Preparar datos para agregar al carrito
    CartDtoIn cartDtoIn = new CartDtoIn();
    cartDtoIn.setIdCart("1");
    cartDtoIn.setIdProduct("101");
    cartDtoIn.setQuantity(2);
    List<CartDtoIn> cartDtoInList = List.of(cartDtoIn);

    // Debug: Imprimir el JSON que estamos enviando
    System.out.println("Request JSON: " + objectMapper.writeValueAsString(cartDtoInList));

    // Act: Realizar la llamada al endpoint
    mockMvc.perform(post("/cart/addProductToCart").content(objectMapper.writeValueAsString(cartDtoInList)).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andDo(print()); // Agregar esto para ver la respuesta completa

    // Verify: Verificar que el servicio fue llamado con los parámetros correctos
    verify(cartService).addProductsToCart(anyList());
  }

  @Test
  void createCart_ShouldReturnNewCart() throws Exception {
    // Arrange
    Cart mockCart = new Cart();
    mockCart.setIdCart("1");
    when(cartService.createCart()).thenReturn(mockCart);

    // Act & Assert
    mockMvc.perform(post("/cart").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.idCart").value("1"));
  }
}
