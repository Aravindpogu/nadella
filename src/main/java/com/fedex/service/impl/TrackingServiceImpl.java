package com.fedex.service.impl;

import com.fedex.service.TrackingService;
import com.fedex.domain.Tracking;
import com.fedex.repository.TrackingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Tracking.
 */
@Service
@Transactional
public class TrackingServiceImpl implements TrackingService{

    private final Logger log = LoggerFactory.getLogger(TrackingServiceImpl.class);
    
    @Inject
    private TrackingRepository trackingRepository;
    
    /**
     * Save a tracking.
     * 
     * @param tracking the entity to save
     * @return the persisted entity
     */
    public Tracking save(Tracking tracking) {
        log.debug("Request to save Tracking : {}", tracking);
        Tracking result = trackingRepository.save(tracking);
        return result;
    }

    /**
     *  Get all the trackings.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Tracking> findAll(Pageable pageable) {
        log.debug("Request to get all Trackings");
        Page<Tracking> result = trackingRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one tracking by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Tracking findOne(Long id) {
        log.debug("Request to get Tracking : {}", id);
        Tracking tracking = trackingRepository.findOne(id);
        return tracking;
    }

    /**
     *  Delete the  tracking by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Tracking : {}", id);
        trackingRepository.delete(id);
    }
}
