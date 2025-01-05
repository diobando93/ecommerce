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
  public CartDtoOut createCart() {

    Cart cart = cartServiceHelper.createCart();
    return cartServiceHelper.convertCartToDtoOut(cart);

  }

  @Override
  public CartDtoOut getCart(String idCart) {

    Cart cart = cartServiceHelper.searchCartById(idCart);
    return cartServiceHelper.convertCartToDtoOut(cart);

  }

  @Override
  public CartDtoOut addProductsToCart(List<CartDtoIn> cartDtoInList) {

    Cart cart = new Cart();
    if (cartServiceHelper.chkCartDtoInList(cartDtoInList)) {
      cart = cartServiceHelper.searchCartById(cartDtoInList.get(0).getIdCart());
      for (CartDtoIn cartDtoIn : cartDtoInList) {
        if (cartServiceHelper.chkDatainCartDto(cartDtoIn)) {
          Product product = cartServiceHelper.searchProductById(cartDtoIn.getIdProduct());
          cartServiceHelper.createOrUpdateCart(cart, product, cartDtoIn.getQuantity().intValue());
        }

      }

    }
    return cartServiceHelper.convertCartToDtoOut(cart);

  }

  @Override
  public CartDtoOut updateCart(CartDtoIn cartDtoIn) {

    Cart cart = new Cart();
    if (cartServiceHelper.chkDatainCartDto(cartDtoIn)) {
      cart = cartServiceHelper.searchCartById(cartDtoIn.getIdCart());
      Product product = cartServiceHelper.searchProductById(cartDtoIn.getIdProduct());
      cartServiceHelper.updateCart(cart, product, cartDtoIn.getQuantity().intValue());
    }

    return cartServiceHelper.convertCartToDtoOut(cart);

  }

  @Override
  public void deleteCart(String idCart) {

    cartServiceHelper.deleteCart(idCart);

  }

}
