package com.gksoft.application.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.application.IntegrationTest;
import com.gksoft.application.domain.Ask;
import com.gksoft.application.domain.enumeration.BidAskStatus;
import com.gksoft.application.repository.AskRepository;
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
 * Integration tests for the {@link AskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AskResourceIT {

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Long DEFAULT_PRICE = 1L;
    private static final Long UPDATED_PRICE = 2L;

    private static final BidAskStatus DEFAULT_STATUS = BidAskStatus.Approve;
    private static final BidAskStatus UPDATED_STATUS = BidAskStatus.Reject;

    private static final String ENTITY_API_URL = "/api/asks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AskRepository askRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAskMockMvc;

    private Ask ask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ask createEntity(EntityManager em) {
        Ask ask = new Ask().notes(DEFAULT_NOTES).price(DEFAULT_PRICE).status(DEFAULT_STATUS);
        return ask;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ask createUpdatedEntity(EntityManager em) {
        Ask ask = new Ask().notes(UPDATED_NOTES).price(UPDATED_PRICE).status(UPDATED_STATUS);
        return ask;
    }

    @BeforeEach
    public void initTest() {
        ask = createEntity(em);
    }

    @Test
    @Transactional
    void createAsk() throws Exception {
        int databaseSizeBeforeCreate = askRepository.findAll().size();
        // Create the Ask
        restAskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ask)))
            .andExpect(status().isCreated());

        // Validate the Ask in the database
        List<Ask> askList = askRepository.findAll();
        assertThat(askList).hasSize(databaseSizeBeforeCreate + 1);
        Ask testAsk = askList.get(askList.size() - 1);
        assertThat(testAsk.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testAsk.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testAsk.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createAskWithExistingId() throws Exception {
        // Create the Ask with an existing ID
        ask.setId(1L);

        int databaseSizeBeforeCreate = askRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ask)))
            .andExpect(status().isBadRequest());

        // Validate the Ask in the database
        List<Ask> askList = askRepository.findAll();
        assertThat(askList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAsks() throws Exception {
        // Initialize the database
        askRepository.saveAndFlush(ask);

        // Get all the askList
        restAskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ask.getId().intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getAsk() throws Exception {
        // Initialize the database
        askRepository.saveAndFlush(ask);

        // Get the ask
        restAskMockMvc
            .perform(get(ENTITY_API_URL_ID, ask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ask.getId().intValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAsk() throws Exception {
        // Get the ask
        restAskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAsk() throws Exception {
        // Initialize the database
        askRepository.saveAndFlush(ask);

        int databaseSizeBeforeUpdate = askRepository.findAll().size();

        // Update the ask
        Ask updatedAsk = askRepository.findById(ask.getId()).get();
        // Disconnect from session so that the updates on updatedAsk are not directly saved in db
        em.detach(updatedAsk);
        updatedAsk.notes(UPDATED_NOTES).price(UPDATED_PRICE).status(UPDATED_STATUS);

        restAskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAsk.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAsk))
            )
            .andExpect(status().isOk());

        // Validate the Ask in the database
        List<Ask> askList = askRepository.findAll();
        assertThat(askList).hasSize(databaseSizeBeforeUpdate);
        Ask testAsk = askList.get(askList.size() - 1);
        assertThat(testAsk.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testAsk.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testAsk.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingAsk() throws Exception {
        int databaseSizeBeforeUpdate = askRepository.findAll().size();
        ask.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ask.getId()).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ask))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ask in the database
        List<Ask> askList = askRepository.findAll();
        assertThat(askList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAsk() throws Exception {
        int databaseSizeBeforeUpdate = askRepository.findAll().size();
        ask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ask))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ask in the database
        List<Ask> askList = askRepository.findAll();
        assertThat(askList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAsk() throws Exception {
        int databaseSizeBeforeUpdate = askRepository.findAll().size();
        ask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ask)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ask in the database
        List<Ask> askList = askRepository.findAll();
        assertThat(askList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAskWithPatch() throws Exception {
        // Initialize the database
        askRepository.saveAndFlush(ask);

        int databaseSizeBeforeUpdate = askRepository.findAll().size();

        // Update the ask using partial update
        Ask partialUpdatedAsk = new Ask();
        partialUpdatedAsk.setId(ask.getId());

        partialUpdatedAsk.price(UPDATED_PRICE).status(UPDATED_STATUS);

        restAskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAsk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAsk))
            )
            .andExpect(status().isOk());

        // Validate the Ask in the database
        List<Ask> askList = askRepository.findAll();
        assertThat(askList).hasSize(databaseSizeBeforeUpdate);
        Ask testAsk = askList.get(askList.size() - 1);
        assertThat(testAsk.getNotes()).isEqualTo(DEFAULT_NOTES);
        assertThat(testAsk.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testAsk.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateAskWithPatch() throws Exception {
        // Initialize the database
        askRepository.saveAndFlush(ask);

        int databaseSizeBeforeUpdate = askRepository.findAll().size();

        // Update the ask using partial update
        Ask partialUpdatedAsk = new Ask();
        partialUpdatedAsk.setId(ask.getId());

        partialUpdatedAsk.notes(UPDATED_NOTES).price(UPDATED_PRICE).status(UPDATED_STATUS);

        restAskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAsk.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAsk))
            )
            .andExpect(status().isOk());

        // Validate the Ask in the database
        List<Ask> askList = askRepository.findAll();
        assertThat(askList).hasSize(databaseSizeBeforeUpdate);
        Ask testAsk = askList.get(askList.size() - 1);
        assertThat(testAsk.getNotes()).isEqualTo(UPDATED_NOTES);
        assertThat(testAsk.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testAsk.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingAsk() throws Exception {
        int databaseSizeBeforeUpdate = askRepository.findAll().size();
        ask.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ask))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ask in the database
        List<Ask> askList = askRepository.findAll();
        assertThat(askList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAsk() throws Exception {
        int databaseSizeBeforeUpdate = askRepository.findAll().size();
        ask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ask))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ask in the database
        List<Ask> askList = askRepository.findAll();
        assertThat(askList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAsk() throws Exception {
        int databaseSizeBeforeUpdate = askRepository.findAll().size();
        ask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAskMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ask)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ask in the database
        List<Ask> askList = askRepository.findAll();
        assertThat(askList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAsk() throws Exception {
        // Initialize the database
        askRepository.saveAndFlush(ask);

        int databaseSizeBeforeDelete = askRepository.findAll().size();

        // Delete the ask
        restAskMockMvc.perform(delete(ENTITY_API_URL_ID, ask.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ask> askList = askRepository.findAll();
        assertThat(askList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
