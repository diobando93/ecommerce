package com.eCommerce.cart.service;

import java.util.List;
import java.util.Optional;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.cart.repository.CartRep;
import com.eCommerce.exception.ApiException;
import com.eCommerce.orders.model.Orders;
import com.eCommerce.orders.model.OrdersPk;
import com.eCommerce.orders.repository.OrderRep;
import com.eCommerce.product.model.Product;
import com.eCommerce.product.repository.ProductRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

@Service("cartService")
public class CartServiceImpl implements CartService {

  @Autowired
  private CartRep           cartRep;

  @Autowired
  private ProductRep        productRep;

  @Autowired
  private OrderRep          orderRep;

  @Autowired
  private CartServiceHelper cartServiceHelper;

  @Override
  public Cart createCart() {

    Cart cart = new Cart();

    return cartRep.save(cart);

  }

  @Override
  @Transactional
  public CartDtoOut addProductsToCart(List<CartDtoIn> cartDtoInList) {

    CartDtoOut cartDtoOut = new CartDtoOut();

    for (CartDtoIn cartDtoIn : cartDtoInList) {

      Cart cart = cartServiceHelper.searchCartById(cartDtoIn.getIdCart());
      Product product = cartServiceHelper.searchProductById(cartDtoIn.getIdProduct());
      cartDtoOut = cartServiceHelper.createOrUpdateOrder(cart, product, cartDtoIn.getQuantity());

    }

    return cartDtoOut;

  }

  @Override
  public void updateProductQuantity(String idCart, String idProduct, int quantity) {

    OrdersPk orderPk = new OrdersPk(idCart, idProduct);
    Optional<Orders> optionalOrder = orderRep.findById(orderPk);

    if (optionalOrder.isEmpty()) throw new ApiException("Product with ID '" + idProduct + "' not found", HttpStatus.NOT_FOUND);

    Orders order = optionalOrder.get();
    order.setQuantity(quantity);
    orderRep.save(order);

  }

  @Override
  public void removeProductFromCart(String idCart, String idProduct) {

    OrdersPk orderPk = new OrdersPk(idCart, idProduct);

    if (!orderRep.existsById(orderPk)) throw new ApiException("Product with ID '" + idProduct + "' not found", HttpStatus.NOT_FOUND);

    orderRep.deleteById(orderPk);

  }

  @Override
  public CartDtoOut getCart(String idCart) {
    Cart cart = cartServiceHelper.searchCartById(idCart);
    return cartServiceHelper.convertToCartDto(cart);
  }

  @Override
  public void deleteCart(String idCart) {

    if (!cartRep.existsById(idCart)) throw new ApiException("Cart with ID '" + idCart + "' not found", HttpStatus.NOT_FOUND);

    cartRep.deleteById(idCart);

  }

}
