package com.eCommerce.tasks;

import java.time.LocalDateTime;
import java.util.List;
import com.eCommerce.cart.model.Cart;
import com.eCommerce.cart.repository.CartRep;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CartCleanupTask {

  private static final Logger logger = LoggerFactory.getLogger(CartCleanupTask.class);

  @Value("${cart.cleanup.minutes}") // El valor será configurable en application.properties
  private int                 minutes;

  @Autowired
  private CartRep             cartRep;

  // Trigger task every minute.
  @Scheduled(fixedRate = 60000)
  public void cleanupInactiveCarts() {
    LocalDateTime cutoffTime = LocalDateTime.now().minusMinutes(minutes);
    List<Cart> cartList = cartRep.searchByLastActivityBefore(cutoffTime);
    for (Cart cart : cartList) {
      logger.info("Deleted inactive cart: {}", cart.getIdCart());
      cartRep.deleteById(cart.getIdCart());
    }

  }

}
