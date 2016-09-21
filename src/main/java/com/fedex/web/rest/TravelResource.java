package com.fedex.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fedex.domain.Travel;
import com.fedex.service.TravelService;
import com.fedex.web.rest.util.HeaderUtil;
import com.fedex.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Travel.
 */
@RestController
@RequestMapping("/api")
public class TravelResource {

    private final Logger log = LoggerFactory.getLogger(TravelResource.class);
        
    @Inject
    private TravelService travelService;
    
    /**
     * POST  /travels : Create a new travel.
     *
     * @param travel the travel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new travel, or with status 400 (Bad Request) if the travel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/travels",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Travel> createTravel(@RequestBody Travel travel) throws URISyntaxException {
        log.debug("REST request to save Travel : {}", travel);
        if (travel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("travel", "idexists", "A new travel cannot already have an ID")).body(null);
        }
        Travel result = travelService.save(travel);
        return ResponseEntity.created(new URI("/api/travels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("travel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /travels : Updates an existing travel.
     *
     * @param travel the travel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated travel,
     * or with status 400 (Bad Request) if the travel is not valid,
     * or with status 500 (Internal Server Error) if the travel couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/travels",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Travel> updateTravel(@RequestBody Travel travel) throws URISyntaxException {
        log.debug("REST request to update Travel : {}", travel);
        if (travel.getId() == null) {
            return createTravel(travel);
        }
        Travel result = travelService.save(travel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("travel", travel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /travels : get all the travels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of travels in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/travels",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Travel>> getAllTravels(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Travels");
        Page<Travel> page = travelService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/travels");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /travels/:id : get the "id" travel.
     *
     * @param id the id of the travel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the travel, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/travels/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Travel> getTravel(@PathVariable Long id) {
        log.debug("REST request to get Travel : {}", id);
        Travel travel = travelService.findOne(id);
        return Optional.ofNullable(travel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /travels/:id : delete the "id" travel.
     *
     * @param id the id of the travel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/travels/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTravel(@PathVariable Long id) {
        log.debug("REST request to delete Travel : {}", id);
        travelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("travel", id.toString())).build();
    }

}
