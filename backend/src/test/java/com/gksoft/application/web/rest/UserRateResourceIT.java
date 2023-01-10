package com.gksoft.application.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.application.IntegrationTest;
import com.gksoft.application.domain.UserRate;
import com.gksoft.application.repository.UserRateRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link UserRateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserRateResourceIT {

    private static final Long DEFAULT_RATE = 1L;
    private static final Long UPDATED_RATE = 2L;

    private static final String DEFAULT_NOTE = "AAAAAAAAAA";
    private static final String UPDATED_NOTE = "BBBBBBBBBB";

    private static final Instant DEFAULT_RATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/user-rates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private UserRateRepository userRateRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserRateMockMvc;

    private UserRate userRate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRate createEntity(EntityManager em) {
        UserRate userRate = new UserRate().rate(DEFAULT_RATE).note(DEFAULT_NOTE).rateDate(DEFAULT_RATE_DATE);
        return userRate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserRate createUpdatedEntity(EntityManager em) {
        UserRate userRate = new UserRate().rate(UPDATED_RATE).note(UPDATED_NOTE).rateDate(UPDATED_RATE_DATE);
        return userRate;
    }

    @BeforeEach
    public void initTest() {
        userRate = createEntity(em);
    }

    @Test
    @Transactional
    void createUserRate() throws Exception {
        int databaseSizeBeforeCreate = userRateRepository.findAll().size();
        // Create the UserRate
        restUserRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userRate)))
            .andExpect(status().isCreated());

        // Validate the UserRate in the database
        List<UserRate> userRateList = userRateRepository.findAll();
        assertThat(userRateList).hasSize(databaseSizeBeforeCreate + 1);
        UserRate testUserRate = userRateList.get(userRateList.size() - 1);
        assertThat(testUserRate.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testUserRate.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testUserRate.getRateDate()).isEqualTo(DEFAULT_RATE_DATE);
    }

    @Test
    @Transactional
    void createUserRateWithExistingId() throws Exception {
        // Create the UserRate with an existing ID
        userRate.setId(1L);

        int databaseSizeBeforeCreate = userRateRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserRateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userRate)))
            .andExpect(status().isBadRequest());

        // Validate the UserRate in the database
        List<UserRate> userRateList = userRateRepository.findAll();
        assertThat(userRateList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserRates() throws Exception {
        // Initialize the database
        userRateRepository.saveAndFlush(userRate);

        // Get all the userRateList
        restUserRateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userRate.getId().intValue())))
            .andExpect(jsonPath("$.[*].rate").value(hasItem(DEFAULT_RATE.intValue())))
            .andExpect(jsonPath("$.[*].note").value(hasItem(DEFAULT_NOTE)))
            .andExpect(jsonPath("$.[*].rateDate").value(hasItem(DEFAULT_RATE_DATE.toString())));
    }

    @Test
    @Transactional
    void getUserRate() throws Exception {
        // Initialize the database
        userRateRepository.saveAndFlush(userRate);

        // Get the userRate
        restUserRateMockMvc
            .perform(get(ENTITY_API_URL_ID, userRate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userRate.getId().intValue()))
            .andExpect(jsonPath("$.rate").value(DEFAULT_RATE.intValue()))
            .andExpect(jsonPath("$.note").value(DEFAULT_NOTE))
            .andExpect(jsonPath("$.rateDate").value(DEFAULT_RATE_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingUserRate() throws Exception {
        // Get the userRate
        restUserRateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserRate() throws Exception {
        // Initialize the database
        userRateRepository.saveAndFlush(userRate);

        int databaseSizeBeforeUpdate = userRateRepository.findAll().size();

        // Update the userRate
        UserRate updatedUserRate = userRateRepository.findById(userRate.getId()).get();
        // Disconnect from session so that the updates on updatedUserRate are not directly saved in db
        em.detach(updatedUserRate);
        updatedUserRate.rate(UPDATED_RATE).note(UPDATED_NOTE).rateDate(UPDATED_RATE_DATE);

        restUserRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserRate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedUserRate))
            )
            .andExpect(status().isOk());

        // Validate the UserRate in the database
        List<UserRate> userRateList = userRateRepository.findAll();
        assertThat(userRateList).hasSize(databaseSizeBeforeUpdate);
        UserRate testUserRate = userRateList.get(userRateList.size() - 1);
        assertThat(testUserRate.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testUserRate.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testUserRate.getRateDate()).isEqualTo(UPDATED_RATE_DATE);
    }

    @Test
    @Transactional
    void putNonExistingUserRate() throws Exception {
        int databaseSizeBeforeUpdate = userRateRepository.findAll().size();
        userRate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userRate.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRate))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRate in the database
        List<UserRate> userRateList = userRateRepository.findAll();
        assertThat(userRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserRate() throws Exception {
        int databaseSizeBeforeUpdate = userRateRepository.findAll().size();
        userRate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(userRate))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRate in the database
        List<UserRate> userRateList = userRateRepository.findAll();
        assertThat(userRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserRate() throws Exception {
        int databaseSizeBeforeUpdate = userRateRepository.findAll().size();
        userRate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(userRate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRate in the database
        List<UserRate> userRateList = userRateRepository.findAll();
        assertThat(userRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserRateWithPatch() throws Exception {
        // Initialize the database
        userRateRepository.saveAndFlush(userRate);

        int databaseSizeBeforeUpdate = userRateRepository.findAll().size();

        // Update the userRate using partial update
        UserRate partialUpdatedUserRate = new UserRate();
        partialUpdatedUserRate.setId(userRate.getId());

        restUserRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserRate))
            )
            .andExpect(status().isOk());

        // Validate the UserRate in the database
        List<UserRate> userRateList = userRateRepository.findAll();
        assertThat(userRateList).hasSize(databaseSizeBeforeUpdate);
        UserRate testUserRate = userRateList.get(userRateList.size() - 1);
        assertThat(testUserRate.getRate()).isEqualTo(DEFAULT_RATE);
        assertThat(testUserRate.getNote()).isEqualTo(DEFAULT_NOTE);
        assertThat(testUserRate.getRateDate()).isEqualTo(DEFAULT_RATE_DATE);
    }

    @Test
    @Transactional
    void fullUpdateUserRateWithPatch() throws Exception {
        // Initialize the database
        userRateRepository.saveAndFlush(userRate);

        int databaseSizeBeforeUpdate = userRateRepository.findAll().size();

        // Update the userRate using partial update
        UserRate partialUpdatedUserRate = new UserRate();
        partialUpdatedUserRate.setId(userRate.getId());

        partialUpdatedUserRate.rate(UPDATED_RATE).note(UPDATED_NOTE).rateDate(UPDATED_RATE_DATE);

        restUserRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedUserRate))
            )
            .andExpect(status().isOk());

        // Validate the UserRate in the database
        List<UserRate> userRateList = userRateRepository.findAll();
        assertThat(userRateList).hasSize(databaseSizeBeforeUpdate);
        UserRate testUserRate = userRateList.get(userRateList.size() - 1);
        assertThat(testUserRate.getRate()).isEqualTo(UPDATED_RATE);
        assertThat(testUserRate.getNote()).isEqualTo(UPDATED_NOTE);
        assertThat(testUserRate.getRateDate()).isEqualTo(UPDATED_RATE_DATE);
    }

    @Test
    @Transactional
    void patchNonExistingUserRate() throws Exception {
        int databaseSizeBeforeUpdate = userRateRepository.findAll().size();
        userRate.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userRate.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userRate))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRate in the database
        List<UserRate> userRateList = userRateRepository.findAll();
        assertThat(userRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserRate() throws Exception {
        int databaseSizeBeforeUpdate = userRateRepository.findAll().size();
        userRate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(userRate))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserRate in the database
        List<UserRate> userRateList = userRateRepository.findAll();
        assertThat(userRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserRate() throws Exception {
        int databaseSizeBeforeUpdate = userRateRepository.findAll().size();
        userRate.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserRateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(userRate)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserRate in the database
        List<UserRate> userRateList = userRateRepository.findAll();
        assertThat(userRateList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserRate() throws Exception {
        // Initialize the database
        userRateRepository.saveAndFlush(userRate);

        int databaseSizeBeforeDelete = userRateRepository.findAll().size();

        // Delete the userRate
        restUserRateMockMvc
            .perform(delete(ENTITY_API_URL_ID, userRate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<UserRate> userRateList = userRateRepository.findAll();
        assertThat(userRateList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
