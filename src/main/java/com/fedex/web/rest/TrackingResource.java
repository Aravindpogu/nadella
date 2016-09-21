package com.fedex.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.fedex.domain.Tracking;
import com.fedex.service.TrackingService;
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
 * REST controller for managing Tracking.
 */
@RestController
@RequestMapping("/api")
public class TrackingResource {

    private final Logger log = LoggerFactory.getLogger(TrackingResource.class);
        
    @Inject
    private TrackingService trackingService;
    
    /**
     * POST  /trackings : Create a new tracking.
     *
     * @param tracking the tracking to create
     * @return the ResponseEntity with status 201 (Created) and with body the new tracking, or with status 400 (Bad Request) if the tracking has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/trackings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tracking> createTracking(@RequestBody Tracking tracking) throws URISyntaxException {
        log.debug("REST request to save Tracking : {}", tracking);
        if (tracking.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("tracking", "idexists", "A new tracking cannot already have an ID")).body(null);
        }
        Tracking result = trackingService.save(tracking);
        return ResponseEntity.created(new URI("/api/trackings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("tracking", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /trackings : Updates an existing tracking.
     *
     * @param tracking the tracking to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated tracking,
     * or with status 400 (Bad Request) if the tracking is not valid,
     * or with status 500 (Internal Server Error) if the tracking couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/trackings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tracking> updateTracking(@RequestBody Tracking tracking) throws URISyntaxException {
        log.debug("REST request to update Tracking : {}", tracking);
        if (tracking.getId() == null) {
            return createTracking(tracking);
        }
        Tracking result = trackingService.save(tracking);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("tracking", tracking.getId().toString()))
            .body(result);
    }

    /**
     * GET  /trackings : get all the trackings.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of trackings in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/trackings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Tracking>> getAllTrackings(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Trackings");
        Page<Tracking> page = trackingService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/trackings");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /trackings/:id : get the "id" tracking.
     *
     * @param id the id of the tracking to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the tracking, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/trackings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Tracking> getTracking(@PathVariable Long id) {
        log.debug("REST request to get Tracking : {}", id);
        Tracking tracking = trackingService.findOne(id);
        return Optional.ofNullable(tracking)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /trackings/:id : delete the "id" tracking.
     *
     * @param id the id of the tracking to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/trackings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTracking(@PathVariable Long id) {
        log.debug("REST request to delete Tracking : {}", id);
        trackingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("tracking", id.toString())).build();
    }

}
