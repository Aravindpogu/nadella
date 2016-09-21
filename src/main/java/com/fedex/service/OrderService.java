package com.fedex.service;

import com.fedex.domain.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Order.
 */
public interface OrderService {

    /**
     * Save a order.
     * 
     * @param order the entity to save
     * @return the persisted entity
     */
    Order save(Order order);

    /**
     *  Get all the orders.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Order> findAll(Pageable pageable);
    /**
     *  Get all the orders where Tracking is null.
     *  
     *  @return the list of entities
     */
    List<Order> findAllWhereTrackingIsNull();

    /**
     *  Get the "id" order.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Order findOne(Long id);

    /**
     *  Delete the "id" order.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
