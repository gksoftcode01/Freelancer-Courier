package com.gksoft.application.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.application.IntegrationTest;
import com.gksoft.application.domain.StateProvince;
import com.gksoft.application.repository.StateProvinceRepository;
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
 * Integration tests for the {@link StateProvinceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StateProvinceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/state-provinces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StateProvinceRepository stateProvinceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStateProvinceMockMvc;

    private StateProvince stateProvince;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StateProvince createEntity(EntityManager em) {
        StateProvince stateProvince = new StateProvince().name(DEFAULT_NAME);
        return stateProvince;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StateProvince createUpdatedEntity(EntityManager em) {
        StateProvince stateProvince = new StateProvince().name(UPDATED_NAME);
        return stateProvince;
    }

    @BeforeEach
    public void initTest() {
        stateProvince = createEntity(em);
    }

    @Test
    @Transactional
    void createStateProvince() throws Exception {
        int databaseSizeBeforeCreate = stateProvinceRepository.findAll().size();
        // Create the StateProvince
        restStateProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stateProvince)))
            .andExpect(status().isCreated());

        // Validate the StateProvince in the database
        List<StateProvince> stateProvinceList = stateProvinceRepository.findAll();
        assertThat(stateProvinceList).hasSize(databaseSizeBeforeCreate + 1);
        StateProvince testStateProvince = stateProvinceList.get(stateProvinceList.size() - 1);
        assertThat(testStateProvince.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createStateProvinceWithExistingId() throws Exception {
        // Create the StateProvince with an existing ID
        stateProvince.setId(1L);

        int databaseSizeBeforeCreate = stateProvinceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStateProvinceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stateProvince)))
            .andExpect(status().isBadRequest());

        // Validate the StateProvince in the database
        List<StateProvince> stateProvinceList = stateProvinceRepository.findAll();
        assertThat(stateProvinceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStateProvinces() throws Exception {
        // Initialize the database
        stateProvinceRepository.saveAndFlush(stateProvince);

        // Get all the stateProvinceList
        restStateProvinceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stateProvince.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getStateProvince() throws Exception {
        // Initialize the database
        stateProvinceRepository.saveAndFlush(stateProvince);

        // Get the stateProvince
        restStateProvinceMockMvc
            .perform(get(ENTITY_API_URL_ID, stateProvince.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stateProvince.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingStateProvince() throws Exception {
        // Get the stateProvince
        restStateProvinceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingStateProvince() throws Exception {
        // Initialize the database
        stateProvinceRepository.saveAndFlush(stateProvince);

        int databaseSizeBeforeUpdate = stateProvinceRepository.findAll().size();

        // Update the stateProvince
        StateProvince updatedStateProvince = stateProvinceRepository.findById(stateProvince.getId()).get();
        // Disconnect from session so that the updates on updatedStateProvince are not directly saved in db
        em.detach(updatedStateProvince);
        updatedStateProvince.name(UPDATED_NAME);

        restStateProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStateProvince.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStateProvince))
            )
            .andExpect(status().isOk());

        // Validate the StateProvince in the database
        List<StateProvince> stateProvinceList = stateProvinceRepository.findAll();
        assertThat(stateProvinceList).hasSize(databaseSizeBeforeUpdate);
        StateProvince testStateProvince = stateProvinceList.get(stateProvinceList.size() - 1);
        assertThat(testStateProvince.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingStateProvince() throws Exception {
        int databaseSizeBeforeUpdate = stateProvinceRepository.findAll().size();
        stateProvince.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stateProvince.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stateProvince))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateProvince in the database
        List<StateProvince> stateProvinceList = stateProvinceRepository.findAll();
        assertThat(stateProvinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStateProvince() throws Exception {
        int databaseSizeBeforeUpdate = stateProvinceRepository.findAll().size();
        stateProvince.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateProvinceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stateProvince))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateProvince in the database
        List<StateProvince> stateProvinceList = stateProvinceRepository.findAll();
        assertThat(stateProvinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStateProvince() throws Exception {
        int databaseSizeBeforeUpdate = stateProvinceRepository.findAll().size();
        stateProvince.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateProvinceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stateProvince)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StateProvince in the database
        List<StateProvince> stateProvinceList = stateProvinceRepository.findAll();
        assertThat(stateProvinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStateProvinceWithPatch() throws Exception {
        // Initialize the database
        stateProvinceRepository.saveAndFlush(stateProvince);

        int databaseSizeBeforeUpdate = stateProvinceRepository.findAll().size();

        // Update the stateProvince using partial update
        StateProvince partialUpdatedStateProvince = new StateProvince();
        partialUpdatedStateProvince.setId(stateProvince.getId());

        restStateProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStateProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStateProvince))
            )
            .andExpect(status().isOk());

        // Validate the StateProvince in the database
        List<StateProvince> stateProvinceList = stateProvinceRepository.findAll();
        assertThat(stateProvinceList).hasSize(databaseSizeBeforeUpdate);
        StateProvince testStateProvince = stateProvinceList.get(stateProvinceList.size() - 1);
        assertThat(testStateProvince.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateStateProvinceWithPatch() throws Exception {
        // Initialize the database
        stateProvinceRepository.saveAndFlush(stateProvince);

        int databaseSizeBeforeUpdate = stateProvinceRepository.findAll().size();

        // Update the stateProvince using partial update
        StateProvince partialUpdatedStateProvince = new StateProvince();
        partialUpdatedStateProvince.setId(stateProvince.getId());

        partialUpdatedStateProvince.name(UPDATED_NAME);

        restStateProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStateProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStateProvince))
            )
            .andExpect(status().isOk());

        // Validate the StateProvince in the database
        List<StateProvince> stateProvinceList = stateProvinceRepository.findAll();
        assertThat(stateProvinceList).hasSize(databaseSizeBeforeUpdate);
        StateProvince testStateProvince = stateProvinceList.get(stateProvinceList.size() - 1);
        assertThat(testStateProvince.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingStateProvince() throws Exception {
        int databaseSizeBeforeUpdate = stateProvinceRepository.findAll().size();
        stateProvince.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStateProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stateProvince.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stateProvince))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateProvince in the database
        List<StateProvince> stateProvinceList = stateProvinceRepository.findAll();
        assertThat(stateProvinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStateProvince() throws Exception {
        int databaseSizeBeforeUpdate = stateProvinceRepository.findAll().size();
        stateProvince.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stateProvince))
            )
            .andExpect(status().isBadRequest());

        // Validate the StateProvince in the database
        List<StateProvince> stateProvinceList = stateProvinceRepository.findAll();
        assertThat(stateProvinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStateProvince() throws Exception {
        int databaseSizeBeforeUpdate = stateProvinceRepository.findAll().size();
        stateProvince.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStateProvinceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stateProvince))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StateProvince in the database
        List<StateProvince> stateProvinceList = stateProvinceRepository.findAll();
        assertThat(stateProvinceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStateProvince() throws Exception {
        // Initialize the database
        stateProvinceRepository.saveAndFlush(stateProvince);

        int databaseSizeBeforeDelete = stateProvinceRepository.findAll().size();

        // Delete the stateProvince
        restStateProvinceMockMvc
            .perform(delete(ENTITY_API_URL_ID, stateProvince.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StateProvince> stateProvinceList = stateProvinceRepository.findAll();
        assertThat(stateProvinceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
