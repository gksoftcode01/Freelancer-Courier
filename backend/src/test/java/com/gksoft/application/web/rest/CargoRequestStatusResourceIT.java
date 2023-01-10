package com.gksoft.application.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.application.IntegrationTest;
import com.gksoft.application.domain.CargoRequestStatus;
import com.gksoft.application.repository.CargoRequestStatusRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link CargoRequestStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CargoRequestStatusResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/cargo-request-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CargoRequestStatusRepository cargoRequestStatusRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCargoRequestStatusMockMvc;

    private CargoRequestStatus cargoRequestStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CargoRequestStatus createEntity(EntityManager em) {
        CargoRequestStatus cargoRequestStatus = new CargoRequestStatus().name(DEFAULT_NAME);
        return cargoRequestStatus;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CargoRequestStatus createUpdatedEntity(EntityManager em) {
        CargoRequestStatus cargoRequestStatus = new CargoRequestStatus().name(UPDATED_NAME);
        return cargoRequestStatus;
    }

    @BeforeEach
    public void initTest() {
        cargoRequestStatus = createEntity(em);
    }

    @Test
    @Transactional
    void createCargoRequestStatus() throws Exception {
        int databaseSizeBeforeCreate = cargoRequestStatusRepository.findAll().size();
        // Create the CargoRequestStatus
        restCargoRequestStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cargoRequestStatus))
            )
            .andExpect(status().isCreated());

        // Validate the CargoRequestStatus in the database
        List<CargoRequestStatus> cargoRequestStatusList = cargoRequestStatusRepository.findAll();
        assertThat(cargoRequestStatusList).hasSize(databaseSizeBeforeCreate + 1);
        CargoRequestStatus testCargoRequestStatus = cargoRequestStatusList.get(cargoRequestStatusList.size() - 1);
        assertThat(testCargoRequestStatus.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createCargoRequestStatusWithExistingId() throws Exception {
        // Create the CargoRequestStatus with an existing ID
        cargoRequestStatus.setId(1L);

        int databaseSizeBeforeCreate = cargoRequestStatusRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCargoRequestStatusMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cargoRequestStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequestStatus in the database
        List<CargoRequestStatus> cargoRequestStatusList = cargoRequestStatusRepository.findAll();
        assertThat(cargoRequestStatusList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCargoRequestStatuses() throws Exception {
        // Initialize the database
        cargoRequestStatusRepository.saveAndFlush(cargoRequestStatus);

        // Get all the cargoRequestStatusList
        restCargoRequestStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargoRequestStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getCargoRequestStatus() throws Exception {
        // Initialize the database
        cargoRequestStatusRepository.saveAndFlush(cargoRequestStatus);

        // Get the cargoRequestStatus
        restCargoRequestStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, cargoRequestStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cargoRequestStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingCargoRequestStatus() throws Exception {
        // Get the cargoRequestStatus
        restCargoRequestStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCargoRequestStatus() throws Exception {
        // Initialize the database
        cargoRequestStatusRepository.saveAndFlush(cargoRequestStatus);

        int databaseSizeBeforeUpdate = cargoRequestStatusRepository.findAll().size();

        // Update the cargoRequestStatus
        CargoRequestStatus updatedCargoRequestStatus = cargoRequestStatusRepository.findById(cargoRequestStatus.getId()).get();
        // Disconnect from session so that the updates on updatedCargoRequestStatus are not directly saved in db
        em.detach(updatedCargoRequestStatus);
        updatedCargoRequestStatus.name(UPDATED_NAME);

        restCargoRequestStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCargoRequestStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCargoRequestStatus))
            )
            .andExpect(status().isOk());

        // Validate the CargoRequestStatus in the database
        List<CargoRequestStatus> cargoRequestStatusList = cargoRequestStatusRepository.findAll();
        assertThat(cargoRequestStatusList).hasSize(databaseSizeBeforeUpdate);
        CargoRequestStatus testCargoRequestStatus = cargoRequestStatusList.get(cargoRequestStatusList.size() - 1);
        assertThat(testCargoRequestStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingCargoRequestStatus() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestStatusRepository.findAll().size();
        cargoRequestStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoRequestStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cargoRequestStatus.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cargoRequestStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequestStatus in the database
        List<CargoRequestStatus> cargoRequestStatusList = cargoRequestStatusRepository.findAll();
        assertThat(cargoRequestStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCargoRequestStatus() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestStatusRepository.findAll().size();
        cargoRequestStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cargoRequestStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequestStatus in the database
        List<CargoRequestStatus> cargoRequestStatusList = cargoRequestStatusRepository.findAll();
        assertThat(cargoRequestStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCargoRequestStatus() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestStatusRepository.findAll().size();
        cargoRequestStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestStatusMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cargoRequestStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CargoRequestStatus in the database
        List<CargoRequestStatus> cargoRequestStatusList = cargoRequestStatusRepository.findAll();
        assertThat(cargoRequestStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCargoRequestStatusWithPatch() throws Exception {
        // Initialize the database
        cargoRequestStatusRepository.saveAndFlush(cargoRequestStatus);

        int databaseSizeBeforeUpdate = cargoRequestStatusRepository.findAll().size();

        // Update the cargoRequestStatus using partial update
        CargoRequestStatus partialUpdatedCargoRequestStatus = new CargoRequestStatus();
        partialUpdatedCargoRequestStatus.setId(cargoRequestStatus.getId());

        restCargoRequestStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargoRequestStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCargoRequestStatus))
            )
            .andExpect(status().isOk());

        // Validate the CargoRequestStatus in the database
        List<CargoRequestStatus> cargoRequestStatusList = cargoRequestStatusRepository.findAll();
        assertThat(cargoRequestStatusList).hasSize(databaseSizeBeforeUpdate);
        CargoRequestStatus testCargoRequestStatus = cargoRequestStatusList.get(cargoRequestStatusList.size() - 1);
        assertThat(testCargoRequestStatus.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateCargoRequestStatusWithPatch() throws Exception {
        // Initialize the database
        cargoRequestStatusRepository.saveAndFlush(cargoRequestStatus);

        int databaseSizeBeforeUpdate = cargoRequestStatusRepository.findAll().size();

        // Update the cargoRequestStatus using partial update
        CargoRequestStatus partialUpdatedCargoRequestStatus = new CargoRequestStatus();
        partialUpdatedCargoRequestStatus.setId(cargoRequestStatus.getId());

        partialUpdatedCargoRequestStatus.name(UPDATED_NAME);

        restCargoRequestStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargoRequestStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCargoRequestStatus))
            )
            .andExpect(status().isOk());

        // Validate the CargoRequestStatus in the database
        List<CargoRequestStatus> cargoRequestStatusList = cargoRequestStatusRepository.findAll();
        assertThat(cargoRequestStatusList).hasSize(databaseSizeBeforeUpdate);
        CargoRequestStatus testCargoRequestStatus = cargoRequestStatusList.get(cargoRequestStatusList.size() - 1);
        assertThat(testCargoRequestStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingCargoRequestStatus() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestStatusRepository.findAll().size();
        cargoRequestStatus.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoRequestStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cargoRequestStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cargoRequestStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequestStatus in the database
        List<CargoRequestStatus> cargoRequestStatusList = cargoRequestStatusRepository.findAll();
        assertThat(cargoRequestStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCargoRequestStatus() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestStatusRepository.findAll().size();
        cargoRequestStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cargoRequestStatus))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequestStatus in the database
        List<CargoRequestStatus> cargoRequestStatusList = cargoRequestStatusRepository.findAll();
        assertThat(cargoRequestStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCargoRequestStatus() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestStatusRepository.findAll().size();
        cargoRequestStatus.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestStatusMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cargoRequestStatus))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CargoRequestStatus in the database
        List<CargoRequestStatus> cargoRequestStatusList = cargoRequestStatusRepository.findAll();
        assertThat(cargoRequestStatusList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCargoRequestStatus() throws Exception {
        // Initialize the database
        cargoRequestStatusRepository.saveAndFlush(cargoRequestStatus);

        int databaseSizeBeforeDelete = cargoRequestStatusRepository.findAll().size();

        // Delete the cargoRequestStatus
        restCargoRequestStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, cargoRequestStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CargoRequestStatus> cargoRequestStatusList = cargoRequestStatusRepository.findAll();
        assertThat(cargoRequestStatusList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
