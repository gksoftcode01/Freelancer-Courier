package com.gksoft.application.service;

import com.gksoft.application.domain.ItemTypes;
import com.gksoft.application.repository.ItemTypesRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ItemTypes}.
 */
@Service
@Transactional
public class ItemTypesService {

    private final Logger log = LoggerFactory.getLogger(ItemTypesService.class);

    private final ItemTypesRepository itemTypesRepository;

    public ItemTypesService(ItemTypesRepository itemTypesRepository) {
        this.itemTypesRepository = itemTypesRepository;
    }

    /**
     * Save a itemTypes.
     *
     * @param itemTypes the entity to save.
     * @return the persisted entity.
     */
    public ItemTypes save(ItemTypes itemTypes) {
        log.debug("Request to save ItemTypes : {}", itemTypes);
        return itemTypesRepository.save(itemTypes);
    }

    /**
     * Update a itemTypes.
     *
     * @param itemTypes the entity to save.
     * @return the persisted entity.
     */
    public ItemTypes update(ItemTypes itemTypes) {
        log.debug("Request to update ItemTypes : {}", itemTypes);
        return itemTypesRepository.save(itemTypes);
    }

    /**
     * Partially update a itemTypes.
     *
     * @param itemTypes the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ItemTypes> partialUpdate(ItemTypes itemTypes) {
        log.debug("Request to partially update ItemTypes : {}", itemTypes);

        return itemTypesRepository
            .findById(itemTypes.getId())
            .map(existingItemTypes -> {
                if (itemTypes.getName() != null) {
                    existingItemTypes.setName(itemTypes.getName());
                }

                return existingItemTypes;
            })
            .map(itemTypesRepository::save);
    }

    /**
     * Get all the itemTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ItemTypes> findAll(Pageable pageable) {
        log.debug("Request to get all ItemTypes");
        return itemTypesRepository.findAll(pageable);
    }

    /**
     * Get one itemTypes by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ItemTypes> findOne(Long id) {
        log.debug("Request to get ItemTypes : {}", id);
        return itemTypesRepository.findById(id);
    }

    /**
     * Delete the itemTypes by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ItemTypes : {}", id);
        itemTypesRepository.deleteById(id);
    }
}
