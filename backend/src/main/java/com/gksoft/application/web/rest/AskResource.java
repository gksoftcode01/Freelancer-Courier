package com.gksoft.application.web.rest;

import com.gksoft.application.domain.Ask;
import com.gksoft.application.repository.AskRepository;
import com.gksoft.application.service.AskService;
import com.gksoft.application.service.FlightService;
import com.gksoft.application.service.NotificationService;
import com.gksoft.application.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.gksoft.application.domain.Ask}.
 */
@RestController
@RequestMapping("/api")
public class AskResource {

    private final Logger log = LoggerFactory.getLogger(AskResource.class);

    private static final String ENTITY_NAME = "ask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AskService askService;

    private final AskRepository askRepository;

    private final NotificationService notificationService;
private  final FlightService flightService;

    public AskResource(AskService askService, AskRepository askRepository, NotificationService notificationService, FlightService flightService) {
        this.askService = askService;
        this.askRepository = askRepository;
        this.notificationService = notificationService;
        this.flightService = flightService;
    }

    /**
     * {@code POST  /asks} : Create a new ask.
     *
     * @param ask the ask to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ask, or with status {@code 400 (Bad Request)} if the ask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asks")
    public ResponseEntity<Ask> createAsk(@RequestBody Ask ask) throws URISyntaxException {
        log.debug("REST request to save Ask : {}", ask);
        if (ask.getId() != null) {
            throw new BadRequestAlertException("A new ask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ask.setCreateDate(Instant.now());
        Ask result = askService.save(ask);
        notificationService.newAskNotify(flightService.findOne(result.getFlight().getId()).get(),   result);
        return ResponseEntity
            .created(new URI("/api/asks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asks/:id} : Updates an existing ask.
     *
     * @param id the id of the ask to save.
     * @param ask the ask to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ask,
     * or with status {@code 400 (Bad Request)} if the ask is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ask couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asks/{id}")
    public ResponseEntity<Ask> updateAsk(@PathVariable(value = "id", required = false) final Long id, @RequestBody Ask ask)
        throws URISyntaxException {
        log.debug("REST request to update Ask : {}, {}", id, ask);
        if (ask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ask.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!askRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ask result = askService.update(ask);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ask.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asks/:id} : Partial updates given fields of an existing ask, field will ignore if it is null
     *
     * @param id the id of the ask to save.
     * @param ask the ask to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ask,
     * or with status {@code 400 (Bad Request)} if the ask is not valid,
     * or with status {@code 404 (Not Found)} if the ask is not found,
     * or with status {@code 500 (Internal Server Error)} if the ask couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ask> partialUpdateAsk(@PathVariable(value = "id", required = false) final Long id, @RequestBody Ask ask)
        throws URISyntaxException {
        log.debug("REST request to partial update Ask partially : {}, {}", id, ask);
        if (ask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ask.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!askRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ask> result = askService.partialUpdate(ask);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ask.getId().toString())
        );
    }

    /**
     * {@code GET  /asks} : get all the asks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of asks in body.
     */
    @GetMapping("/asks")
    public ResponseEntity<List<Ask>> getAllAsks(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Asks");
        Page<Ask> page = askService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asks/:id} : get the "id" ask.
     *
     * @param id the id of the ask to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ask, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asks/{id}")
    public ResponseEntity<Ask> getAsk(@PathVariable Long id) {
        log.debug("REST request to get Ask : {}", id);
        Optional<Ask> ask = askService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ask);
    }

    /**
     * {@code DELETE  /asks/:id} : delete the "id" ask.
     *
     * @param id the id of the ask to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asks/{id}")
    public ResponseEntity<Void> deleteAsk(@PathVariable Long id) {
        log.debug("REST request to delete Ask : {}", id);
        askService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
