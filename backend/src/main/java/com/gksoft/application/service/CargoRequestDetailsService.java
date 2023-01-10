package com.gksoft.application.service;

import com.gksoft.application.domain.CargoRequestDetails;
import com.gksoft.application.repository.CargoRequestDetailsRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link CargoRequestDetails}.
 */
@Service
@Transactional
public class CargoRequestDetailsService {

    private final Logger log = LoggerFactory.getLogger(CargoRequestDetailsService.class);

    private final CargoRequestDetailsRepository cargoRequestDetailsRepository;

    public CargoRequestDetailsService(CargoRequestDetailsRepository cargoRequestDetailsRepository) {
        this.cargoRequestDetailsRepository = cargoRequestDetailsRepository;
    }

    /**
     * Save a cargoRequestDetails.
     *
     * @param cargoRequestDetails the entity to save.
     * @return the persisted entity.
     */
    public CargoRequestDetails save(CargoRequestDetails cargoRequestDetails) {
        log.debug("Request to save CargoRequestDetails : {}", cargoRequestDetails);
        return cargoRequestDetailsRepository.save(cargoRequestDetails);
    }

    /**
     * Update a cargoRequestDetails.
     *
     * @param cargoRequestDetails the entity to save.
     * @return the persisted entity.
     */
    public CargoRequestDetails update(CargoRequestDetails cargoRequestDetails) {
        log.debug("Request to update CargoRequestDetails : {}", cargoRequestDetails);
        return cargoRequestDetailsRepository.save(cargoRequestDetails);
    }

    /**
     * Partially update a cargoRequestDetails.
     *
     * @param cargoRequestDetails the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<CargoRequestDetails> partialUpdate(CargoRequestDetails cargoRequestDetails) {
        log.debug("Request to partially update CargoRequestDetails : {}", cargoRequestDetails);

        return cargoRequestDetailsRepository
            .findById(cargoRequestDetails.getId())
            .map(existingCargoRequestDetails -> {
                if (cargoRequestDetails.getItemDesc() != null) {
                    existingCargoRequestDetails.setItemDesc(cargoRequestDetails.getItemDesc());
                }
                if (cargoRequestDetails.getItemWeight() != null) {
                    existingCargoRequestDetails.setItemWeight(cargoRequestDetails.getItemWeight());
                }
                if (cargoRequestDetails.getItemHeight() != null) {
                    existingCargoRequestDetails.setItemHeight(cargoRequestDetails.getItemHeight());
                }
                if (cargoRequestDetails.getItemWidth() != null) {
                    existingCargoRequestDetails.setItemWidth(cargoRequestDetails.getItemWidth());
                }
                if (cargoRequestDetails.getItemPhoto() != null) {
                    existingCargoRequestDetails.setItemPhoto(cargoRequestDetails.getItemPhoto());
                }
                if (cargoRequestDetails.getItemPhotoContentType() != null) {
                    existingCargoRequestDetails.setItemPhotoContentType(cargoRequestDetails.getItemPhotoContentType());
                }

                return existingCargoRequestDetails;
            })
            .map(cargoRequestDetailsRepository::save);
    }

    /**
     * Get all the cargoRequestDetails.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<CargoRequestDetails> findAll(Pageable pageable) {
        log.debug("Request to get all CargoRequestDetails");
        return cargoRequestDetailsRepository.findAll(pageable);
    }

    /**
     * Get one cargoRequestDetails by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CargoRequestDetails> findOne(Long id) {
        log.debug("Request to get CargoRequestDetails : {}", id);
        return cargoRequestDetailsRepository.findById(id);
    }

    /**
     * Delete the cargoRequestDetails by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CargoRequestDetails : {}", id);
        cargoRequestDetailsRepository.deleteById(id);
    }
}
