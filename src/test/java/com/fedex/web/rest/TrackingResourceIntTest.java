package com.fedex.web.rest;

import com.fedex.NadellaApp;
import com.fedex.domain.Tracking;
import com.fedex.repository.TrackingRepository;
import com.fedex.service.TrackingService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the TrackingResource REST controller.
 *
 * @see TrackingResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NadellaApp.class)
@WebAppConfiguration
@IntegrationTest
public class TrackingResourceIntTest {


    private static final LocalDate DEFAULT_ORDER_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORDER_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ORDER_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORDER_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ACTUAL_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ACTUAL_DELIVERY_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private TrackingRepository trackingRepository;

    @Inject
    private TrackingService trackingService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTrackingMockMvc;

    private Tracking tracking;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TrackingResource trackingResource = new TrackingResource();
        ReflectionTestUtils.setField(trackingResource, "trackingService", trackingService);
        this.restTrackingMockMvc = MockMvcBuilders.standaloneSetup(trackingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        tracking = new Tracking();
        tracking.setOrder_start_date(DEFAULT_ORDER_START_DATE);
        tracking.setOrder_end_date(DEFAULT_ORDER_END_DATE);
        tracking.setActual_delivery_date(DEFAULT_ACTUAL_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void createTracking() throws Exception {
        int databaseSizeBeforeCreate = trackingRepository.findAll().size();

        // Create the Tracking

        restTrackingMockMvc.perform(post("/api/trackings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(tracking)))
                .andExpect(status().isCreated());

        // Validate the Tracking in the database
        List<Tracking> trackings = trackingRepository.findAll();
        assertThat(trackings).hasSize(databaseSizeBeforeCreate + 1);
        Tracking testTracking = trackings.get(trackings.size() - 1);
        assertThat(testTracking.getOrder_start_date()).isEqualTo(DEFAULT_ORDER_START_DATE);
        assertThat(testTracking.getOrder_end_date()).isEqualTo(DEFAULT_ORDER_END_DATE);
        assertThat(testTracking.getActual_delivery_date()).isEqualTo(DEFAULT_ACTUAL_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void getAllTrackings() throws Exception {
        // Initialize the database
        trackingRepository.saveAndFlush(tracking);

        // Get all the trackings
        restTrackingMockMvc.perform(get("/api/trackings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(tracking.getId().intValue())))
                .andExpect(jsonPath("$.[*].order_start_date").value(hasItem(DEFAULT_ORDER_START_DATE.toString())))
                .andExpect(jsonPath("$.[*].order_end_date").value(hasItem(DEFAULT_ORDER_END_DATE.toString())))
                .andExpect(jsonPath("$.[*].actual_delivery_date").value(hasItem(DEFAULT_ACTUAL_DELIVERY_DATE.toString())));
    }

    @Test
    @Transactional
    public void getTracking() throws Exception {
        // Initialize the database
        trackingRepository.saveAndFlush(tracking);

        // Get the tracking
        restTrackingMockMvc.perform(get("/api/trackings/{id}", tracking.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(tracking.getId().intValue()))
            .andExpect(jsonPath("$.order_start_date").value(DEFAULT_ORDER_START_DATE.toString()))
            .andExpect(jsonPath("$.order_end_date").value(DEFAULT_ORDER_END_DATE.toString()))
            .andExpect(jsonPath("$.actual_delivery_date").value(DEFAULT_ACTUAL_DELIVERY_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTracking() throws Exception {
        // Get the tracking
        restTrackingMockMvc.perform(get("/api/trackings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTracking() throws Exception {
        // Initialize the database
        trackingService.save(tracking);

        int databaseSizeBeforeUpdate = trackingRepository.findAll().size();

        // Update the tracking
        Tracking updatedTracking = new Tracking();
        updatedTracking.setId(tracking.getId());
        updatedTracking.setOrder_start_date(UPDATED_ORDER_START_DATE);
        updatedTracking.setOrder_end_date(UPDATED_ORDER_END_DATE);
        updatedTracking.setActual_delivery_date(UPDATED_ACTUAL_DELIVERY_DATE);

        restTrackingMockMvc.perform(put("/api/trackings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTracking)))
                .andExpect(status().isOk());

        // Validate the Tracking in the database
        List<Tracking> trackings = trackingRepository.findAll();
        assertThat(trackings).hasSize(databaseSizeBeforeUpdate);
        Tracking testTracking = trackings.get(trackings.size() - 1);
        assertThat(testTracking.getOrder_start_date()).isEqualTo(UPDATED_ORDER_START_DATE);
        assertThat(testTracking.getOrder_end_date()).isEqualTo(UPDATED_ORDER_END_DATE);
        assertThat(testTracking.getActual_delivery_date()).isEqualTo(UPDATED_ACTUAL_DELIVERY_DATE);
    }

    @Test
    @Transactional
    public void deleteTracking() throws Exception {
        // Initialize the database
        trackingService.save(tracking);

        int databaseSizeBeforeDelete = trackingRepository.findAll().size();

        // Get the tracking
        restTrackingMockMvc.perform(delete("/api/trackings/{id}", tracking.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Tracking> trackings = trackingRepository.findAll();
        assertThat(trackings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
