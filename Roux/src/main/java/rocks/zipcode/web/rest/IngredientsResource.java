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
import rocks.zipcode.domain.Ingredients;
import rocks.zipcode.repository.IngredientsRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.Ingredients}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IngredientsResource {

    private final Logger log = LoggerFactory.getLogger(IngredientsResource.class);

    private static final String ENTITY_NAME = "ingredients";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IngredientsRepository ingredientsRepository;

    public IngredientsResource(IngredientsRepository ingredientsRepository) {
        this.ingredientsRepository = ingredientsRepository;
    }

    /**
     * {@code POST  /ingredients} : Create a new ingredients.
     *
     * @param ingredients the ingredients to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ingredients, or with status {@code 400 (Bad Request)} if the ingredients has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ingredients")
    public ResponseEntity<Ingredients> createIngredients(@RequestBody Ingredients ingredients) throws URISyntaxException {
        log.debug("REST request to save Ingredients : {}", ingredients);
        if (ingredients.getId() != null) {
            throw new BadRequestAlertException("A new ingredients cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ingredients result = ingredientsRepository.save(ingredients);
        return ResponseEntity
            .created(new URI("/api/ingredients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ingredients/:id} : Updates an existing ingredients.
     *
     * @param id the id of the ingredients to save.
     * @param ingredients the ingredients to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingredients,
     * or with status {@code 400 (Bad Request)} if the ingredients is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ingredients couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ingredients/{id}")
    public ResponseEntity<Ingredients> updateIngredients(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ingredients ingredients
    ) throws URISyntaxException {
        log.debug("REST request to update Ingredients : {}, {}", id, ingredients);
        if (ingredients.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingredients.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingredientsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ingredients result = ingredientsRepository.save(ingredients);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ingredients.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ingredients/:id} : Partial updates given fields of an existing ingredients, field will ignore if it is null
     *
     * @param id the id of the ingredients to save.
     * @param ingredients the ingredients to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingredients,
     * or with status {@code 400 (Bad Request)} if the ingredients is not valid,
     * or with status {@code 404 (Not Found)} if the ingredients is not found,
     * or with status {@code 500 (Internal Server Error)} if the ingredients couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ingredients/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ingredients> partialUpdateIngredients(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ingredients ingredients
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ingredients partially : {}, {}", id, ingredients);
        if (ingredients.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingredients.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingredientsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ingredients> result = ingredientsRepository
            .findById(ingredients.getId())
            .map(existingIngredients -> {
                if (ingredients.getIngredientName() != null) {
                    existingIngredients.setIngredientName(ingredients.getIngredientName());
                }

                return existingIngredients;
            })
            .map(ingredientsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ingredients.getId().toString())
        );
    }

    /**
     * {@code GET  /ingredients} : get all the ingredients.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ingredients in body.
     */
    @GetMapping("/ingredients")
    public List<Ingredients> getAllIngredients() {
        log.debug("REST request to get all Ingredients");
        return ingredientsRepository.findAll();
    }

    /**
     * {@code GET  /ingredients/:id} : get the "id" ingredients.
     *
     * @param id the id of the ingredients to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ingredients, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Ingredients> getIngredients(@PathVariable Long id) {
        log.debug("REST request to get Ingredients : {}", id);
        Optional<Ingredients> ingredients = ingredientsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(ingredients);
    }

    /**
     * {@code DELETE  /ingredients/:id} : delete the "id" ingredients.
     *
     * @param id the id of the ingredients to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<Void> deleteIngredients(@PathVariable Long id) {
        log.debug("REST request to delete Ingredients : {}", id);
        ingredientsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
