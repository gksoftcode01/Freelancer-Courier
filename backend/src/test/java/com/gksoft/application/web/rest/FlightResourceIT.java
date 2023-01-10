package com.gksoft.application.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.application.IntegrationTest;
import com.gksoft.application.domain.Flight;
import com.gksoft.application.domain.enumeration.FlightStatus;
import com.gksoft.application.repository.FlightRepository;
import com.gksoft.application.service.FlightService;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link FlightResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class FlightResourceIT {

    private static final Instant DEFAULT_FLIGHT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FLIGHT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_MAX_WEIGHT = 1L;
    private static final Long UPDATED_MAX_WEIGHT = 2L;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Long DEFAULT_BUDGET = 1L;
    private static final Long UPDATED_BUDGET = 2L;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_TO_DOOR_AVAILABLE = false;
    private static final Boolean UPDATED_TO_DOOR_AVAILABLE = true;

    private static final FlightStatus DEFAULT_STATUS = FlightStatus.Available;
    private static final FlightStatus UPDATED_STATUS = FlightStatus.FullyBooked;

    private static final String ENTITY_API_URL = "/api/flights";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FlightRepository flightRepository;

    @Mock
    private FlightRepository flightRepositoryMock;

    @Mock
    private FlightService flightServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFlightMockMvc;

    private Flight flight;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flight createEntity(EntityManager em) {
        Flight flight = new Flight()
            .flightDate(DEFAULT_FLIGHT_DATE)
            .maxWeight(DEFAULT_MAX_WEIGHT)
            .notes(DEFAULT_NOTES)
            .budget(DEFAULT_BUDGET)
            .createDate(DEFAULT_CREATE_DATE)
            .toDoorAvailable(DEFAULT_TO_DOOR_AVAILABLE)
            .status(DEFAULT_STATUS);
        return flight;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Flight createUpdatedEntity(EntityManager em) {
        Flight flight = new Flight()
            .flightDate(UPDATED_FLIGHT_DATE)
            .maxWeight(UPDATED_MAX_WEIGHT)
            .notes(UPDATED_NOTES)
            .budget(UPDATED_BUDGET)
            .createDate(UPDATED_CREATE_DATE)
            .toDoorAvailable(UPDATED_TO_DOOR_AVAILABLE)
            .status(UPDATED_STATUS);
        return flight;
    }

    @BeforeEach
    public void initTest() {
        flight = createEntity(em);
    }

    @Test
    @Transactional
    void createFlight() throws Exception {
        int databaseSizeBeforeCreate = flightRepository.findAll().size();
        // Create the Flight
        restFlightMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isCreated());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeCreate + 1);
        Flight testFlight = flightList.get(flightList.size() - 1);
        assertThat(testFlight.getFlightDate()).isEqualTo(DEFAULT_FLIGHT_DATE);
        assertThat(testFlight.getMaxWeight()).isEqualTo(DEFAULT_MAX_WEIGHT);
        assertThat(testFlight.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testFlight.getBudget()).isEqualTo(DEFAULT_BUDGET);
        assertThat(testFlight.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testFlight.getToDoorAvailable()).isEqualTo(DEFAULT_TO_DOOR_AVAILABLE);
        assertThat(testFlight.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createFlightWithExistingId() throws Exception {
        // Create the Flight with an existing ID
        flight.setId(1L);

        int databaseSizeBeforeCreate = flightRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFlightMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isBadRequest());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFlights() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get all the flightList
        restFlightMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(flight.getId().intValue())))
            .andExpect(jsonPath("$.[*].flightDate").value(hasItem(DEFAULT_FLIGHT_DATE.toString())))
            .andExpect(jsonPath("$.[*].maxWeight").value(hasItem(DEFAULT_MAX_WEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].toDoorAvailable").value(hasItem(DEFAULT_TO_DOOR_AVAILABLE.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFlightsWithEagerRelationshipsIsEnabled() throws Exception {
        when(flightServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFlightMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(flightServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllFlightsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(flightServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restFlightMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(flightRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        // Get the flight
        restFlightMockMvc
            .perform(get(ENTITY_API_URL_ID, flight.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(flight.getId().intValue()))
            .andExpect(jsonPath("$.flightDate").value(DEFAULT_FLIGHT_DATE.toString()))
            .andExpect(jsonPath("$.maxWeight").value(DEFAULT_MAX_WEIGHT.intValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET.intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.toDoorAvailable").value(DEFAULT_TO_DOOR_AVAILABLE.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingFlight() throws Exception {
        // Get the flight
        restFlightMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        int databaseSizeBeforeUpdate = flightRepository.findAll().size();

        // Update the flight
        Flight updatedFlight = flightRepository.findById(flight.getId()).get();
        // Disconnect from session so that the updates on updatedFlight are not directly saved in db
        em.detach(updatedFlight);
        updatedFlight
            .flightDate(UPDATED_FLIGHT_DATE)
            .maxWeight(UPDATED_MAX_WEIGHT)
            .notes(UPDATED_NOTES)
            .budget(UPDATED_BUDGET)
            .createDate(UPDATED_CREATE_DATE)
            .toDoorAvailable(UPDATED_TO_DOOR_AVAILABLE)
            .status(UPDATED_STATUS);

        restFlightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedFlight.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedFlight))
            )
            .andExpect(status().isOk());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate);
        Flight testFlight = flightList.get(flightList.size() - 1);
        assertThat(testFlight.getFlightDate()).isEqualTo(UPDATED_FLIGHT_DATE);
        assertThat(testFlight.getMaxWeight()).isEqualTo(UPDATED_MAX_WEIGHT);
        assertThat(testFlight.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testFlight.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testFlight.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testFlight.getToDoorAvailable()).isEqualTo(UPDATED_TO_DOOR_AVAILABLE);
        assertThat(testFlight.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingFlight() throws Exception {
        int databaseSizeBeforeUpdate = flightRepository.findAll().size();
        flight.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, flight.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flight))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFlight() throws Exception {
        int databaseSizeBeforeUpdate = flightRepository.findAll().size();
        flight.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlightMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(flight))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFlight() throws Exception {
        int databaseSizeBeforeUpdate = flightRepository.findAll().size();
        flight.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlightMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFlightWithPatch() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        int databaseSizeBeforeUpdate = flightRepository.findAll().size();

        // Update the flight using partial update
        Flight partialUpdatedFlight = new Flight();
        partialUpdatedFlight.setId(flight.getId());

        partialUpdatedFlight.notes(UPDATED_NOTES).toDoorAvailable(UPDATED_TO_DOOR_AVAILABLE);

        restFlightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlight.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlight))
            )
            .andExpect(status().isOk());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate);
        Flight testFlight = flightList.get(flightList.size() - 1);
        assertThat(testFlight.getFlightDate()).isEqualTo(DEFAULT_FLIGHT_DATE);
        assertThat(testFlight.getMaxWeight()).isEqualTo(DEFAULT_MAX_WEIGHT);
        assertThat(testFlight.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testFlight.getBudget()).isEqualTo(DEFAULT_BUDGET);
        assertThat(testFlight.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testFlight.getToDoorAvailable()).isEqualTo(UPDATED_TO_DOOR_AVAILABLE);
        assertThat(testFlight.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateFlightWithPatch() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        int databaseSizeBeforeUpdate = flightRepository.findAll().size();

        // Update the flight using partial update
        Flight partialUpdatedFlight = new Flight();
        partialUpdatedFlight.setId(flight.getId());

        partialUpdatedFlight
            .flightDate(UPDATED_FLIGHT_DATE)
            .maxWeight(UPDATED_MAX_WEIGHT)
            .notes(UPDATED_NOTES)
            .budget(UPDATED_BUDGET)
            .createDate(UPDATED_CREATE_DATE)
            .toDoorAvailable(UPDATED_TO_DOOR_AVAILABLE)
            .status(UPDATED_STATUS);

        restFlightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFlight.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFlight))
            )
            .andExpect(status().isOk());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate);
        Flight testFlight = flightList.get(flightList.size() - 1);
        assertThat(testFlight.getFlightDate()).isEqualTo(UPDATED_FLIGHT_DATE);
        assertThat(testFlight.getMaxWeight()).isEqualTo(UPDATED_MAX_WEIGHT);
        assertThat(testFlight.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testFlight.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testFlight.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testFlight.getToDoorAvailable()).isEqualTo(UPDATED_TO_DOOR_AVAILABLE);
        assertThat(testFlight.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingFlight() throws Exception {
        int databaseSizeBeforeUpdate = flightRepository.findAll().size();
        flight.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFlightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, flight.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flight))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFlight() throws Exception {
        int databaseSizeBeforeUpdate = flightRepository.findAll().size();
        flight.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlightMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(flight))
            )
            .andExpect(status().isBadRequest());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFlight() throws Exception {
        int databaseSizeBeforeUpdate = flightRepository.findAll().size();
        flight.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFlightMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(flight)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Flight in the database
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFlight() throws Exception {
        // Initialize the database
        flightRepository.saveAndFlush(flight);

        int databaseSizeBeforeDelete = flightRepository.findAll().size();

        // Delete the flight
        restFlightMockMvc
            .perform(delete(ENTITY_API_URL_ID, flight.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Flight> flightList = flightRepository.findAll();
        assertThat(flightList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
