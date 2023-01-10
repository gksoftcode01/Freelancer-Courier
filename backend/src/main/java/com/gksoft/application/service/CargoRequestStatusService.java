package com.gksoft.application.service;

import com.gksoft.application.domain.CargoRequestStatus;
import com.gksoft.application.repository.CargoRequestStatusRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CargoRequestStatus}.
 */
@Service
@Transactional
public class CargoRequestStatusService {

    private final Logger log = LoggerFactory.getLogger(CargoRequestStatusService.class);

    private final CargoRequestStatusRepository cargoRequestStatusRepository;

    public CargoRequestStatusService(CargoRequestStatusRepository cargoRequestStatusRepository) {
        this.cargoRequestStatusRepository = cargoRequestStatusRepository;
    }

    /**
     * Save a cargoRequestStatus.
     *
     * @param cargoRequestStatus the entity to save.
     * @return the persisted entity.
     */
    public CargoRequestStatus save(CargoRequestStatus cargoRequestStatus) {
        log.debug("Request to save CargoRequestStatus : {}", cargoRequestStatus);
        return cargoRequestStatusRepository.save(cargoRequestStatus);
    }

    /**
     * Update a cargoRequestStatus.
     *
     * @param cargoRequestStatus the entity to save.
     * @return the persisted entity.
     */
    public CargoRequestStatus update(CargoRequestStatus cargoRequestStatus) {
        log.debug("Request to update CargoRequestStatus : {}", cargoRequestStatus);
        return cargoRequestStatusRepository.save(cargoRequestStatus);
    }

    /**
     * Partially update a cargoRequestStatus.
     *
     * @param cargoRequestStatus the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CargoRequestStatus> partialUpdate(CargoRequestStatus cargoRequestStatus) {
        log.debug("Request to partially update CargoRequestStatus : {}", cargoRequestStatus);

        return cargoRequestStatusRepository
            .findById(cargoRequestStatus.getId())
            .map(existingCargoRequestStatus -> {
                if (cargoRequestStatus.getName() != null) {
                    existingCargoRequestStatus.setName(cargoRequestStatus.getName());
                }

                return existingCargoRequestStatus;
            })
            .map(cargoRequestStatusRepository::save);
    }

    /**
     * Get all the cargoRequestStatuses.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CargoRequestStatus> findAll(Pageable pageable) {
        log.debug("Request to get all CargoRequestStatuses");
        return cargoRequestStatusRepository.findAll(pageable);
    }

    /**
     * Get one cargoRequestStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CargoRequestStatus> findOne(Long id) {
        log.debug("Request to get CargoRequestStatus : {}", id);
        return cargoRequestStatusRepository.findById(id);
    }

    /**
     * Delete the cargoRequestStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CargoRequestStatus : {}", id);
        cargoRequestStatusRepository.deleteById(id);
    }
}
