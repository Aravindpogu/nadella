package com.fedex.service;

import com.fedex.domain.Item;

import java.util.List;

/**
 * Service Interface for managing Item.
 */
public interface ItemService {

    /**
     * Save a item.
     * 
     * @param item the entity to save
     * @return the persisted entity
     */
    Item save(Item item);

    /**
     *  Get all the items.
     *  
     *  @return the list of entities
     */
    List<Item> findAll();

    /**
     *  Get the "id" item.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Item findOne(Long id);

    /**
     *  Delete the "id" item.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
