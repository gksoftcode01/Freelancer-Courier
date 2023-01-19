package com.gksoft.application.service;

import com.gksoft.application.domain.UserRate;
import com.gksoft.application.repository.UserRateRepository;

import java.util.List;
import java.util.Optional;

import com.gksoft.application.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link UserRate}.
 */
@Service
@Transactional
public class UserRateService {

    private final Logger log = LoggerFactory.getLogger(UserRateService.class);

    private final UserRateRepository userRateRepository;

    private  final UserRepository userRepository;

    public UserRateService(UserRateRepository userRateRepository, UserRepository userRepository) {
        this.userRateRepository = userRateRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a userRate.
     *
     * @param userRate the entity to save.
     * @return the persisted entity.
     */
    public UserRate save(UserRate userRate) {
        log.debug("Request to save UserRate : {}", userRate);
        UserRate ur =  userRateRepository.save(userRate);
        userRepository.updateClienttotalRate(ur.getUser().getId());
        userRepository.updateClientAvgRate(ur.getUser().getId());
        userRepository.updateCouriertotalRate(ur.getUser().getId());
        userRepository.updateCourierAvgRate(ur.getUser().getId());
        return ur;
    }

    /**
     * Update a userRate.
     *
     * @param userRate the entity to save.
     * @return the persisted entity.
     */
    public UserRate update(UserRate userRate) {
        log.debug("Request to update UserRate : {}", userRate);
        UserRate ur =  userRateRepository.save(userRate);
        userRepository.updateClienttotalRate(ur.getUser().getId());
        userRepository.updateClientAvgRate(ur.getUser().getId());
        userRepository.updateCouriertotalRate(ur.getUser().getId());
        userRepository.updateCourierAvgRate(ur.getUser().getId());
        return ur;
    }

    /**
     * Partially update a userRate.
     *
     * @param userRate the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserRate> partialUpdate(UserRate userRate) {
        log.debug("Request to partially update UserRate : {}", userRate);

        return userRateRepository
            .findById(userRate.getId())
            .map(existingUserRate -> {
                if (userRate.getRate() != null) {
                    existingUserRate.setRate(userRate.getRate());
                }
                if (userRate.getNote() != null) {
                    existingUserRate.setNote(userRate.getNote());
                }
                if (userRate.getRateDate() != null) {
                    existingUserRate.setRateDate(userRate.getRateDate());
                }

                return existingUserRate;
            })
            .map(userRateRepository::save);
    }

    /**
     * Get all the userRates.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserRate> findAll(Pageable pageable,Long cargoId,Long userId,Long isCourier) {
        log.debug("Request to get all UserRates");
        return userRateRepository.srchRates( cargoId, userId, isCourier , pageable);
    }


    @Transactional(readOnly = true)
    public List<UserRate> findByCargoRequest(Long cargoRequestId) {
        log.debug("Request to get all UserRates");
        return userRateRepository.findByCargoRequestId(cargoRequestId);
    }


    /**
     * Get one userRate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserRate> findOne(Long id) {
        log.debug("Request to get UserRate : {}", id);
        return userRateRepository.findById(id);
    }

    /**
     * Delete the userRate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete UserRate : {}", id);
        userRateRepository.deleteById(id);
    }
}
