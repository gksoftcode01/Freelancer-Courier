package com.gksoft.application.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.gksoft.application.IntegrationTest;
import com.gksoft.application.domain.ItemTypes;
import com.gksoft.application.repository.ItemTypesRepository;
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
 * Integration tests for the {@link ItemTypesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ItemTypesResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/item-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ItemTypesRepository itemTypesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restItemTypesMockMvc;

    private ItemTypes itemTypes;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemTypes createEntity(EntityManager em) {
        ItemTypes itemTypes = new ItemTypes().name(DEFAULT_NAME);
        return itemTypes;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ItemTypes createUpdatedEntity(EntityManager em) {
        ItemTypes itemTypes = new ItemTypes().name(UPDATED_NAME);
        return itemTypes;
    }

    @BeforeEach
    public void initTest() {
        itemTypes = createEntity(em);
    }

    @Test
    @Transactional
    void createItemTypes() throws Exception {
        int databaseSizeBeforeCreate = itemTypesRepository.findAll().size();
        // Create the ItemTypes
        restItemTypesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemTypes)))
            .andExpect(status().isCreated());

        // Validate the ItemTypes in the database
        List<ItemTypes> itemTypesList = itemTypesRepository.findAll();
        assertThat(itemTypesList).hasSize(databaseSizeBeforeCreate + 1);
        ItemTypes testItemTypes = itemTypesList.get(itemTypesList.size() - 1);
        assertThat(testItemTypes.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createItemTypesWithExistingId() throws Exception {
        // Create the ItemTypes with an existing ID
        itemTypes.setId(1L);

        int databaseSizeBeforeCreate = itemTypesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restItemTypesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemTypes)))
            .andExpect(status().isBadRequest());

        // Validate the ItemTypes in the database
        List<ItemTypes> itemTypesList = itemTypesRepository.findAll();
        assertThat(itemTypesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = itemTypesRepository.findAll().size();
        // set the field null
        itemTypes.setName(null);

        // Create the ItemTypes, which fails.

        restItemTypesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemTypes)))
            .andExpect(status().isBadRequest());

        List<ItemTypes> itemTypesList = itemTypesRepository.findAll();
        assertThat(itemTypesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllItemTypes() throws Exception {
        // Initialize the database
        itemTypesRepository.saveAndFlush(itemTypes);

        // Get all the itemTypesList
        restItemTypesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itemTypes.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getItemTypes() throws Exception {
        // Initialize the database
        itemTypesRepository.saveAndFlush(itemTypes);

        // Get the itemTypes
        restItemTypesMockMvc
            .perform(get(ENTITY_API_URL_ID, itemTypes.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(itemTypes.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingItemTypes() throws Exception {
        // Get the itemTypes
        restItemTypesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingItemTypes() throws Exception {
        // Initialize the database
        itemTypesRepository.saveAndFlush(itemTypes);

        int databaseSizeBeforeUpdate = itemTypesRepository.findAll().size();

        // Update the itemTypes
        ItemTypes updatedItemTypes = itemTypesRepository.findById(itemTypes.getId()).get();
        // Disconnect from session so that the updates on updatedItemTypes are not directly saved in db
        em.detach(updatedItemTypes);
        updatedItemTypes.name(UPDATED_NAME);

        restItemTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedItemTypes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedItemTypes))
            )
            .andExpect(status().isOk());

        // Validate the ItemTypes in the database
        List<ItemTypes> itemTypesList = itemTypesRepository.findAll();
        assertThat(itemTypesList).hasSize(databaseSizeBeforeUpdate);
        ItemTypes testItemTypes = itemTypesList.get(itemTypesList.size() - 1);
        assertThat(testItemTypes.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingItemTypes() throws Exception {
        int databaseSizeBeforeUpdate = itemTypesRepository.findAll().size();
        itemTypes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, itemTypes.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemTypes in the database
        List<ItemTypes> itemTypesList = itemTypesRepository.findAll();
        assertThat(itemTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchItemTypes() throws Exception {
        int databaseSizeBeforeUpdate = itemTypesRepository.findAll().size();
        itemTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTypesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(itemTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemTypes in the database
        List<ItemTypes> itemTypesList = itemTypesRepository.findAll();
        assertThat(itemTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamItemTypes() throws Exception {
        int databaseSizeBeforeUpdate = itemTypesRepository.findAll().size();
        itemTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTypesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(itemTypes)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemTypes in the database
        List<ItemTypes> itemTypesList = itemTypesRepository.findAll();
        assertThat(itemTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateItemTypesWithPatch() throws Exception {
        // Initialize the database
        itemTypesRepository.saveAndFlush(itemTypes);

        int databaseSizeBeforeUpdate = itemTypesRepository.findAll().size();

        // Update the itemTypes using partial update
        ItemTypes partialUpdatedItemTypes = new ItemTypes();
        partialUpdatedItemTypes.setId(itemTypes.getId());

        restItemTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemTypes))
            )
            .andExpect(status().isOk());

        // Validate the ItemTypes in the database
        List<ItemTypes> itemTypesList = itemTypesRepository.findAll();
        assertThat(itemTypesList).hasSize(databaseSizeBeforeUpdate);
        ItemTypes testItemTypes = itemTypesList.get(itemTypesList.size() - 1);
        assertThat(testItemTypes.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateItemTypesWithPatch() throws Exception {
        // Initialize the database
        itemTypesRepository.saveAndFlush(itemTypes);

        int databaseSizeBeforeUpdate = itemTypesRepository.findAll().size();

        // Update the itemTypes using partial update
        ItemTypes partialUpdatedItemTypes = new ItemTypes();
        partialUpdatedItemTypes.setId(itemTypes.getId());

        partialUpdatedItemTypes.name(UPDATED_NAME);

        restItemTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedItemTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedItemTypes))
            )
            .andExpect(status().isOk());

        // Validate the ItemTypes in the database
        List<ItemTypes> itemTypesList = itemTypesRepository.findAll();
        assertThat(itemTypesList).hasSize(databaseSizeBeforeUpdate);
        ItemTypes testItemTypes = itemTypesList.get(itemTypesList.size() - 1);
        assertThat(testItemTypes.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingItemTypes() throws Exception {
        int databaseSizeBeforeUpdate = itemTypesRepository.findAll().size();
        itemTypes.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restItemTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, itemTypes.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemTypes in the database
        List<ItemTypes> itemTypesList = itemTypesRepository.findAll();
        assertThat(itemTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchItemTypes() throws Exception {
        int databaseSizeBeforeUpdate = itemTypesRepository.findAll().size();
        itemTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTypesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(itemTypes))
            )
            .andExpect(status().isBadRequest());

        // Validate the ItemTypes in the database
        List<ItemTypes> itemTypesList = itemTypesRepository.findAll();
        assertThat(itemTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamItemTypes() throws Exception {
        int databaseSizeBeforeUpdate = itemTypesRepository.findAll().size();
        itemTypes.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restItemTypesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(itemTypes))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ItemTypes in the database
        List<ItemTypes> itemTypesList = itemTypesRepository.findAll();
        assertThat(itemTypesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteItemTypes() throws Exception {
        // Initialize the database
        itemTypesRepository.saveAndFlush(itemTypes);

        int databaseSizeBeforeDelete = itemTypesRepository.findAll().size();

        // Delete the itemTypes
        restItemTypesMockMvc
            .perform(delete(ENTITY_API_URL_ID, itemTypes.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ItemTypes> itemTypesList = itemTypesRepository.findAll();
        assertThat(itemTypesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
