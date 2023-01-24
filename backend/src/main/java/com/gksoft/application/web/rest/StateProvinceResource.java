package com.gksoft.application.web.rest;

import com.gksoft.application.domain.StateProvince;
import com.gksoft.application.repository.StateProvinceRepository;
import com.gksoft.application.service.StateProvinceService;
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
 * REST controller for managing {@link com.gksoft.application.domain.StateProvince}.
 */
@RestController
@RequestMapping("/api")
public class StateProvinceResource {

    private final Logger log = LoggerFactory.getLogger(StateProvinceResource.class);

    private static final String ENTITY_NAME = "stateProvince";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StateProvinceService stateProvinceService;

    private final StateProvinceRepository stateProvinceRepository;

    public StateProvinceResource(StateProvinceService stateProvinceService, StateProvinceRepository stateProvinceRepository) {
        this.stateProvinceService = stateProvinceService;
        this.stateProvinceRepository = stateProvinceRepository;
    }

    /**
     * {@code POST  /state-provinces} : Create a new stateProvince.
     *
     * @param stateProvince the stateProvince to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stateProvince, or with status {@code 400 (Bad Request)} if the stateProvince has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/state-provinces")
    public ResponseEntity<StateProvince> createStateProvince(@RequestBody StateProvince stateProvince) throws URISyntaxException {
        log.debug("REST request to save StateProvince : {}", stateProvince);
        if (stateProvince.getId() != null) {
            throw new BadRequestAlertException("A new stateProvince cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StateProvince result = stateProvinceService.save(stateProvince);
        return ResponseEntity
            .created(new URI("/api/state-provinces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /state-provinces/:id} : Updates an existing stateProvince.
     *
     * @param id the id of the stateProvince to save.
     * @param stateProvince the stateProvince to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stateProvince,
     * or with status {@code 400 (Bad Request)} if the stateProvince is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stateProvince couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/state-provinces/{id}")
    public ResponseEntity<StateProvince> updateStateProvince(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StateProvince stateProvince
    ) throws URISyntaxException {
        log.debug("REST request to update StateProvince : {}, {}", id, stateProvince);
        if (stateProvince.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stateProvince.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateProvinceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StateProvince result = stateProvinceService.update(stateProvince);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stateProvince.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /state-provinces/:id} : Partial updates given fields of an existing stateProvince, field will ignore if it is null
     *
     * @param id the id of the stateProvince to save.
     * @param stateProvince the stateProvince to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stateProvince,
     * or with status {@code 400 (Bad Request)} if the stateProvince is not valid,
     * or with status {@code 404 (Not Found)} if the stateProvince is not found,
     * or with status {@code 500 (Internal Server Error)} if the stateProvince couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/state-provinces/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StateProvince> partialUpdateStateProvince(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StateProvince stateProvince
    ) throws URISyntaxException {
        log.debug("REST request to partial update StateProvince partially : {}, {}", id, stateProvince);
        if (stateProvince.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stateProvince.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stateProvinceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StateProvince> result = stateProvinceService.partialUpdate(stateProvince);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stateProvince.getId().toString())
        );
    }

    /**
     * {@code GET  /state-provinces} : get all the stateProvinces.
     *
     * @param countryId the   information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stateProvinces in body.
     */
    @GetMapping("/state-provinces")
    public ResponseEntity<List<StateProvince>> getAllStateProvinces(
        @RequestParam(name = "countryId",required = false) Long countryId     ) {
        log.debug("REST request to get a page of StateProvinces");
        List<StateProvince> allByCountryId = stateProvinceRepository.findAllByCountryId(countryId);
         return ResponseEntity.ok(allByCountryId);
    }

    /**
     * {@code GET  /state-provinces/:id} : get the "id" stateProvince.
     *
     * @param id the id of the stateProvince to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stateProvince, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/state-provinces/{id}")
    public ResponseEntity<StateProvince> getStateProvince(@PathVariable Long id) {
        log.debug("REST request to get StateProvince : {}", id);
        Optional<StateProvince> stateProvince = stateProvinceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(stateProvince);
    }

    /**
     * {@code DELETE  /state-provinces/:id} : delete the "id" stateProvince.
     *
     * @param id the id of the stateProvince to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/state-provinces/{id}")
    public ResponseEntity<Void> deleteStateProvince(@PathVariable Long id) {
        log.debug("REST request to delete StateProvince : {}", id);
        stateProvinceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
