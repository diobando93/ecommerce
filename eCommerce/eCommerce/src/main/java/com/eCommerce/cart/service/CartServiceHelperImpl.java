package com.eCommerce.cart.service;

import java.time.LocalDateTime;
import java.util.Optional;
import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.cart.repository.CartRep;
import com.eCommerce.exception.ApiException;
import com.eCommerce.orders.dto.OrderDtoOut;
import com.eCommerce.orders.model.Orders;
import com.eCommerce.orders.model.OrdersPk;
import com.eCommerce.orders.repository.OrderRep;
import com.eCommerce.product.model.Product;
import com.eCommerce.product.repository.ProductRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CartServiceHelperImpl implements CartServiceHelper {

  @Autowired
  private CartRep    cartRep;

  @Autowired
  private ProductRep productRep;

  @Autowired
  private OrderRep   orderRep;

  @Override
  public Cart searchCartById(String idCart) {

    Optional<Cart> cartOpt = cartRep.findById(idCart);
    if (cartOpt.isEmpty()) throw new ApiException("Cart with ID '" + idCart + "' not found", HttpStatus.NOT_FOUND);
    return cartOpt.get();
  }

  @Override
  public Product searchProductById(String idProduct) {

    Optional<Product> productOpt = productRep.findById(idProduct);
    if (productOpt.isEmpty()) throw new ApiException("Product with ID '" + idProduct + "' not found", HttpStatus.NOT_FOUND);
    return productOpt.get();

  }

  @Override
  public CartDtoOut createOrUpdateOrder(Cart cart, Product product, int quantity) {

    if (chkStockOfProdcut(product, quantity)) {

      OrdersPk orderPk = new OrdersPk(cart.getIdCart(), product.getIdProduct());
      Optional<Orders> orderOpt = orderRep.findById(orderPk);

      if (!orderOpt.isEmpty()) {
        // update order and cart
        Orders order = orderOpt.get();
        order.setQuantity(quantity + order.getQuantity());
        order.setSubTotal(order.getQuantity() * product.getPrice());
        cart.setUpdatedAt(LocalDateTime.now());
        return convertToCartDto(cart);
      } else {
        // create order and update cart
        Orders newOrder = new Orders();
        newOrder.setOrderPk(orderPk);
        newOrder.setCart(cart);
        newOrder.setProduct(product);
        newOrder.setQuantity(quantity);
        newOrder.setSubTotal(newOrder.getQuantity() * product.getPrice());
        cart.setUpdatedAt(LocalDateTime.now());
        orderRep.save(newOrder);
        return convertToCartDto(cart);
      }

    } else {
      throw new ApiException("Product with ID '" + product.getIdProduct() + "' not found", HttpStatus.NOT_FOUND);
    }

  }

  @Override
  public boolean chkStockOfProdcut(Product product, int quantity) {

    return product.getAmount() > quantity;
  }

  @Override
  public CartDtoOut convertToCartDto(Cart cart) {

    CartDtoOut cartDtoOut = new CartDtoOut();
    cartDtoOut.setIdCart(cart.getIdCart());
    cartDtoOut.setOrders(cart.getOrders().stream()
        .map(order -> new OrderDtoOut(order.getProduct().getIdProduct(), order.getQuantity(), order.getSubTotal())).toList());
    double totalPrice = cart.getOrders().stream().mapToDouble(Orders::getSubTotal).sum();
    cart.setTotalPrice(totalPrice);
    cartDtoOut.setTotalPrice(totalPrice);
    cartDtoOut.setTotalPrice(cart.getTotalPrice());
    cartDtoOut.setCreatedAt(cart.getCreatedAt());
    cartDtoOut.setUpdatedAt(cart.getUpdatedAt());
    cartDtoOut.setStatus(cart.getStatus());

    return cartDtoOut;
  }

}
