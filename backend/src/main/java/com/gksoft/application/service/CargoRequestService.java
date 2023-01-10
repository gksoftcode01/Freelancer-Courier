package com.gksoft.application.service;

import com.gksoft.application.domain.CargoRequest;
import com.gksoft.application.repository.CargoRequestRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CargoRequest}.
 */
@Service
@Transactional
public class CargoRequestService {

    private final Logger log = LoggerFactory.getLogger(CargoRequestService.class);

    private final CargoRequestRepository cargoRequestRepository;

    public CargoRequestService(CargoRequestRepository cargoRequestRepository) {
        this.cargoRequestRepository = cargoRequestRepository;
    }

    /**
     * Save a cargoRequest.
     *
     * @param cargoRequest the entity to save.
     * @return the persisted entity.
     */
    public CargoRequest save(CargoRequest cargoRequest) {
        log.debug("Request to save CargoRequest : {}", cargoRequest);
        return cargoRequestRepository.save(cargoRequest);
    }

    /**
     * Update a cargoRequest.
     *
     * @param cargoRequest the entity to save.
     * @return the persisted entity.
     */
    public CargoRequest update(CargoRequest cargoRequest) {
        log.debug("Request to update CargoRequest : {}", cargoRequest);
        return cargoRequestRepository.save(cargoRequest);
    }

    /**
     * Partially update a cargoRequest.
     *
     * @param cargoRequest the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CargoRequest> partialUpdate(CargoRequest cargoRequest) {
        log.debug("Request to partially update CargoRequest : {}", cargoRequest);

        return cargoRequestRepository
            .findById(cargoRequest.getId())
            .map(existingCargoRequest -> {
                if (cargoRequest.getBudget() != null) {
                    existingCargoRequest.setBudget(cargoRequest.getBudget());
                }
                if (cargoRequest.getIsToDoor() != null) {
                    existingCargoRequest.setIsToDoor(cargoRequest.getIsToDoor());
                }
                if (cargoRequest.getCreateDate() != null) {
                    existingCargoRequest.setCreateDate(cargoRequest.getCreateDate());
                }
                if (cargoRequest.getAgreedPrice() != null) {
                    existingCargoRequest.setAgreedPrice(cargoRequest.getAgreedPrice());
                }

                return existingCargoRequest;
            })
            .map(cargoRequestRepository::save);
    }

    /**
     * Get all the cargoRequests.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CargoRequest> findAll(Pageable pageable) {
        log.debug("Request to get all CargoRequests");
        return cargoRequestRepository.findAll(pageable);
    }

    /**
     * Get all the cargoRequests with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<CargoRequest> findAllWithEagerRelationships(Pageable pageable) {
        return cargoRequestRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     *  Get all the cargoRequests where UserRate is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CargoRequest> findAllWhereUserRateIsNull() {
        log.debug("Request to get all cargoRequests where UserRate is null");
        return StreamSupport
            .stream(cargoRequestRepository.findAll().spliterator(), false)
            .filter(cargoRequest -> cargoRequest.getUserRate() == null)
            .collect(Collectors.toList());
    }

    /**
     * Get one cargoRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CargoRequest> findOne(Long id) {
        log.debug("Request to get CargoRequest : {}", id);
        return cargoRequestRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the cargoRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CargoRequest : {}", id);
        cargoRequestRepository.deleteById(id);
    }
}
