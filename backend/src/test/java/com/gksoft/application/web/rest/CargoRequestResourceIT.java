package com.gksoft.application.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.application.IntegrationTest;
import com.gksoft.application.domain.CargoRequest;
import com.gksoft.application.repository.CargoRequestRepository;
import com.gksoft.application.service.CargoRequestService;
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
 * Integration tests for the {@link CargoRequestResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class CargoRequestResourceIT {

    private static final Long DEFAULT_BUDGET = 1L;
    private static final Long UPDATED_BUDGET = 2L;

    private static final Boolean DEFAULT_IS_TO_DOOR = false;
    private static final Boolean UPDATED_IS_TO_DOOR = true;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_AGREED_PRICE = 1L;
    private static final Long UPDATED_AGREED_PRICE = 2L;

    private static final String ENTITY_API_URL = "/api/cargo-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CargoRequestRepository cargoRequestRepository;

    @Mock
    private CargoRequestRepository cargoRequestRepositoryMock;

    @Mock
    private CargoRequestService cargoRequestServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCargoRequestMockMvc;

    private CargoRequest cargoRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CargoRequest createEntity(EntityManager em) {
        CargoRequest cargoRequest = new CargoRequest()
            .budget(DEFAULT_BUDGET)
            .isToDoor(DEFAULT_IS_TO_DOOR)
            .createDate(DEFAULT_CREATE_DATE)
            .agreedPrice(DEFAULT_AGREED_PRICE);
        return cargoRequest;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CargoRequest createUpdatedEntity(EntityManager em) {
        CargoRequest cargoRequest = new CargoRequest()
            .budget(UPDATED_BUDGET)
            .isToDoor(UPDATED_IS_TO_DOOR)
            .createDate(UPDATED_CREATE_DATE)
            .agreedPrice(UPDATED_AGREED_PRICE);
        return cargoRequest;
    }

    @BeforeEach
    public void initTest() {
        cargoRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createCargoRequest() throws Exception {
        int databaseSizeBeforeCreate = cargoRequestRepository.findAll().size();
        // Create the CargoRequest
        restCargoRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cargoRequest)))
            .andExpect(status().isCreated());

        // Validate the CargoRequest in the database
        List<CargoRequest> cargoRequestList = cargoRequestRepository.findAll();
        assertThat(cargoRequestList).hasSize(databaseSizeBeforeCreate + 1);
        CargoRequest testCargoRequest = cargoRequestList.get(cargoRequestList.size() - 1);
        assertThat(testCargoRequest.getBudget()).isEqualTo(DEFAULT_BUDGET);
        assertThat(testCargoRequest.getIsToDoor()).isEqualTo(DEFAULT_IS_TO_DOOR);
        assertThat(testCargoRequest.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testCargoRequest.getAgreedPrice()).isEqualTo(DEFAULT_AGREED_PRICE);
    }

    @Test
    @Transactional
    void createCargoRequestWithExistingId() throws Exception {
        // Create the CargoRequest with an existing ID
        cargoRequest.setId(1L);

        int databaseSizeBeforeCreate = cargoRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCargoRequestMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cargoRequest)))
            .andExpect(status().isBadRequest());

        // Validate the CargoRequest in the database
        List<CargoRequest> cargoRequestList = cargoRequestRepository.findAll();
        assertThat(cargoRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCargoRequests() throws Exception {
        // Initialize the database
        cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get all the cargoRequestList
        restCargoRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargoRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].budget").value(hasItem(DEFAULT_BUDGET.intValue())))
            .andExpect(jsonPath("$.[*].isToDoor").value(hasItem(DEFAULT_IS_TO_DOOR.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].agreedPrice").value(hasItem(DEFAULT_AGREED_PRICE.intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCargoRequestsWithEagerRelationshipsIsEnabled() throws Exception {
        when(cargoRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCargoRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(cargoRequestServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllCargoRequestsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(cargoRequestServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restCargoRequestMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(cargoRequestRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getCargoRequest() throws Exception {
        // Initialize the database
        cargoRequestRepository.saveAndFlush(cargoRequest);

        // Get the cargoRequest
        restCargoRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, cargoRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cargoRequest.getId().intValue()))
            .andExpect(jsonPath("$.budget").value(DEFAULT_BUDGET.intValue()))
            .andExpect(jsonPath("$.isToDoor").value(DEFAULT_IS_TO_DOOR.booleanValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.agreedPrice").value(DEFAULT_AGREED_PRICE.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingCargoRequest() throws Exception {
        // Get the cargoRequest
        restCargoRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCargoRequest() throws Exception {
        // Initialize the database
        cargoRequestRepository.saveAndFlush(cargoRequest);

        int databaseSizeBeforeUpdate = cargoRequestRepository.findAll().size();

        // Update the cargoRequest
        CargoRequest updatedCargoRequest = cargoRequestRepository.findById(cargoRequest.getId()).get();
        // Disconnect from session so that the updates on updatedCargoRequest are not directly saved in db
        em.detach(updatedCargoRequest);
        updatedCargoRequest
            .budget(UPDATED_BUDGET)
            .isToDoor(UPDATED_IS_TO_DOOR)
            .createDate(UPDATED_CREATE_DATE)
            .agreedPrice(UPDATED_AGREED_PRICE);

        restCargoRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCargoRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCargoRequest))
            )
            .andExpect(status().isOk());

        // Validate the CargoRequest in the database
        List<CargoRequest> cargoRequestList = cargoRequestRepository.findAll();
        assertThat(cargoRequestList).hasSize(databaseSizeBeforeUpdate);
        CargoRequest testCargoRequest = cargoRequestList.get(cargoRequestList.size() - 1);
        assertThat(testCargoRequest.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testCargoRequest.getIsToDoor()).isEqualTo(UPDATED_IS_TO_DOOR);
        assertThat(testCargoRequest.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCargoRequest.getAgreedPrice()).isEqualTo(UPDATED_AGREED_PRICE);
    }

    @Test
    @Transactional
    void putNonExistingCargoRequest() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestRepository.findAll().size();
        cargoRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cargoRequest.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cargoRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequest in the database
        List<CargoRequest> cargoRequestList = cargoRequestRepository.findAll();
        assertThat(cargoRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCargoRequest() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestRepository.findAll().size();
        cargoRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cargoRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequest in the database
        List<CargoRequest> cargoRequestList = cargoRequestRepository.findAll();
        assertThat(cargoRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCargoRequest() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestRepository.findAll().size();
        cargoRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cargoRequest)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CargoRequest in the database
        List<CargoRequest> cargoRequestList = cargoRequestRepository.findAll();
        assertThat(cargoRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCargoRequestWithPatch() throws Exception {
        // Initialize the database
        cargoRequestRepository.saveAndFlush(cargoRequest);

        int databaseSizeBeforeUpdate = cargoRequestRepository.findAll().size();

        // Update the cargoRequest using partial update
        CargoRequest partialUpdatedCargoRequest = new CargoRequest();
        partialUpdatedCargoRequest.setId(cargoRequest.getId());

        partialUpdatedCargoRequest.isToDoor(UPDATED_IS_TO_DOOR).createDate(UPDATED_CREATE_DATE).agreedPrice(UPDATED_AGREED_PRICE);

        restCargoRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargoRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCargoRequest))
            )
            .andExpect(status().isOk());

        // Validate the CargoRequest in the database
        List<CargoRequest> cargoRequestList = cargoRequestRepository.findAll();
        assertThat(cargoRequestList).hasSize(databaseSizeBeforeUpdate);
        CargoRequest testCargoRequest = cargoRequestList.get(cargoRequestList.size() - 1);
        assertThat(testCargoRequest.getBudget()).isEqualTo(DEFAULT_BUDGET);
        assertThat(testCargoRequest.getIsToDoor()).isEqualTo(UPDATED_IS_TO_DOOR);
        assertThat(testCargoRequest.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCargoRequest.getAgreedPrice()).isEqualTo(UPDATED_AGREED_PRICE);
    }

    @Test
    @Transactional
    void fullUpdateCargoRequestWithPatch() throws Exception {
        // Initialize the database
        cargoRequestRepository.saveAndFlush(cargoRequest);

        int databaseSizeBeforeUpdate = cargoRequestRepository.findAll().size();

        // Update the cargoRequest using partial update
        CargoRequest partialUpdatedCargoRequest = new CargoRequest();
        partialUpdatedCargoRequest.setId(cargoRequest.getId());

        partialUpdatedCargoRequest
            .budget(UPDATED_BUDGET)
            .isToDoor(UPDATED_IS_TO_DOOR)
            .createDate(UPDATED_CREATE_DATE)
            .agreedPrice(UPDATED_AGREED_PRICE);

        restCargoRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargoRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCargoRequest))
            )
            .andExpect(status().isOk());

        // Validate the CargoRequest in the database
        List<CargoRequest> cargoRequestList = cargoRequestRepository.findAll();
        assertThat(cargoRequestList).hasSize(databaseSizeBeforeUpdate);
        CargoRequest testCargoRequest = cargoRequestList.get(cargoRequestList.size() - 1);
        assertThat(testCargoRequest.getBudget()).isEqualTo(UPDATED_BUDGET);
        assertThat(testCargoRequest.getIsToDoor()).isEqualTo(UPDATED_IS_TO_DOOR);
        assertThat(testCargoRequest.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCargoRequest.getAgreedPrice()).isEqualTo(UPDATED_AGREED_PRICE);
    }

    @Test
    @Transactional
    void patchNonExistingCargoRequest() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestRepository.findAll().size();
        cargoRequest.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cargoRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cargoRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequest in the database
        List<CargoRequest> cargoRequestList = cargoRequestRepository.findAll();
        assertThat(cargoRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCargoRequest() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestRepository.findAll().size();
        cargoRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cargoRequest))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequest in the database
        List<CargoRequest> cargoRequestList = cargoRequestRepository.findAll();
        assertThat(cargoRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCargoRequest() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestRepository.findAll().size();
        cargoRequest.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cargoRequest))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CargoRequest in the database
        List<CargoRequest> cargoRequestList = cargoRequestRepository.findAll();
        assertThat(cargoRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCargoRequest() throws Exception {
        // Initialize the database
        cargoRequestRepository.saveAndFlush(cargoRequest);

        int databaseSizeBeforeDelete = cargoRequestRepository.findAll().size();

        // Delete the cargoRequest
        restCargoRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, cargoRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CargoRequest> cargoRequestList = cargoRequestRepository.findAll();
        assertThat(cargoRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
