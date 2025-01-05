package com.eCommerce.cart.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.cart.repository.CartRep;
import com.eCommerce.cart.service.helper.CartMapper;
import com.eCommerce.cart.service.helper.CartValidator;
import com.eCommerce.exception.ApiErrorCode;
import com.eCommerce.exception.ApiException;
import com.eCommerce.orders.model.Order;
import com.eCommerce.orders.model.OrderPk;
import com.eCommerce.product.model.Product;
import com.eCommerce.product.repository.ProductRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("cartService")
public class CartServiceImpl implements CartService {

  @Autowired
  private CartRep       cartRep;

  @Autowired
  private ProductRep    productRep;

  @Autowired
  private CartValidator cartValidator;

  @Autowired
  private CartMapper    cartMapper;

  @Override
  public CartDtoOut createCart() {
    Cart cart = cartRep.save(new Cart());
    return cartMapper.toDto(cart);
  }

  @Override
  public CartDtoOut getCart(String idCart) {
    Cart cart = searchCartById(idCart);
    return cartMapper.toDto(cart);
  }

  @Override
  public CartDtoOut addProductsToCart(List<CartDtoIn> cartDtoInList) {

    cartValidator.validateCartDtoInList(cartDtoInList);
    Cart cart = searchCartById(cartDtoInList.get(0).getIdCart());

    for (CartDtoIn cartDtoIn : cartDtoInList) {
      cartValidator.validateCartDtoData(cartDtoIn);
      Product product = searchProductById(cartDtoIn.getIdProduct());
      cartValidator.validateQuantityAndStock(product, cartDtoIn.getQuantity().intValue());
      createOrUpdateCart(cart, product, cartDtoIn.getQuantity().intValue());
    }

    return cartMapper.toDto(cart);
  }

  @Override
  public CartDtoOut updateCart(CartDtoIn cartDtoIn) {

    cartValidator.validateCartDtoData(cartDtoIn);
    Cart cart = searchCartById(cartDtoIn.getIdCart());
    Product product = searchProductById(cartDtoIn.getIdProduct());
    updateCartWithProduct(cart, product, cartDtoIn.getQuantity().intValue());
    return cartMapper.toDto(cart);

  }

  @Override
  public void deleteCart(String idCart) {

    cartValidator.validateCartToDelete(idCart, !cartRep.existsById(idCart));

    cartRep.deleteById(idCart);

  }

  private Cart searchCartById(String idCart) {
    return cartRep.findById(idCart)
        .orElseThrow(() -> new ApiException(ApiErrorCode.CART_NOT_FOUND.getMessage(idCart), ApiErrorCode.CART_NOT_FOUND.getStatus()));
  }

  private Product searchProductById(String idProduct) {
    return productRep.findById(idProduct)
        .orElseThrow(() -> new ApiException(ApiErrorCode.PRODUCT_NOT_FOUND.getMessage(idProduct), ApiErrorCode.PRODUCT_NOT_FOUND.getStatus()));
  }

  private void createOrUpdateCart(Cart cart, Product product, int quantity) {

    Optional<Order> existingOrder = cart.getOrders().stream().filter(order -> order.getProduct().getIdProduct().equals(product.getIdProduct()))
        .findFirst();

    if (existingOrder.isPresent()) {
      updateOrder(existingOrder.get(), product, quantity);
    } else {
      createNewOrder(cart, product, quantity);
    }

    cart.setUpdatedAt(LocalDateTime.now());
    cartRep.save(cart);
  }

  private void updateCartWithProduct(Cart cart, Product product, int quantity) {
    Optional<Order> existingOrder = cart.getOrders().stream().filter(order -> order.getProduct().getIdProduct().equals(product.getIdProduct()))
        .findFirst();

    cartValidator.validateOrder(existingOrder, cart, product);
    cartValidator.validateUpdateQuantity(product, quantity);
    Order order = existingOrder.get();

    if (quantity == 0) {
      cart.removeOrder(order);
    } else {
      order.setQuantity(quantity);
      order.setSubTotal(quantity * product.getPrice());
    }

    cart.setUpdatedAt(LocalDateTime.now());
    cartRep.save(cart);
  }

  private void createNewOrder(Cart cart, Product product, int quantity) {
    Order newOrder = new Order();
    newOrder.setOrderPk(new OrderPk(cart.getIdCart(), product.getIdProduct()));
    newOrder.setCart(cart);
    newOrder.setProduct(product);
    newOrder.setQuantity(quantity);
    newOrder.setSubTotal(quantity * product.getPrice());
    cart.addOrder(newOrder);
  }

  private void updateOrder(Order order, Product product, int quantity) {
    order.setQuantity(order.getQuantity() + quantity);
    order.setSubTotal(order.getQuantity() * product.getPrice());
  }

}
