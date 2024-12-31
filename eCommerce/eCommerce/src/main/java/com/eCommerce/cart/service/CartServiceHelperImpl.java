package com.eCommerce.cart.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.eCommerce.cart.dto.CartDtoIn;
import com.eCommerce.cart.dto.CartDtoOut;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.cart.repository.CartRep;
import com.eCommerce.exception.ApiErrorCode;
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
    if (cartOpt.isEmpty()) throw new ApiException(ApiErrorCode.CART_NOT_FOUND.getMessage(idCart), ApiErrorCode.CART_NOT_FOUND.getStatus());
    return cartOpt.get();
  }

  @Override
  public Product searchProductById(String idProduct) {

    Optional<Product> productOpt = productRep.findById(idProduct);
    if (productOpt.isEmpty())
      throw new ApiException(ApiErrorCode.PRODUCT_NOT_FOUND.getMessage(idProduct), ApiErrorCode.PRODUCT_NOT_FOUND.getStatus());
    return productOpt.get();

  }

  @Override
  public boolean chkCartDtoInList(List<CartDtoIn> cartDtoInList) {

    if (cartDtoInList.isEmpty()) throw new ApiException(ApiErrorCode.EMPTY_CART_LIST.getMessage(), ApiErrorCode.EMPTY_CART_LIST.getStatus());

    if (!chkAllCartAreTheSame(cartDtoInList))
      throw new ApiException(ApiErrorCode.DIFFERENT_CARTS.getMessage(), ApiErrorCode.DIFFERENT_CARTS.getStatus());

    return true;
  }

  @Override
  public boolean chkDatainCartDto(CartDtoIn cartDtoIn) {

    if (cartDtoIn.getIdCart() == null) throw new ApiException(ApiErrorCode.CART_ID_NULL.getMessage(), ApiErrorCode.CART_ID_NULL.getStatus());
    if (cartDtoIn.getIdProduct() == null) throw new ApiException(ApiErrorCode.PRODUCT_ID_NULL.getMessage(), ApiErrorCode.PRODUCT_ID_NULL.getStatus());
    if (cartDtoIn.getQuantity() == null) throw new ApiException(ApiErrorCode.QUANTITY_NULL.getMessage(), ApiErrorCode.QUANTITY_NULL.getStatus());

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
        throw new ApiException(ApiErrorCode.INVALID_QUANTITY.getMessage(product.getIdProduct()), ApiErrorCode.INVALID_QUANTITY.getStatus());
      throw new ApiException(ApiErrorCode.INSUFFICIENT_STOCK.getMessage(product.getIdProduct()), ApiErrorCode.INSUFFICIENT_STOCK.getStatus());
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
        throw new ApiException(ApiErrorCode.INVALID_QUANTITY.getMessage(product.getIdProduct()), ApiErrorCode.INVALID_QUANTITY.getStatus());
      throw new ApiException(ApiErrorCode.INSUFFICIENT_STOCK.getMessage(product.getIdProduct()), ApiErrorCode.INSUFFICIENT_STOCK.getStatus());
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

    if (idCart == null) throw new ApiException(ApiErrorCode.CART_ID_NULL.getMessage(), ApiErrorCode.CART_ID_NULL.getStatus());
    if (!cartRep.existsById(idCart)) throw new ApiException(ApiErrorCode.CART_NOT_FOUND.getMessage(idCart), ApiErrorCode.CART_NOT_FOUND.getStatus());

    cartRep.deleteById(idCart);

  }

  @Override
  public Cart createCart() {
    Cart cart = new Cart();
    return cartRep.save(cart);
  }

}
