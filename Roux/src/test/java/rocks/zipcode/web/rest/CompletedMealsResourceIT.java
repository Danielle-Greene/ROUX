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
import rocks.zipcode.domain.CompletedMeals;
import rocks.zipcode.repository.CompletedMealsRepository;

/**
 * Integration tests for the {@link CompletedMealsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompletedMealsResourceIT {

    private static final String DEFAULT_PREP_STEPS = "AAAAAAAAAA";
    private static final String UPDATED_PREP_STEPS = "BBBBBBBBBB";

    private static final String DEFAULT_INGREDIENT_LIST = "AAAAAAAAAA";
    private static final String UPDATED_INGREDIENT_LIST = "BBBBBBBBBB";

    private static final String DEFAULT_COOKING_STEPS = "AAAAAAAAAA";
    private static final String UPDATED_COOKING_STEPS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/completed-meals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CompletedMealsRepository completedMealsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompletedMealsMockMvc;

    private CompletedMeals completedMeals;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompletedMeals createEntity(EntityManager em) {
        CompletedMeals completedMeals = new CompletedMeals()
            .prepSteps(DEFAULT_PREP_STEPS)
            .ingredientList(DEFAULT_INGREDIENT_LIST)
            .cookingSteps(DEFAULT_COOKING_STEPS);
        return completedMeals;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompletedMeals createUpdatedEntity(EntityManager em) {
        CompletedMeals completedMeals = new CompletedMeals()
            .prepSteps(UPDATED_PREP_STEPS)
            .ingredientList(UPDATED_INGREDIENT_LIST)
            .cookingSteps(UPDATED_COOKING_STEPS);
        return completedMeals;
    }

    @BeforeEach
    public void initTest() {
        completedMeals = createEntity(em);
    }

    @Test
    @Transactional
    void createCompletedMeals() throws Exception {
        int databaseSizeBeforeCreate = completedMealsRepository.findAll().size();
        // Create the CompletedMeals
        restCompletedMealsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(completedMeals))
            )
            .andExpect(status().isCreated());

        // Validate the CompletedMeals in the database
        List<CompletedMeals> completedMealsList = completedMealsRepository.findAll();
        assertThat(completedMealsList).hasSize(databaseSizeBeforeCreate + 1);
        CompletedMeals testCompletedMeals = completedMealsList.get(completedMealsList.size() - 1);
        assertThat(testCompletedMeals.getPrepSteps()).isEqualTo(DEFAULT_PREP_STEPS);
        assertThat(testCompletedMeals.getIngredientList()).isEqualTo(DEFAULT_INGREDIENT_LIST);
        assertThat(testCompletedMeals.getCookingSteps()).isEqualTo(DEFAULT_COOKING_STEPS);
    }

    @Test
    @Transactional
    void createCompletedMealsWithExistingId() throws Exception {
        // Create the CompletedMeals with an existing ID
        completedMeals.setId(1L);

        int databaseSizeBeforeCreate = completedMealsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompletedMealsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(completedMeals))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompletedMeals in the database
        List<CompletedMeals> completedMealsList = completedMealsRepository.findAll();
        assertThat(completedMealsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCompletedMeals() throws Exception {
        // Initialize the database
        completedMealsRepository.saveAndFlush(completedMeals);

        // Get all the completedMealsList
        restCompletedMealsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(completedMeals.getId().intValue())))
            .andExpect(jsonPath("$.[*].prepSteps").value(hasItem(DEFAULT_PREP_STEPS)))
            .andExpect(jsonPath("$.[*].ingredientList").value(hasItem(DEFAULT_INGREDIENT_LIST)))
            .andExpect(jsonPath("$.[*].cookingSteps").value(hasItem(DEFAULT_COOKING_STEPS)));
    }

    @Test
    @Transactional
    void getCompletedMeals() throws Exception {
        // Initialize the database
        completedMealsRepository.saveAndFlush(completedMeals);

        // Get the completedMeals
        restCompletedMealsMockMvc
            .perform(get(ENTITY_API_URL_ID, completedMeals.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(completedMeals.getId().intValue()))
            .andExpect(jsonPath("$.prepSteps").value(DEFAULT_PREP_STEPS))
            .andExpect(jsonPath("$.ingredientList").value(DEFAULT_INGREDIENT_LIST))
            .andExpect(jsonPath("$.cookingSteps").value(DEFAULT_COOKING_STEPS));
    }

    @Test
    @Transactional
    void getNonExistingCompletedMeals() throws Exception {
        // Get the completedMeals
        restCompletedMealsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompletedMeals() throws Exception {
        // Initialize the database
        completedMealsRepository.saveAndFlush(completedMeals);

        int databaseSizeBeforeUpdate = completedMealsRepository.findAll().size();

        // Update the completedMeals
        CompletedMeals updatedCompletedMeals = completedMealsRepository.findById(completedMeals.getId()).get();
        // Disconnect from session so that the updates on updatedCompletedMeals are not directly saved in db
        em.detach(updatedCompletedMeals);
        updatedCompletedMeals.prepSteps(UPDATED_PREP_STEPS).ingredientList(UPDATED_INGREDIENT_LIST).cookingSteps(UPDATED_COOKING_STEPS);

        restCompletedMealsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCompletedMeals.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCompletedMeals))
            )
            .andExpect(status().isOk());

        // Validate the CompletedMeals in the database
        List<CompletedMeals> completedMealsList = completedMealsRepository.findAll();
        assertThat(completedMealsList).hasSize(databaseSizeBeforeUpdate);
        CompletedMeals testCompletedMeals = completedMealsList.get(completedMealsList.size() - 1);
        assertThat(testCompletedMeals.getPrepSteps()).isEqualTo(UPDATED_PREP_STEPS);
        assertThat(testCompletedMeals.getIngredientList()).isEqualTo(UPDATED_INGREDIENT_LIST);
        assertThat(testCompletedMeals.getCookingSteps()).isEqualTo(UPDATED_COOKING_STEPS);
    }

    @Test
    @Transactional
    void putNonExistingCompletedMeals() throws Exception {
        int databaseSizeBeforeUpdate = completedMealsRepository.findAll().size();
        completedMeals.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompletedMealsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, completedMeals.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(completedMeals))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompletedMeals in the database
        List<CompletedMeals> completedMealsList = completedMealsRepository.findAll();
        assertThat(completedMealsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompletedMeals() throws Exception {
        int databaseSizeBeforeUpdate = completedMealsRepository.findAll().size();
        completedMeals.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompletedMealsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(completedMeals))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompletedMeals in the database
        List<CompletedMeals> completedMealsList = completedMealsRepository.findAll();
        assertThat(completedMealsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompletedMeals() throws Exception {
        int databaseSizeBeforeUpdate = completedMealsRepository.findAll().size();
        completedMeals.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompletedMealsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(completedMeals)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompletedMeals in the database
        List<CompletedMeals> completedMealsList = completedMealsRepository.findAll();
        assertThat(completedMealsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompletedMealsWithPatch() throws Exception {
        // Initialize the database
        completedMealsRepository.saveAndFlush(completedMeals);

        int databaseSizeBeforeUpdate = completedMealsRepository.findAll().size();

        // Update the completedMeals using partial update
        CompletedMeals partialUpdatedCompletedMeals = new CompletedMeals();
        partialUpdatedCompletedMeals.setId(completedMeals.getId());

        partialUpdatedCompletedMeals.cookingSteps(UPDATED_COOKING_STEPS);

        restCompletedMealsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompletedMeals.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompletedMeals))
            )
            .andExpect(status().isOk());

        // Validate the CompletedMeals in the database
        List<CompletedMeals> completedMealsList = completedMealsRepository.findAll();
        assertThat(completedMealsList).hasSize(databaseSizeBeforeUpdate);
        CompletedMeals testCompletedMeals = completedMealsList.get(completedMealsList.size() - 1);
        assertThat(testCompletedMeals.getPrepSteps()).isEqualTo(DEFAULT_PREP_STEPS);
        assertThat(testCompletedMeals.getIngredientList()).isEqualTo(DEFAULT_INGREDIENT_LIST);
        assertThat(testCompletedMeals.getCookingSteps()).isEqualTo(UPDATED_COOKING_STEPS);
    }

    @Test
    @Transactional
    void fullUpdateCompletedMealsWithPatch() throws Exception {
        // Initialize the database
        completedMealsRepository.saveAndFlush(completedMeals);

        int databaseSizeBeforeUpdate = completedMealsRepository.findAll().size();

        // Update the completedMeals using partial update
        CompletedMeals partialUpdatedCompletedMeals = new CompletedMeals();
        partialUpdatedCompletedMeals.setId(completedMeals.getId());

        partialUpdatedCompletedMeals
            .prepSteps(UPDATED_PREP_STEPS)
            .ingredientList(UPDATED_INGREDIENT_LIST)
            .cookingSteps(UPDATED_COOKING_STEPS);

        restCompletedMealsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompletedMeals.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCompletedMeals))
            )
            .andExpect(status().isOk());

        // Validate the CompletedMeals in the database
        List<CompletedMeals> completedMealsList = completedMealsRepository.findAll();
        assertThat(completedMealsList).hasSize(databaseSizeBeforeUpdate);
        CompletedMeals testCompletedMeals = completedMealsList.get(completedMealsList.size() - 1);
        assertThat(testCompletedMeals.getPrepSteps()).isEqualTo(UPDATED_PREP_STEPS);
        assertThat(testCompletedMeals.getIngredientList()).isEqualTo(UPDATED_INGREDIENT_LIST);
        assertThat(testCompletedMeals.getCookingSteps()).isEqualTo(UPDATED_COOKING_STEPS);
    }

    @Test
    @Transactional
    void patchNonExistingCompletedMeals() throws Exception {
        int databaseSizeBeforeUpdate = completedMealsRepository.findAll().size();
        completedMeals.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompletedMealsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, completedMeals.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(completedMeals))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompletedMeals in the database
        List<CompletedMeals> completedMealsList = completedMealsRepository.findAll();
        assertThat(completedMealsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompletedMeals() throws Exception {
        int databaseSizeBeforeUpdate = completedMealsRepository.findAll().size();
        completedMeals.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompletedMealsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(completedMeals))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompletedMeals in the database
        List<CompletedMeals> completedMealsList = completedMealsRepository.findAll();
        assertThat(completedMealsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompletedMeals() throws Exception {
        int databaseSizeBeforeUpdate = completedMealsRepository.findAll().size();
        completedMeals.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompletedMealsMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(completedMeals))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompletedMeals in the database
        List<CompletedMeals> completedMealsList = completedMealsRepository.findAll();
        assertThat(completedMealsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompletedMeals() throws Exception {
        // Initialize the database
        completedMealsRepository.saveAndFlush(completedMeals);

        int databaseSizeBeforeDelete = completedMealsRepository.findAll().size();

        // Delete the completedMeals
        restCompletedMealsMockMvc
            .perform(delete(ENTITY_API_URL_ID, completedMeals.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CompletedMeals> completedMealsList = completedMealsRepository.findAll();
        assertThat(completedMealsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
