package rocks.zipcode.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import rocks.zipcode.IntegrationTest;
import rocks.zipcode.domain.Ingredients;
import rocks.zipcode.repository.IngredientsRepository;

/**
 * Integration tests for the {@link IngredientsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IngredientsResourceIT {

    private static final String DEFAULT_INGREDIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_INGREDIENT_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ingredients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IngredientsRepository ingredientsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIngredientsMockMvc;

    private Ingredients ingredients;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingredients createEntity(EntityManager em) {
        Ingredients ingredients = new Ingredients().ingredientName(DEFAULT_INGREDIENT_NAME);
        return ingredients;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Ingredients createUpdatedEntity(EntityManager em) {
        Ingredients ingredients = new Ingredients().ingredientName(UPDATED_INGREDIENT_NAME);
        return ingredients;
    }

    @BeforeEach
    public void initTest() {
        ingredients = createEntity(em);
    }

    @Test
    @Transactional
    void createIngredients() throws Exception {
        int databaseSizeBeforeCreate = ingredientsRepository.findAll().size();
        // Create the Ingredients
        restIngredientsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredients)))
            .andExpect(status().isCreated());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeCreate + 1);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getIngredientName()).isEqualTo(DEFAULT_INGREDIENT_NAME);
    }

    @Test
    @Transactional
    void createIngredientsWithExistingId() throws Exception {
        // Create the Ingredients with an existing ID
        ingredients.setId(1L);

        int databaseSizeBeforeCreate = ingredientsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIngredientsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredients)))
            .andExpect(status().isBadRequest());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIngredients() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get all the ingredientsList
        restIngredientsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ingredients.getId().intValue())))
            .andExpect(jsonPath("$.[*].ingredientName").value(hasItem(DEFAULT_INGREDIENT_NAME)));
    }

    @Test
    @Transactional
    void getIngredients() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        // Get the ingredients
        restIngredientsMockMvc
            .perform(get(ENTITY_API_URL_ID, ingredients.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ingredients.getId().intValue()))
            .andExpect(jsonPath("$.ingredientName").value(DEFAULT_INGREDIENT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingIngredients() throws Exception {
        // Get the ingredients
        restIngredientsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingIngredients() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();

        // Update the ingredients
        Ingredients updatedIngredients = ingredientsRepository.findById(ingredients.getId()).get();
        // Disconnect from session so that the updates on updatedIngredients are not directly saved in db
        em.detach(updatedIngredients);
        updatedIngredients.ingredientName(UPDATED_INGREDIENT_NAME);

        restIngredientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIngredients.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIngredients))
            )
            .andExpect(status().isOk());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getIngredientName()).isEqualTo(UPDATED_INGREDIENT_NAME);
    }

    @Test
    @Transactional
    void putNonExistingIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ingredients.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingredients))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(ingredients))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(ingredients)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIngredientsWithPatch() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();

        // Update the ingredients using partial update
        Ingredients partialUpdatedIngredients = new Ingredients();
        partialUpdatedIngredients.setId(ingredients.getId());

        restIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngredients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngredients))
            )
            .andExpect(status().isOk());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getIngredientName()).isEqualTo(DEFAULT_INGREDIENT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateIngredientsWithPatch() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();

        // Update the ingredients using partial update
        Ingredients partialUpdatedIngredients = new Ingredients();
        partialUpdatedIngredients.setId(ingredients.getId());

        partialUpdatedIngredients.ingredientName(UPDATED_INGREDIENT_NAME);

        restIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIngredients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIngredients))
            )
            .andExpect(status().isOk());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
        Ingredients testIngredients = ingredientsList.get(ingredientsList.size() - 1);
        assertThat(testIngredients.getIngredientName()).isEqualTo(UPDATED_INGREDIENT_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ingredients.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingredients))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(ingredients))
            )
            .andExpect(status().isBadRequest());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIngredients() throws Exception {
        int databaseSizeBeforeUpdate = ingredientsRepository.findAll().size();
        ingredients.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIngredientsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(ingredients))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Ingredients in the database
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIngredients() throws Exception {
        // Initialize the database
        ingredientsRepository.saveAndFlush(ingredients);

        int databaseSizeBeforeDelete = ingredientsRepository.findAll().size();

        // Delete the ingredients
        restIngredientsMockMvc
            .perform(delete(ENTITY_API_URL_ID, ingredients.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Ingredients> ingredientsList = ingredientsRepository.findAll();
        assertThat(ingredientsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
