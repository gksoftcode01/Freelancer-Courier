package com.gksoft.application.web.rest;

import com.gksoft.application.domain.ItemTypes;
import com.gksoft.application.repository.ItemTypesRepository;
import com.gksoft.application.service.ItemTypesService;
import com.gksoft.application.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.gksoft.application.domain.ItemTypes}.
 */
@RestController
@RequestMapping("/api")
public class ItemTypesResource {

    private final Logger log = LoggerFactory.getLogger(ItemTypesResource.class);

    private static final String ENTITY_NAME = "itemTypes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ItemTypesService itemTypesService;

    private final ItemTypesRepository itemTypesRepository;

    public ItemTypesResource(ItemTypesService itemTypesService, ItemTypesRepository itemTypesRepository) {
        this.itemTypesService = itemTypesService;
        this.itemTypesRepository = itemTypesRepository;
    }

    /**
     * {@code POST  /item-types} : Create a new itemTypes.
     *
     * @param itemTypes the itemTypes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new itemTypes, or with status {@code 400 (Bad Request)} if the itemTypes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/item-types")
    public ResponseEntity<ItemTypes> createItemTypes(@Valid @RequestBody ItemTypes itemTypes) throws URISyntaxException {
        log.debug("REST request to save ItemTypes : {}", itemTypes);
        if (itemTypes.getId() != null) {
            throw new BadRequestAlertException("A new itemTypes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ItemTypes result = itemTypesService.save(itemTypes);
        return ResponseEntity
            .created(new URI("/api/item-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /item-types/:id} : Updates an existing itemTypes.
     *
     * @param id the id of the itemTypes to save.
     * @param itemTypes the itemTypes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemTypes,
     * or with status {@code 400 (Bad Request)} if the itemTypes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the itemTypes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/item-types/{id}")
    public ResponseEntity<ItemTypes> updateItemTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ItemTypes itemTypes
    ) throws URISyntaxException {
        log.debug("REST request to update ItemTypes : {}, {}", id, itemTypes);
        if (itemTypes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemTypes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ItemTypes result = itemTypesService.update(itemTypes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemTypes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /item-types/:id} : Partial updates given fields of an existing itemTypes, field will ignore if it is null
     *
     * @param id the id of the itemTypes to save.
     * @param itemTypes the itemTypes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated itemTypes,
     * or with status {@code 400 (Bad Request)} if the itemTypes is not valid,
     * or with status {@code 404 (Not Found)} if the itemTypes is not found,
     * or with status {@code 500 (Internal Server Error)} if the itemTypes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/item-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ItemTypes> partialUpdateItemTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ItemTypes itemTypes
    ) throws URISyntaxException {
        log.debug("REST request to partial update ItemTypes partially : {}, {}", id, itemTypes);
        if (itemTypes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, itemTypes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!itemTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ItemTypes> result = itemTypesService.partialUpdate(itemTypes);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, itemTypes.getId().toString())
        );
    }

    /**
     * {@code GET  /item-types} : get all the itemTypes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of itemTypes in body.
     */
    @GetMapping("/item-types")
    public ResponseEntity<List<ItemTypes>> getAllItemTypes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ItemTypes");
        Page<ItemTypes> page = itemTypesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /item-types/:id} : get the "id" itemTypes.
     *
     * @param id the id of the itemTypes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the itemTypes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/item-types/{id}")
    public ResponseEntity<ItemTypes> getItemTypes(@PathVariable Long id) {
        log.debug("REST request to get ItemTypes : {}", id);
        Optional<ItemTypes> itemTypes = itemTypesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(itemTypes);
    }

    /**
     * {@code DELETE  /item-types/:id} : delete the "id" itemTypes.
     *
     * @param id the id of the itemTypes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/item-types/{id}")
    public ResponseEntity<Void> deleteItemTypes(@PathVariable Long id) {
        log.debug("REST request to delete ItemTypes : {}", id);
        itemTypesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
