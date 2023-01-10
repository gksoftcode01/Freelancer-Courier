package com.gksoft.application.web.rest;

import com.gksoft.application.domain.CargoRequestDetails;
import com.gksoft.application.repository.CargoRequestDetailsRepository;
import com.gksoft.application.service.CargoRequestDetailsService;
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
 * REST controller for managing {@link com.gksoft.application.domain.CargoRequestDetails}.
 */
@RestController
@RequestMapping("/api")
public class CargoRequestDetailsResource {

    private final Logger log = LoggerFactory.getLogger(CargoRequestDetailsResource.class);

    private static final String ENTITY_NAME = "cargoRequestDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CargoRequestDetailsService cargoRequestDetailsService;

    private final CargoRequestDetailsRepository cargoRequestDetailsRepository;

    public CargoRequestDetailsResource(
        CargoRequestDetailsService cargoRequestDetailsService,
        CargoRequestDetailsRepository cargoRequestDetailsRepository
    ) {
        this.cargoRequestDetailsService = cargoRequestDetailsService;
        this.cargoRequestDetailsRepository = cargoRequestDetailsRepository;
    }

    /**
     * {@code POST  /cargo-request-details} : Create a new cargoRequestDetails.
     *
     * @param cargoRequestDetails the cargoRequestDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cargoRequestDetails, or with status {@code 400 (Bad Request)} if the cargoRequestDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cargo-request-details")
    public ResponseEntity<CargoRequestDetails> createCargoRequestDetails(@RequestBody CargoRequestDetails cargoRequestDetails)
        throws URISyntaxException {
        log.debug("REST request to save CargoRequestDetails : {}", cargoRequestDetails);
        if (cargoRequestDetails.getId() != null) {
            throw new BadRequestAlertException("A new cargoRequestDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CargoRequestDetails result = cargoRequestDetailsService.save(cargoRequestDetails);
        return ResponseEntity
            .created(new URI("/api/cargo-request-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cargo-request-details/:id} : Updates an existing cargoRequestDetails.
     *
     * @param id the id of the cargoRequestDetails to save.
     * @param cargoRequestDetails the cargoRequestDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoRequestDetails,
     * or with status {@code 400 (Bad Request)} if the cargoRequestDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cargoRequestDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cargo-request-details/{id}")
    public ResponseEntity<CargoRequestDetails> updateCargoRequestDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CargoRequestDetails cargoRequestDetails
    ) throws URISyntaxException {
        log.debug("REST request to update CargoRequestDetails : {}, {}", id, cargoRequestDetails);
        if (cargoRequestDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoRequestDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoRequestDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CargoRequestDetails result = cargoRequestDetailsService.update(cargoRequestDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoRequestDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cargo-request-details/:id} : Partial updates given fields of an existing cargoRequestDetails, field will ignore if it is null
     *
     * @param id the id of the cargoRequestDetails to save.
     * @param cargoRequestDetails the cargoRequestDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoRequestDetails,
     * or with status {@code 400 (Bad Request)} if the cargoRequestDetails is not valid,
     * or with status {@code 404 (Not Found)} if the cargoRequestDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the cargoRequestDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cargo-request-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CargoRequestDetails> partialUpdateCargoRequestDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CargoRequestDetails cargoRequestDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update CargoRequestDetails partially : {}, {}", id, cargoRequestDetails);
        if (cargoRequestDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoRequestDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoRequestDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CargoRequestDetails> result = cargoRequestDetailsService.partialUpdate(cargoRequestDetails);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoRequestDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /cargo-request-details} : get all the cargoRequestDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cargoRequestDetails in body.
     */
    @GetMapping("/cargo-request-details")
    public ResponseEntity<List<CargoRequestDetails>> getAllCargoRequestDetails(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of CargoRequestDetails");
        Page<CargoRequestDetails> page = cargoRequestDetailsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cargo-request-details/:id} : get the "id" cargoRequestDetails.
     *
     * @param id the id of the cargoRequestDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cargoRequestDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cargo-request-details/{id}")
    public ResponseEntity<CargoRequestDetails> getCargoRequestDetails(@PathVariable Long id) {
        log.debug("REST request to get CargoRequestDetails : {}", id);
        Optional<CargoRequestDetails> cargoRequestDetails = cargoRequestDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cargoRequestDetails);
    }

    /**
     * {@code DELETE  /cargo-request-details/:id} : delete the "id" cargoRequestDetails.
     *
     * @param id the id of the cargoRequestDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cargo-request-details/{id}")
    public ResponseEntity<Void> deleteCargoRequestDetails(@PathVariable Long id) {
        log.debug("REST request to delete CargoRequestDetails : {}", id);
        cargoRequestDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
