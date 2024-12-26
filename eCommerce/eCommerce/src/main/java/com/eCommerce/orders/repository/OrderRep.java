package com.eCommerce.orders.repository;

import com.eCommerce.orders.model.Orders;
import com.eCommerce.orders.model.OrdersPk;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRep extends JpaRepository<Orders, OrdersPk> {

}
