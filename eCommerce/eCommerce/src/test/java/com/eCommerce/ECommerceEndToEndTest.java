package com.eCommerce;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.service.CartService;
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
class ECommerceEndToEndTest {

  @Autowired
  private MockMvc      mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private CartService  cartService;

  @Test
  void createCart_Success() throws Exception {
    CartDtoOut cartDto = new CartDtoOut();
    cartDto.setIdCart("1");
    when(cartService.createCart()).thenReturn(cartDto);

    mockMvc.perform(post("/cart").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk()).andExpect(jsonPath("$.idCart").value("1"));
  }

  @Test
  void addProductsToCart_Success() throws Exception {
    CartDtoIn cartDtoIn = new CartDtoIn("1", "101", 2);
    CartDtoOut cartDto = new CartDtoOut();
    cartDto.setIdCart("1");

    when(cartService.addProductsToCart(anyList())).thenReturn(cartDto);

    mockMvc
        .perform(post("/cart/addProductToCart").content(objectMapper.writeValueAsString(List.of(cartDtoIn))).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$.idCart").value("1"));
  }
}
