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
import rocks.zipcode.domain.Recipes;
import rocks.zipcode.repository.RecipesRepository;
import rocks.zipcode.web.rest.errors.BadRequestAlertException;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link rocks.zipcode.domain.Recipes}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RecipesResource {

    private final Logger log = LoggerFactory.getLogger(RecipesResource.class);

    private static final String ENTITY_NAME = "recipes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RecipesRepository recipesRepository;

    public RecipesResource(RecipesRepository recipesRepository) {
        this.recipesRepository = recipesRepository;
    }

    /**
     * {@code POST  /recipes} : Create a new recipes.
     *
     * @param recipes the recipes to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recipes, or with status {@code 400 (Bad Request)} if the recipes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/recipes")
    public ResponseEntity<Recipes> createRecipes(@RequestBody Recipes recipes) throws URISyntaxException {
        log.debug("REST request to save Recipes : {}", recipes);
        if (recipes.getId() != null) {
            throw new BadRequestAlertException("A new recipes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Recipes result = recipesRepository.save(recipes);
        return ResponseEntity
            .created(new URI("/api/recipes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /recipes/:id} : Updates an existing recipes.
     *
     * @param id the id of the recipes to save.
     * @param recipes the recipes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipes,
     * or with status {@code 400 (Bad Request)} if the recipes is not valid,
     * or with status {@code 500 (Internal Server Error)} if the recipes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/recipes/{id}")
    public ResponseEntity<Recipes> updateRecipes(@PathVariable(value = "id", required = false) final Long id, @RequestBody Recipes recipes)
        throws URISyntaxException {
        log.debug("REST request to update Recipes : {}, {}", id, recipes);
        if (recipes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Recipes result = recipesRepository.save(recipes);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recipes.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /recipes/:id} : Partial updates given fields of an existing recipes, field will ignore if it is null
     *
     * @param id the id of the recipes to save.
     * @param recipes the recipes to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipes,
     * or with status {@code 400 (Bad Request)} if the recipes is not valid,
     * or with status {@code 404 (Not Found)} if the recipes is not found,
     * or with status {@code 500 (Internal Server Error)} if the recipes couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/recipes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Recipes> partialUpdateRecipes(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Recipes recipes
    ) throws URISyntaxException {
        log.debug("REST request to partial update Recipes partially : {}, {}", id, recipes);
        if (recipes.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, recipes.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!recipesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Recipes> result = recipesRepository
            .findById(recipes.getId())
            .map(existingRecipes -> {
                if (recipes.getRecipeName() != null) {
                    existingRecipes.setRecipeName(recipes.getRecipeName());
                }

                return existingRecipes;
            })
            .map(recipesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, recipes.getId().toString())
        );
    }

    /**
     * {@code GET  /recipes} : get all the recipes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recipes in body.
     */
    @GetMapping("/recipes")
    public List<Recipes> getAllRecipes() {
        log.debug("REST request to get all Recipes");
        return recipesRepository.findAll();
    }

    /**
     * {@code GET  /recipes/:id} : get the "id" recipes.
     *
     * @param id the id of the recipes to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recipes, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/recipes/{id}")
    public ResponseEntity<Recipes> getRecipes(@PathVariable Long id) {
        log.debug("REST request to get Recipes : {}", id);
        Optional<Recipes> recipes = recipesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(recipes);
    }

    /**
     * {@code DELETE  /recipes/:id} : delete the "id" recipes.
     *
     * @param id the id of the recipes to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/recipes/{id}")
    public ResponseEntity<Void> deleteRecipes(@PathVariable Long id) {
        log.debug("REST request to delete Recipes : {}", id);
        recipesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
