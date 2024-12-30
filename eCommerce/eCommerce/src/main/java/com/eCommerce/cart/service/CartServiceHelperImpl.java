package com.eCommerce.cart.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.eCommerce.cart.dto.CartDtoIn;
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
    if (cartOpt.isEmpty()) throw new ApiException("Cart with ID '" + idCart + "' not found", HttpStatus.CONFLICT);
    return cartOpt.get();
  }

  @Override
  public Product searchProductById(String idProduct) {

    Optional<Product> productOpt = productRep.findById(idProduct);
    if (productOpt.isEmpty()) throw new ApiException("Product with ID '" + idProduct + "' not found", HttpStatus.CONFLICT);
    return productOpt.get();

  }

  @Override
  public boolean chkCartDtoInList(List<CartDtoIn> cartDtoInList) {

    if (cartDtoInList.isEmpty()) throw new ApiException("We can´t add products list empty", HttpStatus.CONFLICT);

    if (!chkAllCartAreTheSame(cartDtoInList)) throw new ApiException("The list have differents carts", HttpStatus.CONFLICT);

    return true;
  }

  @Override
  public boolean chkDatainCartDto(CartDtoIn cartDtoIn) {

    if (cartDtoIn.getIdCart() == null) throw new ApiException("IdCart must not be null", HttpStatus.CONFLICT);
    if (cartDtoIn.getIdProduct() == null) throw new ApiException("IdProduct must not be null", HttpStatus.CONFLICT);
    if (cartDtoIn.getQuantity() == null) throw new ApiException("Quantity of product must not be null", HttpStatus.CONFLICT);

    return true;
  }

  @Override
  public void createOrUpdateCart(Cart cart, Product product, int quantity) {

    if (quantity > 0 && chkStockOfProdcut(product, quantity)) {

      OrdersPk orderPk = new OrdersPk(cart.getIdCart(), product.getIdProduct());
      Optional<Orders> orderOpt = orderRep.findById(orderPk);

      if (!orderOpt.isEmpty()) {
        // update order and cart
        updateOrder(orderOpt.get(), product, quantity);
        cart.setUpdatedAt(LocalDateTime.now());
        orderRep.save(orderOpt.get());
        cartRep.save(cart);
      } else {
        // create order and update cart
        createNewOrder(orderPk, cart, product, quantity);
        cart.setUpdatedAt(LocalDateTime.now());
        cartRep.save(cart);
      }

    } else {
      if (quantity < 0)
        throw new ApiException("The quantity of Product with ID '" + product.getIdProduct() + "' must be greater than 0", HttpStatus.CONFLICT);
      throw new ApiException("The quantity of Product with ID '" + product.getIdProduct() + "' not in stock", HttpStatus.CONFLICT);
    }

  }

  @Override
  public void updateCart(Cart cart, Product product, int quantity) {

    OrdersPk orderPk = new OrdersPk(cart.getIdCart(), product.getIdProduct());
    Optional<Orders> orderOpt = orderRep.findById(orderPk);

    if (orderOpt.isEmpty())
      throw new ApiException("Order for idCart/idProduct: '" + cart.getIdCart() + "'/" + product.getIdProduct() + "' not exits", HttpStatus.CONFLICT);

    Orders order = orderOpt.get();

    if (quantity > 0 && chkStockOfProdcut(product, quantity)) {

      order.setQuantity(quantity);
      order.setSubTotal(quantity * product.getPrice());
      cart.setUpdatedAt(LocalDateTime.now());
      orderRep.save(order);
      cartRep.save(cart);
    } else if (quantity == 0) {

      orderRep.deleteById(orderPk);
      cart.setUpdatedAt(LocalDateTime.now());

    } else {

      if (quantity < 0)
        throw new ApiException("The quantity of Product with ID '" + product.getIdProduct() + "' must be greater than 0", HttpStatus.CONFLICT);
      throw new ApiException("The quantity of Product with ID '" + product.getIdProduct() + "' not in stock", HttpStatus.CONFLICT);
    }

  }

  @Override
  public boolean chkStockOfProdcut(Product product, int quantity) {

    return product.getAmount() > quantity;
  }

  @Override
  public CartDtoOut convertCartToDtoOut(Cart cart) {

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

  public boolean chkAllCartAreTheSame(List<CartDtoIn> cartDtoInList) {
    return cartDtoInList.stream().map(CartDtoIn::getIdCart).distinct().count() == 1;
  }

  public void createNewOrder(OrdersPk orderPk, Cart cart, Product product, int quantity) {
    Orders newOrder = new Orders();
    newOrder.setOrderPk(orderPk);
    newOrder.setCart(cart);
    newOrder.setProduct(product);
    newOrder.setQuantity(quantity);
    newOrder.setSubTotal(newOrder.getQuantity() * product.getPrice());
    orderRep.save(newOrder);
  }

  public void updateOrder(Orders order, Product product, int quantity) {
    order.setQuantity(quantity + order.getQuantity());
    order.setSubTotal(order.getQuantity() * product.getPrice());
  }

  @Override
  public void deleteCart(String idCart) {

    if (idCart == null) throw new ApiException("IdCart must not be null", HttpStatus.CONFLICT);
    if (!cartRep.existsById(idCart)) throw new ApiException("Cart with ID '" + idCart + "' not exits", HttpStatus.CONFLICT);

    cartRep.deleteById(idCart);

  }

  @Override
  public void removeProductFromCart(String idCart, String idProduct) {

    if (idCart == null) throw new ApiException("IdCart must not be null", HttpStatus.CONFLICT);
    if (idProduct == null) throw new ApiException("IdProduct must not be null", HttpStatus.CONFLICT);

    OrdersPk orderPk = new OrdersPk(idCart, idProduct);
    Optional<Orders> optionalOrder = orderRep.findById(orderPk);

    if (optionalOrder.isEmpty())
      throw new ApiException("Order for idCart/idProduct'" + idProduct + "'/" + idCart + "' not exits", HttpStatus.CONFLICT);

    orderRep.deleteById(orderPk);

  }

  @Override
  public Cart createCart() {
    Cart cart = new Cart();
    return cartRep.save(cart);
  }

}
