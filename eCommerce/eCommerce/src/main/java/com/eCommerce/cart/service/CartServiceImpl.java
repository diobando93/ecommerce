package com.eCommerce.cart.service;

import java.util.List;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.product.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("cartService")
public class CartServiceImpl implements CartService {

  @Autowired
  private CartServiceHelper cartServiceHelper;

  @Override
  public Cart createCart() {

    return cartServiceHelper.createCart();

  }

  @Override
  public CartDtoOut getCart(String idCart) {

    Cart cart = cartServiceHelper.searchCartById(idCart);
    return cartServiceHelper.convertCartToDtoOut(cart);

  }

  @Override
  public void addProductsToCart(List<CartDtoIn> cartDtoInList) {

    if (cartServiceHelper.chkCartDtoInList(cartDtoInList)) {

      for (CartDtoIn cartDtoIn : cartDtoInList) {

        if (cartServiceHelper.chkDatainCartDto(cartDtoIn)) {
          Cart cart = cartServiceHelper.searchCartById(cartDtoIn.getIdCart());
          Product product = cartServiceHelper.searchProductById(cartDtoIn.getIdProduct());
          cartServiceHelper.createOrUpdateCart(cart, product, cartDtoIn.getQuantity().intValue());
        }

      }

    }

  }

  @Override
  public void updateCart(CartDtoIn cartDtoIn) {

    if (cartServiceHelper.chkDatainCartDto(cartDtoIn)) {
      Cart cart = cartServiceHelper.searchCartById(cartDtoIn.getIdCart());
      Product product = cartServiceHelper.searchProductById(cartDtoIn.getIdProduct());
      cartServiceHelper.updateCart(cart, product, cartDtoIn.getQuantity().intValue());
    }

  }

  @Override
  public void deleteCart(String idCart) {

    cartServiceHelper.deleteCart(idCart);

  }

}
