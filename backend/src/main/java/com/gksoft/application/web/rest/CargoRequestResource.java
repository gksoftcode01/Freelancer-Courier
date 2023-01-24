package com.gksoft.application.web.rest;

import com.gksoft.application.domain.CargoRequest;
import com.gksoft.application.repository.CargoRequestRepository;
import com.gksoft.application.service.CargoRequestService;
import com.gksoft.application.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link com.gksoft.application.domain.CargoRequest}.
 */
@RestController
@RequestMapping("/api")
public class CargoRequestResource {

    private final Logger log = LoggerFactory.getLogger(CargoRequestResource.class);

    private static final String ENTITY_NAME = "cargoRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CargoRequestService cargoRequestService;

    private final CargoRequestRepository cargoRequestRepository;

    public CargoRequestResource(CargoRequestService cargoRequestService, CargoRequestRepository cargoRequestRepository) {
        this.cargoRequestService = cargoRequestService;
        this.cargoRequestRepository = cargoRequestRepository;
    }

    /**
     * {@code POST  /cargo-requests} : Create a new cargoRequest.
     *
     * @param cargoRequest the cargoRequest to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cargoRequest, or with status {@code 400 (Bad Request)} if the cargoRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cargo-requests")
    public ResponseEntity<CargoRequest> createCargoRequest(@RequestBody CargoRequest cargoRequest) throws URISyntaxException {
        log.debug("REST request to save CargoRequest : {}", cargoRequest);
        if (cargoRequest.getId() != null) {
            throw new BadRequestAlertException("A new cargoRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CargoRequest result = cargoRequestService.save(cargoRequest);
        return ResponseEntity
            .created(new URI("/api/cargo-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cargo-requests/:id} : Updates an existing cargoRequest.
     *
     * @param id the id of the cargoRequest to save.
     * @param cargoRequest the cargoRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoRequest,
     * or with status {@code 400 (Bad Request)} if the cargoRequest is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cargoRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cargo-requests/{id}")
    public ResponseEntity<CargoRequest> updateCargoRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CargoRequest cargoRequest
    ) throws URISyntaxException {
        log.debug("REST request to update CargoRequest : {}, {}", id, cargoRequest);
        if (cargoRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CargoRequest result = cargoRequestService.update(cargoRequest);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoRequest.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cargo-requests/:id} : Partial updates given fields of an existing cargoRequest, field will ignore if it is null
     *
     * @param id the id of the cargoRequest to save.
     * @param cargoRequest the cargoRequest to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cargoRequest,
     * or with status {@code 400 (Bad Request)} if the cargoRequest is not valid,
     * or with status {@code 404 (Not Found)} if the cargoRequest is not found,
     * or with status {@code 500 (Internal Server Error)} if the cargoRequest couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cargo-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CargoRequest> partialUpdateCargoRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CargoRequest cargoRequest
    ) throws URISyntaxException {
        log.debug("REST request to partial update CargoRequest partially : {}, {}", id, cargoRequest);
        if (cargoRequest.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cargoRequest.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cargoRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CargoRequest> result = cargoRequestService.partialUpdate(cargoRequest);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cargoRequest.getId().toString())
        );
    }

    /**
     * {@code GET  /cargo-requests} : get all the cargoRequests.
     *
     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cargoRequests in body.
     */
    @GetMapping("/cargo-requests")
    public ResponseEntity<List<CargoRequest>> getAllCargoRequests(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
         @RequestParam(name = "fromCountry",required = false) Long fromCountry,
        @RequestParam(name = "toCountry",required = false) Long toCountry ,
        @RequestParam(name = "fromState",required = false) Long fromState,
        @RequestParam(name = "toState",required = false) Long toState,
        @RequestParam(name = "createBy",required = false) Long createBy,
        @RequestParam(name = "statusId",required = false) Long statusId,
        @RequestParam(name = "isMine",required = false) boolean isMine,
        @RequestParam(name = "isBidSent",required = false) boolean isBid

    ) {


        log.debug("REST request to get a page of CargoRequests");
        Page<CargoRequest> page;
             page = cargoRequestRepository.srchCargoRequest(fromCountry,toCountry,fromState,toState,createBy,statusId,
                 isMine,isBid,pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /cargo-requests/:id} : get the "id" cargoRequest.
     *
     * @param id the id of the cargoRequest to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cargoRequest, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cargo-requests/{id}")
    public ResponseEntity<CargoRequest> getCargoRequest(@PathVariable Long id) {
        log.debug("REST request to get CargoRequest : {}", id);
        Optional<CargoRequest> cargoRequest = cargoRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cargoRequest);
    }

    /**
     * {@code DELETE  /cargo-requests/:id} : delete the "id" cargoRequest.
     *
     * @param id the id of the cargoRequest to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cargo-requests/{id}")
    public ResponseEntity<Void> deleteCargoRequest(@PathVariable Long id) {
        log.debug("REST request to delete CargoRequest : {}", id);
        cargoRequestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
