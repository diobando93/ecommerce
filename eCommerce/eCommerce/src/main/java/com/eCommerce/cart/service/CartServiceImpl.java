package com.eCommerce.cart.service;

import java.util.Optional;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.cart.repository.CartRep;
import com.eCommerce.orders.model.Orders;
import com.eCommerce.orders.model.OrdersPk;
import com.eCommerce.orders.repository.OrderRep;
import com.eCommerce.product.model.Product;
import com.eCommerce.product.repository.ProductRep;
import org.springframework.beans.factory.annotation.Autowired;
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
    Optional<Cart> cartOpt = cartRep.findById(idProduct);
    Optional<Product> productOpt = productRep.findById(idProduct);

    Cart cart = cartOpt.get();
    Product product = productOpt.get();

    if (cartOpt.isEmpty() || productOpt.isEmpty()) {
      throw new IllegalArgumentException("Cart or Product not found");
    }

    OrdersPk orderPk = new OrdersPk(idCart, idProduct);
    Orders order = orderRep.findById(orderPk).orElseGet(() -> {
      Orders newOrder = new Orders();
      newOrder.setOrderPk(orderPk);
      newOrder.setCart(cart);
      newOrder.setProduct(product);
      return newOrder;
    });

    order.setQuantity(order.getQuantity() + quantity);
    orderRep.save(order);

  }

  @Override
  public void updateProductQuantity(String idCart, String idProduct, int quantity) {

    OrdersPk orderPk = new OrdersPk(idCart, idProduct);
    Optional<Orders> optionalOrder = orderRep.findById(orderPk);

    if (optionalOrder.isEmpty()) {
      throw new IllegalArgumentException("Order not found");
    }

    Orders order = optionalOrder.get();
    order.setQuantity(quantity);
    orderRep.save(order);

  }

  @Override
  public void removeProductFromCart(String idCart, String idProduct) {

    OrdersPk orderPk = new OrdersPk(idCart, idProduct);

    if (!orderRep.existsById(orderPk)) {
      throw new IllegalArgumentException("Order not found");
    }

    orderRep.deleteById(orderPk);

  }

  @Override
  public Optional<Cart> getCart(String idCart) {

    return cartRep.findById(idCart);

  }

  @Override
  public void deleteCart(String idCart) {
    if (!cartRep.existsById(idCart)) {
      throw new IllegalArgumentException("Cart not found");
    }

    cartRep.deleteById(idCart);

  }

}
