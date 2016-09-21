package com.fedex.service.impl;

import com.fedex.service.TravelService;
import com.fedex.domain.Travel;
import com.fedex.repository.TravelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Travel.
 */
@Service
@Transactional
public class TravelServiceImpl implements TravelService{

    private final Logger log = LoggerFactory.getLogger(TravelServiceImpl.class);
    
    @Inject
    private TravelRepository travelRepository;
    
    /**
     * Save a travel.
     * 
     * @param travel the entity to save
     * @return the persisted entity
     */
    public Travel save(Travel travel) {
        log.debug("Request to save Travel : {}", travel);
        Travel result = travelRepository.save(travel);
        return result;
    }

    /**
     *  Get all the travels.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Travel> findAll(Pageable pageable) {
        log.debug("Request to get all Travels");
        Page<Travel> result = travelRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one travel by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Travel findOne(Long id) {
        log.debug("Request to get Travel : {}", id);
        Travel travel = travelRepository.findOne(id);
        return travel;
    }

    /**
     *  Delete the  travel by id.
     *  
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Travel : {}", id);
        travelRepository.delete(id);
    }
}
