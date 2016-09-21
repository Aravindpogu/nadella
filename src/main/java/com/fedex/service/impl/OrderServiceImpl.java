package com.fedex.service.impl;

import com.fedex.service.OrderService;
import com.fedex.domain.Order;
import com.fedex.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Service Implementation for managing Order.
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService{

    private final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);
    
    @Inject
    private OrderRepository orderRepository;
    
    /**
     * Save a order.
     * 
     * @param order the entity to save
     * @return the persisted entity
     */
    public Order save(Order order) {
        log.debug("Request to save Order : {}", order);
        Order result = orderRepository.save(order);
        return result;
    }

    /**
     *  Get all the orders.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Order> findAll(Pageable pageable) {
        log.debug("Request to get all Orders");
        Page<Order> result = orderRepository.findAll(pageable); 
        return result;
    }


    /**
     *  get all the orders where Tracking is null.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Order> findAllWhereTrackingIsNull() {
        log.debug("Request to get all orders where Tracking is null");
        return StreamSupport
            .stream(orderRepository.findAll().spliterator(), false)
            .filter(order -> order.getTracking() == null)
            .collect(Collectors.toList());
    }

    /**
     *  Get one order by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Order findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        Order order = orderRepository.findOne(id);
        return order;
    }

    /**
     *  Delete the  order by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        orderRepository.delete(id);
    }
}
