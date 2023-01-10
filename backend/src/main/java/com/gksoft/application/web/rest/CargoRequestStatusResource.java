package com.gksoft.application.web.rest;

import com.gksoft.application.domain.CargoRequestStatus;
import com.gksoft.application.repository.CargoRequestStatusRepository;
import com.gksoft.application.service.CargoRequestStatusService;
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
 * REST controller for managing {@link com.gksoft.application.domain.CargoRequestStatus}.
 */
@RestController
@RequestMapping("/api")
public class CargoRequestStatusResource {

    private final Logger log = LoggerFactory.getLogger(CargoRequestStatusResource.class);

    private static final String ENTITY_NAME = "cargoRequestStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CargoRequestStatusService cargoRequestStatusService;

    private final CargoRequestStatusRepository cargoRequestStatusRepository;

    public CargoRequestStatusResource(
        CargoRequestStatusService cargoRequestStatusService,
        CargoRequestStatusRepository cargoRequestStatusRepository
    ) {
        this.cargoRequestStatusService = cargoRequestStatusService;
        this.cargoRequestStatusRepository = cargoRequestStatusRepository;
    }

    /**
     * {@code POST  /cargo-request-statuses} : Create a new cargoRequestStatus.
     *
     * @param cargoRequestStatus the cargoRequestStatus to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cargoRequestStatus, or with status {@code 400 (Bad Request)} if the cargoRequestStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cargo-request-statuses")
    public ResponseEntity<CargoRequestStatus> createCargoRequestStatus(@RequestBody CargoRequestStatus cargoRequestStatus)
        throws URISyntaxException {
        log.debug("REST request to save CargoRequestStatus : {}", cargoRequestStatus);
        if (cargoRequestStatus.getId() != null) {
            throw new BadRequestAlertException("A new cargoRequestStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CargoRequestStatus result = cargoRequestStatusService.save(cargoRequestStatus);
        return ResponseEntity
            .created(new URI("/api/cargo-request-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cargo-request-statuses/:id} : Updates an existing cargoRequestStatus.
     *
     * @param id the id of the cargoRequestStatus to save.
     * @param cargoRequestStatus the cargoRequestStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoRequestStatus,
     * or with status {@code 400 (Bad Request)} if the cargoRequestStatus is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cargoRequestStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cargo-request-statuses/{id}")
    public ResponseEntity<CargoRequestStatus> updateCargoRequestStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CargoRequestStatus cargoRequestStatus
    ) throws URISyntaxException {
        log.debug("REST request to update CargoRequestStatus : {}, {}", id, cargoRequestStatus);
        if (cargoRequestStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoRequestStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoRequestStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CargoRequestStatus result = cargoRequestStatusService.update(cargoRequestStatus);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoRequestStatus.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cargo-request-statuses/:id} : Partial updates given fields of an existing cargoRequestStatus, field will ignore if it is null
     *
     * @param id the id of the cargoRequestStatus to save.
     * @param cargoRequestStatus the cargoRequestStatus to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoRequestStatus,
     * or with status {@code 400 (Bad Request)} if the cargoRequestStatus is not valid,
     * or with status {@code 404 (Not Found)} if the cargoRequestStatus is not found,
     * or with status {@code 500 (Internal Server Error)} if the cargoRequestStatus couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cargo-request-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CargoRequestStatus> partialUpdateCargoRequestStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CargoRequestStatus cargoRequestStatus
    ) throws URISyntaxException {
        log.debug("REST request to partial update CargoRequestStatus partially : {}, {}", id, cargoRequestStatus);
        if (cargoRequestStatus.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoRequestStatus.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoRequestStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CargoRequestStatus> result = cargoRequestStatusService.partialUpdate(cargoRequestStatus);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoRequestStatus.getId().toString())
        );
    }

    /**
     * {@code GET  /cargo-request-statuses} : get all the cargoRequestStatuses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cargoRequestStatuses in body.
     */
    @GetMapping("/cargo-request-statuses")
    public ResponseEntity<List<CargoRequestStatus>> getAllCargoRequestStatuses(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of CargoRequestStatuses");
        Page<CargoRequestStatus> page = cargoRequestStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cargo-request-statuses/:id} : get the "id" cargoRequestStatus.
     *
     * @param id the id of the cargoRequestStatus to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cargoRequestStatus, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cargo-request-statuses/{id}")
    public ResponseEntity<CargoRequestStatus> getCargoRequestStatus(@PathVariable Long id) {
        log.debug("REST request to get CargoRequestStatus : {}", id);
        Optional<CargoRequestStatus> cargoRequestStatus = cargoRequestStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cargoRequestStatus);
    }

    /**
     * {@code DELETE  /cargo-request-statuses/:id} : delete the "id" cargoRequestStatus.
     *
     * @param id the id of the cargoRequestStatus to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cargo-request-statuses/{id}")
    public ResponseEntity<Void> deleteCargoRequestStatus(@PathVariable Long id) {
        log.debug("REST request to delete CargoRequestStatus : {}", id);
        cargoRequestStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
