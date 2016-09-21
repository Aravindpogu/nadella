package com.fedex.service;

import com.fedex.domain.Tracking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing Tracking.
 */
public interface TrackingService {

    /**
     * Save a tracking.
     * 
     * @param tracking the entity to save
     * @return the persisted entity
     */
    Tracking save(Tracking tracking);

    /**
     *  Get all the trackings.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<Tracking> findAll(Pageable pageable);

    /**
     *  Get the "id" tracking.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    Tracking findOne(Long id);

    /**
     *  Delete the "id" tracking.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
