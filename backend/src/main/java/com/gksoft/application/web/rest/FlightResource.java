package com.gksoft.application.web.rest;

import com.gksoft.application.domain.Flight;
import com.gksoft.application.repository.FlightRepository;
import com.gksoft.application.service.FlightService;
import com.gksoft.application.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gksoft.application.domain.Flight}.
 */
@RestController
@RequestMapping("/api")
public class FlightResource {

    private final Logger log = LoggerFactory.getLogger(FlightResource.class);

    private static final String ENTITY_NAME = "flight";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FlightService flightService;

    private final FlightRepository flightRepository;

    public FlightResource(FlightService flightService, FlightRepository flightRepository) {
        this.flightService = flightService;
        this.flightRepository = flightRepository;
    }

    /**
     * {@code POST  /flights} : Create a new flight.
     *
     * @param flight the flight to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new flight, or with status {@code 400 (Bad Request)} if the flight has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/flights")
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) throws URISyntaxException {
        log.debug("REST request to save Flight : {}", flight);
        if (flight.getId() != null) {
            throw new BadRequestAlertException("A new flight cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Flight result = flightService.save(flight);
        return ResponseEntity
            .created(new URI("/api/flights/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /flights/:id} : Updates an existing flight.
     *
     * @param id the id of the flight to save.
     * @param flight the flight to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flight,
     * or with status {@code 400 (Bad Request)} if the flight is not valid,
     * or with status {@code 500 (Internal Server Error)} if the flight couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/flights/{id}")
    public ResponseEntity<Flight> updateFlight(@PathVariable(value = "id", required = false) final Long id, @RequestBody Flight flight)
        throws URISyntaxException {
        log.debug("REST request to update Flight : {}, {}", id, flight);
        if (flight.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flight.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flightRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Flight result = flightService.update(flight);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flight.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /flights/:id} : Partial updates given fields of an existing flight, field will ignore if it is null
     *
     * @param id the id of the flight to save.
     * @param flight the flight to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated flight,
     * or with status {@code 400 (Bad Request)} if the flight is not valid,
     * or with status {@code 404 (Not Found)} if the flight is not found,
     * or with status {@code 500 (Internal Server Error)} if the flight couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/flights/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Flight> partialUpdateFlight(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Flight flight
    ) throws URISyntaxException {
        log.debug("REST request to partial update Flight partially : {}, {}", id, flight);
        if (flight.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, flight.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!flightRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Flight> result = flightService.partialUpdate(flight);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, flight.getId().toString())
        );
    }

    /**
     * {@code GET  /flights} : get all the flights.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of flights in body.
     */
    @GetMapping("/flights")
    public ResponseEntity<List<Flight>> getAllFlights(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Flights");
        Page<Flight> page;
        if (eagerload) {
            page = flightService.findAllWithEagerRelationships(pageable);
        } else {
            page = flightService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /flights/:id} : get the "id" flight.
     *
     * @param id the id of the flight to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the flight, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/flights/{id}")
    public ResponseEntity<Flight> getFlight(@PathVariable Long id) {
        log.debug("REST request to get Flight : {}", id);
        Optional<Flight> flight = flightService.findOne(id);
        return ResponseUtil.wrapOrNotFound(flight);
    }

    /**
     * {@code DELETE  /flights/:id} : delete the "id" flight.
     *
     * @param id the id of the flight to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/flights/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        log.debug("REST request to delete Flight : {}", id);
        flightService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
