package com.fedex.service.impl;

import com.fedex.service.ItemService;
import com.fedex.domain.Item;
import com.fedex.repository.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Item.
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService{

    private final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);
    
    @Inject
    private ItemRepository itemRepository;
    
    /**
     * Save a item.
     * 
     * @param item the entity to save
     * @return the persisted entity
     */
    public Item save(Item item) {
        log.debug("Request to save Item : {}", item);
        Item result = itemRepository.save(item);
        return result;
    }

    /**
     *  Get all the items.
     *  
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Item> findAll() {
        log.debug("Request to get all Items");
        List<Item> result = itemRepository.findAll();
        return result;
    }

    /**
     *  Get one item by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Item findOne(Long id) {
        log.debug("Request to get Item : {}", id);
        Item item = itemRepository.findOne(id);
        return item;
    }

    /**
     *  Delete the  item by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Item : {}", id);
        itemRepository.delete(id);
    }
}
