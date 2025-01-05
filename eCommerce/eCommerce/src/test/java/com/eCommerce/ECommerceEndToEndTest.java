package com.eCommerce;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.service.CartService;
import com.eCommerce.cart.service.CartServiceHelper;
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
public class ECommerceEndToEndTest {

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
    CartDtoIn cartDtoIn = new CartDtoIn("1", "101", 2);
    CartDtoOut mockCartDtoOut = new CartDtoOut();
    mockCartDtoOut.setIdCart("1");

    when(cartService.addProductsToCart(anyList())).thenReturn(mockCartDtoOut);

    mockMvc
        .perform(post("/cart/addProductToCart").content(objectMapper.writeValueAsString(List.of(cartDtoIn))).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.idCart").value("1"));

    verify(cartService).addProductsToCart(anyList());
  }

  @Test
  void createCart_ShouldReturnNewCart() throws Exception {
    CartDtoOut mockCartDtoOut = new CartDtoOut();
    mockCartDtoOut.setIdCart("1");
    when(cartService.createCart()).thenReturn(mockCartDtoOut);

    mockMvc.perform(post("/cart").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.idCart").value("1"));
  }
}
