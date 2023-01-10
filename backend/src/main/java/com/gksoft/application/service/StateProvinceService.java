package com.gksoft.application.service;

import com.gksoft.application.domain.StateProvince;
import com.gksoft.application.repository.StateProvinceRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link StateProvince}.
 */
@Service
@Transactional
public class StateProvinceService {

    private final Logger log = LoggerFactory.getLogger(StateProvinceService.class);

    private final StateProvinceRepository stateProvinceRepository;

    public StateProvinceService(StateProvinceRepository stateProvinceRepository) {
        this.stateProvinceRepository = stateProvinceRepository;
    }

    /**
     * Save a stateProvince.
     *
     * @param stateProvince the entity to save.
     * @return the persisted entity.
     */
    public StateProvince save(StateProvince stateProvince) {
        log.debug("Request to save StateProvince : {}", stateProvince);
        return stateProvinceRepository.save(stateProvince);
    }

    /**
     * Update a stateProvince.
     *
     * @param stateProvince the entity to save.
     * @return the persisted entity.
     */
    public StateProvince update(StateProvince stateProvince) {
        log.debug("Request to update StateProvince : {}", stateProvince);
        return stateProvinceRepository.save(stateProvince);
    }

    /**
     * Partially update a stateProvince.
     *
     * @param stateProvince the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<StateProvince> partialUpdate(StateProvince stateProvince) {
        log.debug("Request to partially update StateProvince : {}", stateProvince);

        return stateProvinceRepository
            .findById(stateProvince.getId())
            .map(existingStateProvince -> {
                if (stateProvince.getName() != null) {
                    existingStateProvince.setName(stateProvince.getName());
                }

                return existingStateProvince;
            })
            .map(stateProvinceRepository::save);
    }

    /**
     * Get all the stateProvinces.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<StateProvince> findAll(Pageable pageable) {
        log.debug("Request to get all StateProvinces");
        return stateProvinceRepository.findAll(pageable);
    }

    /**
     * Get one stateProvince by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<StateProvince> findOne(Long id) {
        log.debug("Request to get StateProvince : {}", id);
        return stateProvinceRepository.findById(id);
    }

    /**
     * Delete the stateProvince by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete StateProvince : {}", id);
        stateProvinceRepository.deleteById(id);
    }
}
