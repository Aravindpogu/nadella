package com.fedex.service;

import com.fedex.domain.Travel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Travel.
 */
public interface TravelService {

    /**
     * Save a travel.
     * 
     * @param travel the entity to save
     * @return the persisted entity
     */
    Travel save(Travel travel);

    /**
     *  Get all the travels.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Travel> findAll(Pageable pageable);

    /**
     *  Get the "id" travel.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Travel findOne(Long id);

    /**
     *  Delete the "id" travel.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
