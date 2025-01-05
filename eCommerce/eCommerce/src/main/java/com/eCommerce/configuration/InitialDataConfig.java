package com.eCommerce.configuration;

import com.eCommerce.product.model.Product;
import com.eCommerce.product.repository.ProductRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.PostConstruct;

@Configuration
public class InitialDataConfig {

  @Autowired
  private ProductRep productRep;

  @PostConstruct
  public void initData() {

    // Creates products in the stratup of the app.
    Product laptop = new Product();
    laptop.setIdProduct("1");
    laptop.setName("Laptop");
    laptop.setPrice(1200.0);
    laptop.setDescription("High-end laptop");
    laptop.setAmount(10);

    Product smartphone = new Product();
    smartphone.setIdProduct("2");
    smartphone.setName("Smartphone");
    smartphone.setPrice(800.0);
    smartphone.setDescription("Latest model smartphone");
    smartphone.setAmount(50);

    Product tablet = new Product();
    tablet.setIdProduct("3");
    tablet.setName("Tablet");
    tablet.setPrice(400.0);
    tablet.setDescription("Latest model tablet");
    tablet.setAmount(30);

    productRep.save(laptop);
    productRep.save(smartphone);
    productRep.save(tablet);

  }

}
