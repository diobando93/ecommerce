package com.eCommerce.cart.service;

import java.time.LocalDateTime;
import java.util.Optional;
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

@Service("cartService")
public class CartServiceImpl implements CartService {

  @Autowired
  private CartRep    cartRep;

  @Autowired
  private ProductRep productRep;

  @Autowired
  private OrderRep   orderRep;

  @Override
  public Cart createCart() {

    Cart cart = new Cart();
    return cartRep.save(cart);

  }

  @Override
  public void addProductToCart(String idCart, String idProduct, int quantity) {

    Optional<Cart> cartOpt = cartRep.findById(idCart);
    Optional<Product> productOpt = productRep.findById(idProduct);

    if (cartOpt.isEmpty()) throw new ApiException("Cart with ID '" + idCart + "' not found", HttpStatus.NOT_FOUND);

    if (productOpt.isEmpty()) throw new ApiException("Product with ID '" + idProduct + "' not found", HttpStatus.NOT_FOUND);

    Cart cart = cartOpt.get();
    Product product = productOpt.get();
    OrdersPk orderPk = new OrdersPk(idCart, idProduct);
    Orders order = orderRep.findById(orderPk).orElseGet(() -> {
      Orders newOrder = new Orders();
      newOrder.setOrderPk(orderPk);
      newOrder.setCart(cart);
      newOrder.setProduct(product);
      return newOrder;
    });

    order.setQuantity(order.getQuantity() + quantity);
    cart.setUpdatedAt(LocalDateTime.now());
    orderRep.save(order);

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
  public Optional<Cart> getCart(String idCart) {

    Optional<Cart> cartOpt = cartRep.findById(idCart);
    if (cartOpt.isEmpty()) throw new ApiException("Cart with ID '" + idCart + "' not found", HttpStatus.NOT_FOUND);
    // update date if the cart have prudcts
    if (!cartOpt.get().getOrders().isEmpty()) cartOpt.get().setUpdatedAt(LocalDateTime.now());
    return cartOpt;

  }

  @Override
  public void deleteCart(String idCart) {

    if (!cartRep.existsById(idCart)) throw new ApiException("Cart with ID '" + idCart + "' not found", HttpStatus.NOT_FOUND);

    cartRep.deleteById(idCart);

  }

}
