package com.fedex.web.rest;

import com.fedex.NadellaApp;
import com.fedex.domain.Travel;
import com.fedex.repository.TravelRepository;
import com.fedex.service.TravelService;

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
 * Test class for the TravelResource REST controller.
 *
 * @see TravelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = NadellaApp.class)
@WebAppConfiguration
@IntegrationTest
public class TravelResourceIntTest {

    private static final String DEFAULT_FROM_PLACE = "AAAAA";
    private static final String UPDATED_FROM_PLACE = "BBBBB";
    private static final String DEFAULT_TO_PLACE = "AAAAA";
    private static final String UPDATED_TO_PLACE = "BBBBB";

    private static final LocalDate DEFAULT_FROM_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_FROM_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_TO_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TO_DATE = LocalDate.now(ZoneId.systemDefault());

    @Inject
    private TravelRepository travelRepository;

    @Inject
    private TravelService travelService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restTravelMockMvc;

    private Travel travel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TravelResource travelResource = new TravelResource();
        ReflectionTestUtils.setField(travelResource, "travelService", travelService);
        this.restTravelMockMvc = MockMvcBuilders.standaloneSetup(travelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        travel = new Travel();
        travel.setFrom_place(DEFAULT_FROM_PLACE);
        travel.setTo_place(DEFAULT_TO_PLACE);
        travel.setFrom_date(DEFAULT_FROM_DATE);
        travel.setTo_date(DEFAULT_TO_DATE);
    }

    @Test
    @Transactional
    public void createTravel() throws Exception {
        int databaseSizeBeforeCreate = travelRepository.findAll().size();

        // Create the Travel

        restTravelMockMvc.perform(post("/api/travels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(travel)))
                .andExpect(status().isCreated());

        // Validate the Travel in the database
        List<Travel> travels = travelRepository.findAll();
        assertThat(travels).hasSize(databaseSizeBeforeCreate + 1);
        Travel testTravel = travels.get(travels.size() - 1);
        assertThat(testTravel.getFrom_place()).isEqualTo(DEFAULT_FROM_PLACE);
        assertThat(testTravel.getTo_place()).isEqualTo(DEFAULT_TO_PLACE);
        assertThat(testTravel.getFrom_date()).isEqualTo(DEFAULT_FROM_DATE);
        assertThat(testTravel.getTo_date()).isEqualTo(DEFAULT_TO_DATE);
    }

    @Test
    @Transactional
    public void getAllTravels() throws Exception {
        // Initialize the database
        travelRepository.saveAndFlush(travel);

        // Get all the travels
        restTravelMockMvc.perform(get("/api/travels?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(travel.getId().intValue())))
                .andExpect(jsonPath("$.[*].from_place").value(hasItem(DEFAULT_FROM_PLACE.toString())))
                .andExpect(jsonPath("$.[*].to_place").value(hasItem(DEFAULT_TO_PLACE.toString())))
                .andExpect(jsonPath("$.[*].from_date").value(hasItem(DEFAULT_FROM_DATE.toString())))
                .andExpect(jsonPath("$.[*].to_date").value(hasItem(DEFAULT_TO_DATE.toString())));
    }

    @Test
    @Transactional
    public void getTravel() throws Exception {
        // Initialize the database
        travelRepository.saveAndFlush(travel);

        // Get the travel
        restTravelMockMvc.perform(get("/api/travels/{id}", travel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(travel.getId().intValue()))
            .andExpect(jsonPath("$.from_place").value(DEFAULT_FROM_PLACE.toString()))
            .andExpect(jsonPath("$.to_place").value(DEFAULT_TO_PLACE.toString()))
            .andExpect(jsonPath("$.from_date").value(DEFAULT_FROM_DATE.toString()))
            .andExpect(jsonPath("$.to_date").value(DEFAULT_TO_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingTravel() throws Exception {
        // Get the travel
        restTravelMockMvc.perform(get("/api/travels/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTravel() throws Exception {
        // Initialize the database
        travelService.save(travel);

        int databaseSizeBeforeUpdate = travelRepository.findAll().size();

        // Update the travel
        Travel updatedTravel = new Travel();
        updatedTravel.setId(travel.getId());
        updatedTravel.setFrom_place(UPDATED_FROM_PLACE);
        updatedTravel.setTo_place(UPDATED_TO_PLACE);
        updatedTravel.setFrom_date(UPDATED_FROM_DATE);
        updatedTravel.setTo_date(UPDATED_TO_DATE);

        restTravelMockMvc.perform(put("/api/travels")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTravel)))
                .andExpect(status().isOk());

        // Validate the Travel in the database
        List<Travel> travels = travelRepository.findAll();
        assertThat(travels).hasSize(databaseSizeBeforeUpdate);
        Travel testTravel = travels.get(travels.size() - 1);
        assertThat(testTravel.getFrom_place()).isEqualTo(UPDATED_FROM_PLACE);
        assertThat(testTravel.getTo_place()).isEqualTo(UPDATED_TO_PLACE);
        assertThat(testTravel.getFrom_date()).isEqualTo(UPDATED_FROM_DATE);
        assertThat(testTravel.getTo_date()).isEqualTo(UPDATED_TO_DATE);
    }

    @Test
    @Transactional
    public void deleteTravel() throws Exception {
        // Initialize the database
        travelService.save(travel);

        int databaseSizeBeforeDelete = travelRepository.findAll().size();

        // Get the travel
        restTravelMockMvc.perform(delete("/api/travels/{id}", travel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Travel> travels = travelRepository.findAll();
        assertThat(travels).hasSize(databaseSizeBeforeDelete - 1);
    }
}
