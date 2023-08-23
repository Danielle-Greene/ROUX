package rocks.zipcode.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import rocks.zipcode.domain.CompletedMeals;
import rocks.zipcode.repository.CompletedMealsRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.CompletedMeals}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CompletedMealsResource {

    private final Logger log = LoggerFactory.getLogger(CompletedMealsResource.class);

    private static final String ENTITY_NAME = "completedMeals";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CompletedMealsRepository completedMealsRepository;

    public CompletedMealsResource(CompletedMealsRepository completedMealsRepository) {
        this.completedMealsRepository = completedMealsRepository;
    }

    /**
     * {@code POST  /completed-meals} : Create a new completedMeals.
     *
     * @param completedMeals the completedMeals to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new completedMeals, or with status {@code 400 (Bad Request)} if the completedMeals has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/completed-meals")
    public ResponseEntity<CompletedMeals> createCompletedMeals(@RequestBody CompletedMeals completedMeals) throws URISyntaxException {
        log.debug("REST request to save CompletedMeals : {}", completedMeals);
        if (completedMeals.getId() != null) {
            throw new BadRequestAlertException("A new completedMeals cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CompletedMeals result = completedMealsRepository.save(completedMeals);
        return ResponseEntity
            .created(new URI("/api/completed-meals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /completed-meals/:id} : Updates an existing completedMeals.
     *
     * @param id the id of the completedMeals to save.
     * @param completedMeals the completedMeals to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated completedMeals,
     * or with status {@code 400 (Bad Request)} if the completedMeals is not valid,
     * or with status {@code 500 (Internal Server Error)} if the completedMeals couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/completed-meals/{id}")
    public ResponseEntity<CompletedMeals> updateCompletedMeals(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompletedMeals completedMeals
    ) throws URISyntaxException {
        log.debug("REST request to update CompletedMeals : {}, {}", id, completedMeals);
        if (completedMeals.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, completedMeals.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!completedMealsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CompletedMeals result = completedMealsRepository.save(completedMeals);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, completedMeals.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /completed-meals/:id} : Partial updates given fields of an existing completedMeals, field will ignore if it is null
     *
     * @param id the id of the completedMeals to save.
     * @param completedMeals the completedMeals to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated completedMeals,
     * or with status {@code 400 (Bad Request)} if the completedMeals is not valid,
     * or with status {@code 404 (Not Found)} if the completedMeals is not found,
     * or with status {@code 500 (Internal Server Error)} if the completedMeals couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/completed-meals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CompletedMeals> partialUpdateCompletedMeals(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody CompletedMeals completedMeals
    ) throws URISyntaxException {
        log.debug("REST request to partial update CompletedMeals partially : {}, {}", id, completedMeals);
        if (completedMeals.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, completedMeals.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!completedMealsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CompletedMeals> result = completedMealsRepository
            .findById(completedMeals.getId())
            .map(existingCompletedMeals -> {
                if (completedMeals.getPrepSteps() != null) {
                    existingCompletedMeals.setPrepSteps(completedMeals.getPrepSteps());
                }
                if (completedMeals.getIngredientList() != null) {
                    existingCompletedMeals.setIngredientList(completedMeals.getIngredientList());
                }
                if (completedMeals.getCookingSteps() != null) {
                    existingCompletedMeals.setCookingSteps(completedMeals.getCookingSteps());
                }

                return existingCompletedMeals;
            })
            .map(completedMealsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, completedMeals.getId().toString())
        );
    }

    /**
     * {@code GET  /completed-meals} : get all the completedMeals.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of completedMeals in body.
     */
    @GetMapping("/completed-meals")
    public List<CompletedMeals> getAllCompletedMeals() {
        log.debug("REST request to get all CompletedMeals");
        return completedMealsRepository.findAll();
    }

    /**
     * {@code GET  /completed-meals/:id} : get the "id" completedMeals.
     *
     * @param id the id of the completedMeals to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the completedMeals, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/completed-meals/{id}")
    public ResponseEntity<CompletedMeals> getCompletedMeals(@PathVariable Long id) {
        log.debug("REST request to get CompletedMeals : {}", id);
        Optional<CompletedMeals> completedMeals = completedMealsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(completedMeals);
    }

    /**
     * {@code DELETE  /completed-meals/:id} : delete the "id" completedMeals.
     *
     * @param id the id of the completedMeals to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/completed-meals/{id}")
    public ResponseEntity<Void> deleteCompletedMeals(@PathVariable Long id) {
        log.debug("REST request to delete CompletedMeals : {}", id);
        completedMealsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
