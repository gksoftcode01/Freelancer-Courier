package com.gksoft.application.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.application.IntegrationTest;
import com.gksoft.application.domain.CargoRequestDetails;
import com.gksoft.application.repository.CargoRequestDetailsRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link CargoRequestDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CargoRequestDetailsResourceIT {

    private static final String DEFAULT_ITEM_DESC = "AAAAAAAAAA";
    private static final String UPDATED_ITEM_DESC = "BBBBBBBBBB";

    private static final Long DEFAULT_ITEM_WEIGHT = 1L;
    private static final Long UPDATED_ITEM_WEIGHT = 2L;

    private static final Long DEFAULT_ITEM_HEIGHT = 1L;
    private static final Long UPDATED_ITEM_HEIGHT = 2L;

    private static final Long DEFAULT_ITEM_WIDTH = 1L;
    private static final Long UPDATED_ITEM_WIDTH = 2L;

    private static final byte[] DEFAULT_ITEM_PHOTO = TestUtil.createByteArray(1, "0");
    private static final byte[] UPDATED_ITEM_PHOTO = TestUtil.createByteArray(1, "1");
    private static final String DEFAULT_ITEM_PHOTO_CONTENT_TYPE = "image/jpg";
    private static final String UPDATED_ITEM_PHOTO_CONTENT_TYPE = "image/png";

    private static final String ENTITY_API_URL = "/api/cargo-request-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CargoRequestDetailsRepository cargoRequestDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCargoRequestDetailsMockMvc;

    private CargoRequestDetails cargoRequestDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CargoRequestDetails createEntity(EntityManager em) {
        CargoRequestDetails cargoRequestDetails = new CargoRequestDetails()
            .itemDesc(DEFAULT_ITEM_DESC)
            .itemWeight(DEFAULT_ITEM_WEIGHT)
            .itemHeight(DEFAULT_ITEM_HEIGHT)
            .itemWidth(DEFAULT_ITEM_WIDTH)
            .itemPhoto(DEFAULT_ITEM_PHOTO)
            .itemPhotoContentType(DEFAULT_ITEM_PHOTO_CONTENT_TYPE);
        return cargoRequestDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CargoRequestDetails createUpdatedEntity(EntityManager em) {
        CargoRequestDetails cargoRequestDetails = new CargoRequestDetails()
            .itemDesc(UPDATED_ITEM_DESC)
            .itemWeight(UPDATED_ITEM_WEIGHT)
            .itemHeight(UPDATED_ITEM_HEIGHT)
            .itemWidth(UPDATED_ITEM_WIDTH)
            .itemPhoto(UPDATED_ITEM_PHOTO)
            .itemPhotoContentType(UPDATED_ITEM_PHOTO_CONTENT_TYPE);
        return cargoRequestDetails;
    }

    @BeforeEach
    public void initTest() {
        cargoRequestDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createCargoRequestDetails() throws Exception {
        int databaseSizeBeforeCreate = cargoRequestDetailsRepository.findAll().size();
        // Create the CargoRequestDetails
        restCargoRequestDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cargoRequestDetails))
            )
            .andExpect(status().isCreated());

        // Validate the CargoRequestDetails in the database
        List<CargoRequestDetails> cargoRequestDetailsList = cargoRequestDetailsRepository.findAll();
        assertThat(cargoRequestDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        CargoRequestDetails testCargoRequestDetails = cargoRequestDetailsList.get(cargoRequestDetailsList.size() - 1);
        assertThat(testCargoRequestDetails.getItemDesc()).isEqualTo(DEFAULT_ITEM_DESC);
        assertThat(testCargoRequestDetails.getItemWeight()).isEqualTo(DEFAULT_ITEM_WEIGHT);
        assertThat(testCargoRequestDetails.getItemHeight()).isEqualTo(DEFAULT_ITEM_HEIGHT);
        assertThat(testCargoRequestDetails.getItemWidth()).isEqualTo(DEFAULT_ITEM_WIDTH);
        assertThat(testCargoRequestDetails.getItemPhoto()).isEqualTo(DEFAULT_ITEM_PHOTO);
        assertThat(testCargoRequestDetails.getItemPhotoContentType()).isEqualTo(DEFAULT_ITEM_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void createCargoRequestDetailsWithExistingId() throws Exception {
        // Create the CargoRequestDetails with an existing ID
        cargoRequestDetails.setId(1L);

        int databaseSizeBeforeCreate = cargoRequestDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCargoRequestDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cargoRequestDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequestDetails in the database
        List<CargoRequestDetails> cargoRequestDetailsList = cargoRequestDetailsRepository.findAll();
        assertThat(cargoRequestDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCargoRequestDetails() throws Exception {
        // Initialize the database
        cargoRequestDetailsRepository.saveAndFlush(cargoRequestDetails);

        // Get all the cargoRequestDetailsList
        restCargoRequestDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cargoRequestDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].itemDesc").value(hasItem(DEFAULT_ITEM_DESC)))
            .andExpect(jsonPath("$.[*].itemWeight").value(hasItem(DEFAULT_ITEM_WEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].itemHeight").value(hasItem(DEFAULT_ITEM_HEIGHT.intValue())))
            .andExpect(jsonPath("$.[*].itemWidth").value(hasItem(DEFAULT_ITEM_WIDTH.intValue())))
            .andExpect(jsonPath("$.[*].itemPhotoContentType").value(hasItem(DEFAULT_ITEM_PHOTO_CONTENT_TYPE)))
            .andExpect(jsonPath("$.[*].itemPhoto").value(hasItem(Base64Utils.encodeToString(DEFAULT_ITEM_PHOTO))));
    }

    @Test
    @Transactional
    void getCargoRequestDetails() throws Exception {
        // Initialize the database
        cargoRequestDetailsRepository.saveAndFlush(cargoRequestDetails);

        // Get the cargoRequestDetails
        restCargoRequestDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, cargoRequestDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cargoRequestDetails.getId().intValue()))
            .andExpect(jsonPath("$.itemDesc").value(DEFAULT_ITEM_DESC))
            .andExpect(jsonPath("$.itemWeight").value(DEFAULT_ITEM_WEIGHT.intValue()))
            .andExpect(jsonPath("$.itemHeight").value(DEFAULT_ITEM_HEIGHT.intValue()))
            .andExpect(jsonPath("$.itemWidth").value(DEFAULT_ITEM_WIDTH.intValue()))
            .andExpect(jsonPath("$.itemPhotoContentType").value(DEFAULT_ITEM_PHOTO_CONTENT_TYPE))
            .andExpect(jsonPath("$.itemPhoto").value(Base64Utils.encodeToString(DEFAULT_ITEM_PHOTO)));
    }

    @Test
    @Transactional
    void getNonExistingCargoRequestDetails() throws Exception {
        // Get the cargoRequestDetails
        restCargoRequestDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCargoRequestDetails() throws Exception {
        // Initialize the database
        cargoRequestDetailsRepository.saveAndFlush(cargoRequestDetails);

        int databaseSizeBeforeUpdate = cargoRequestDetailsRepository.findAll().size();

        // Update the cargoRequestDetails
        CargoRequestDetails updatedCargoRequestDetails = cargoRequestDetailsRepository.findById(cargoRequestDetails.getId()).get();
        // Disconnect from session so that the updates on updatedCargoRequestDetails are not directly saved in db
        em.detach(updatedCargoRequestDetails);
        updatedCargoRequestDetails
            .itemDesc(UPDATED_ITEM_DESC)
            .itemWeight(UPDATED_ITEM_WEIGHT)
            .itemHeight(UPDATED_ITEM_HEIGHT)
            .itemWidth(UPDATED_ITEM_WIDTH)
            .itemPhoto(UPDATED_ITEM_PHOTO)
            .itemPhotoContentType(UPDATED_ITEM_PHOTO_CONTENT_TYPE);

        restCargoRequestDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCargoRequestDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCargoRequestDetails))
            )
            .andExpect(status().isOk());

        // Validate the CargoRequestDetails in the database
        List<CargoRequestDetails> cargoRequestDetailsList = cargoRequestDetailsRepository.findAll();
        assertThat(cargoRequestDetailsList).hasSize(databaseSizeBeforeUpdate);
        CargoRequestDetails testCargoRequestDetails = cargoRequestDetailsList.get(cargoRequestDetailsList.size() - 1);
        assertThat(testCargoRequestDetails.getItemDesc()).isEqualTo(UPDATED_ITEM_DESC);
        assertThat(testCargoRequestDetails.getItemWeight()).isEqualTo(UPDATED_ITEM_WEIGHT);
        assertThat(testCargoRequestDetails.getItemHeight()).isEqualTo(UPDATED_ITEM_HEIGHT);
        assertThat(testCargoRequestDetails.getItemWidth()).isEqualTo(UPDATED_ITEM_WIDTH);
        assertThat(testCargoRequestDetails.getItemPhoto()).isEqualTo(UPDATED_ITEM_PHOTO);
        assertThat(testCargoRequestDetails.getItemPhotoContentType()).isEqualTo(UPDATED_ITEM_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingCargoRequestDetails() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestDetailsRepository.findAll().size();
        cargoRequestDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoRequestDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cargoRequestDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cargoRequestDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequestDetails in the database
        List<CargoRequestDetails> cargoRequestDetailsList = cargoRequestDetailsRepository.findAll();
        assertThat(cargoRequestDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCargoRequestDetails() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestDetailsRepository.findAll().size();
        cargoRequestDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cargoRequestDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequestDetails in the database
        List<CargoRequestDetails> cargoRequestDetailsList = cargoRequestDetailsRepository.findAll();
        assertThat(cargoRequestDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCargoRequestDetails() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestDetailsRepository.findAll().size();
        cargoRequestDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cargoRequestDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CargoRequestDetails in the database
        List<CargoRequestDetails> cargoRequestDetailsList = cargoRequestDetailsRepository.findAll();
        assertThat(cargoRequestDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCargoRequestDetailsWithPatch() throws Exception {
        // Initialize the database
        cargoRequestDetailsRepository.saveAndFlush(cargoRequestDetails);

        int databaseSizeBeforeUpdate = cargoRequestDetailsRepository.findAll().size();

        // Update the cargoRequestDetails using partial update
        CargoRequestDetails partialUpdatedCargoRequestDetails = new CargoRequestDetails();
        partialUpdatedCargoRequestDetails.setId(cargoRequestDetails.getId());

        partialUpdatedCargoRequestDetails
            .itemDesc(UPDATED_ITEM_DESC)
            .itemWeight(UPDATED_ITEM_WEIGHT)
            .itemWidth(UPDATED_ITEM_WIDTH)
            .itemPhoto(UPDATED_ITEM_PHOTO)
            .itemPhotoContentType(UPDATED_ITEM_PHOTO_CONTENT_TYPE);

        restCargoRequestDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargoRequestDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCargoRequestDetails))
            )
            .andExpect(status().isOk());

        // Validate the CargoRequestDetails in the database
        List<CargoRequestDetails> cargoRequestDetailsList = cargoRequestDetailsRepository.findAll();
        assertThat(cargoRequestDetailsList).hasSize(databaseSizeBeforeUpdate);
        CargoRequestDetails testCargoRequestDetails = cargoRequestDetailsList.get(cargoRequestDetailsList.size() - 1);
        assertThat(testCargoRequestDetails.getItemDesc()).isEqualTo(UPDATED_ITEM_DESC);
        assertThat(testCargoRequestDetails.getItemWeight()).isEqualTo(UPDATED_ITEM_WEIGHT);
        assertThat(testCargoRequestDetails.getItemHeight()).isEqualTo(DEFAULT_ITEM_HEIGHT);
        assertThat(testCargoRequestDetails.getItemWidth()).isEqualTo(UPDATED_ITEM_WIDTH);
        assertThat(testCargoRequestDetails.getItemPhoto()).isEqualTo(UPDATED_ITEM_PHOTO);
        assertThat(testCargoRequestDetails.getItemPhotoContentType()).isEqualTo(UPDATED_ITEM_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateCargoRequestDetailsWithPatch() throws Exception {
        // Initialize the database
        cargoRequestDetailsRepository.saveAndFlush(cargoRequestDetails);

        int databaseSizeBeforeUpdate = cargoRequestDetailsRepository.findAll().size();

        // Update the cargoRequestDetails using partial update
        CargoRequestDetails partialUpdatedCargoRequestDetails = new CargoRequestDetails();
        partialUpdatedCargoRequestDetails.setId(cargoRequestDetails.getId());

        partialUpdatedCargoRequestDetails
            .itemDesc(UPDATED_ITEM_DESC)
            .itemWeight(UPDATED_ITEM_WEIGHT)
            .itemHeight(UPDATED_ITEM_HEIGHT)
            .itemWidth(UPDATED_ITEM_WIDTH)
            .itemPhoto(UPDATED_ITEM_PHOTO)
            .itemPhotoContentType(UPDATED_ITEM_PHOTO_CONTENT_TYPE);

        restCargoRequestDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCargoRequestDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCargoRequestDetails))
            )
            .andExpect(status().isOk());

        // Validate the CargoRequestDetails in the database
        List<CargoRequestDetails> cargoRequestDetailsList = cargoRequestDetailsRepository.findAll();
        assertThat(cargoRequestDetailsList).hasSize(databaseSizeBeforeUpdate);
        CargoRequestDetails testCargoRequestDetails = cargoRequestDetailsList.get(cargoRequestDetailsList.size() - 1);
        assertThat(testCargoRequestDetails.getItemDesc()).isEqualTo(UPDATED_ITEM_DESC);
        assertThat(testCargoRequestDetails.getItemWeight()).isEqualTo(UPDATED_ITEM_WEIGHT);
        assertThat(testCargoRequestDetails.getItemHeight()).isEqualTo(UPDATED_ITEM_HEIGHT);
        assertThat(testCargoRequestDetails.getItemWidth()).isEqualTo(UPDATED_ITEM_WIDTH);
        assertThat(testCargoRequestDetails.getItemPhoto()).isEqualTo(UPDATED_ITEM_PHOTO);
        assertThat(testCargoRequestDetails.getItemPhotoContentType()).isEqualTo(UPDATED_ITEM_PHOTO_CONTENT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingCargoRequestDetails() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestDetailsRepository.findAll().size();
        cargoRequestDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCargoRequestDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cargoRequestDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cargoRequestDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequestDetails in the database
        List<CargoRequestDetails> cargoRequestDetailsList = cargoRequestDetailsRepository.findAll();
        assertThat(cargoRequestDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCargoRequestDetails() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestDetailsRepository.findAll().size();
        cargoRequestDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cargoRequestDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the CargoRequestDetails in the database
        List<CargoRequestDetails> cargoRequestDetailsList = cargoRequestDetailsRepository.findAll();
        assertThat(cargoRequestDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCargoRequestDetails() throws Exception {
        int databaseSizeBeforeUpdate = cargoRequestDetailsRepository.findAll().size();
        cargoRequestDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCargoRequestDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cargoRequestDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CargoRequestDetails in the database
        List<CargoRequestDetails> cargoRequestDetailsList = cargoRequestDetailsRepository.findAll();
        assertThat(cargoRequestDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCargoRequestDetails() throws Exception {
        // Initialize the database
        cargoRequestDetailsRepository.saveAndFlush(cargoRequestDetails);

        int databaseSizeBeforeDelete = cargoRequestDetailsRepository.findAll().size();

        // Delete the cargoRequestDetails
        restCargoRequestDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, cargoRequestDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CargoRequestDetails> cargoRequestDetailsList = cargoRequestDetailsRepository.findAll();
        assertThat(cargoRequestDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
