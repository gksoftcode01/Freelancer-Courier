package com.gksoft.application.service;

import com.gksoft.application.domain.Ask;
import com.gksoft.application.repository.AskRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Ask}.
 */
@Service
@Transactional
public class AskService {

    private final Logger log = LoggerFactory.getLogger(AskService.class);

    private final AskRepository askRepository;

    public AskService(AskRepository askRepository) {
        this.askRepository = askRepository;
    }

    /**
     * Save a ask.
     *
     * @param ask the entity to save.
     * @return the persisted entity.
     */
    public Ask save(Ask ask) {
        log.debug("Request to save Ask : {}", ask);
        return askRepository.save(ask);
    }

    /**
     * Update a ask.
     *
     * @param ask the entity to save.
     * @return the persisted entity.
     */
    public Ask update(Ask ask) {
        log.debug("Request to update Ask : {}", ask);
        return askRepository.save(ask);
    }

    /**
     * Partially update a ask.
     *
     * @param ask the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Ask> partialUpdate(Ask ask) {
        log.debug("Request to partially update Ask : {}", ask);

        return askRepository
            .findById(ask.getId())
            .map(existingAsk -> {
                if (ask.getNotes() != null) {
                    existingAsk.setNotes(ask.getNotes());
                }
                if (ask.getPrice() != null) {
                    existingAsk.setPrice(ask.getPrice());
                }
                if (ask.getStatus() != null) {
                    existingAsk.setStatus(ask.getStatus());
                }

                return existingAsk;
            })
            .map(askRepository::save);
    }

    /**
     * Get all the asks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Ask> findAll(Pageable pageable) {
        log.debug("Request to get all Asks");
        return askRepository.findAll(pageable);
    }

    /**
     * Get one ask by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Ask> findOne(Long id) {
        log.debug("Request to get Ask : {}", id);
        return askRepository.findById(id);
    }

    /**
     * Delete the ask by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Ask : {}", id);
        askRepository.deleteById(id);
    }
}
