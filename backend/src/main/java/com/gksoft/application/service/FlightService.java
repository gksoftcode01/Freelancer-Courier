package com.gksoft.application.service;

import com.gksoft.application.domain.Flight;
import com.gksoft.application.repository.FlightRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Flight}.
 */
@Service
@Transactional
public class FlightService {

    private final Logger log = LoggerFactory.getLogger(FlightService.class);

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    /**
     * Save a flight.
     *
     * @param flight the entity to save.
     * @return the persisted entity.
     */
    public Flight save(Flight flight) {
        log.debug("Request to save Flight : {}", flight);
        return flightRepository.save(flight);
    }

    /**
     * Update a flight.
     *
     * @param flight the entity to save.
     * @return the persisted entity.
     */
    public Flight update(Flight flight) {
        log.debug("Request to update Flight : {}", flight);
        return flightRepository.save(flight);
    }

    /**
     * Partially update a flight.
     *
     * @param flight the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<Flight> partialUpdate(Flight flight) {
        log.debug("Request to partially update Flight : {}", flight);

        return flightRepository
            .findById(flight.getId())
            .map(existingFlight -> {
                if (flight.getFlightDate() != null) {
                    existingFlight.setFlightDate(flight.getFlightDate());
                }
                if (flight.getMaxWeight() != null) {
                    existingFlight.setMaxWeight(flight.getMaxWeight());
                }
                if (flight.getNotes() != null) {
                    existingFlight.setNotes(flight.getNotes());
                }
                if (flight.getBudget() != null) {
                    existingFlight.setBudget(flight.getBudget());
                }
                if (flight.getCreateDate() != null) {
                    existingFlight.setCreateDate(flight.getCreateDate());
                }
                if (flight.getToDoorAvailable() != null) {
                    existingFlight.setToDoorAvailable(flight.getToDoorAvailable());
                }
                if (flight.getStatus() != null) {
                    existingFlight.setStatus(flight.getStatus());
                }

                return existingFlight;
            })
            .map(flightRepository::save);
    }

    /**
     * Get all the flights.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Flight> findAll(Pageable pageable) {
        log.debug("Request to get all Flights");
        return flightRepository.findAll(pageable);
    }

    /**
     * Get all the flights with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Flight> findAllWithEagerRelationships(Pageable pageable) {
        return flightRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one flight by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Flight> findOne(Long id) {
        log.debug("Request to get Flight : {}", id);
        return flightRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the flight by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Flight : {}", id);
        flightRepository.deleteById(id);
    }
}
