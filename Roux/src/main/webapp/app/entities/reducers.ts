import ingredients from 'app/entities/ingredients/ingredients.reducer';
import recipes from 'app/entities/recipes/recipes.reducer';
import completedMeals from 'app/entities/completed-meals/completed-meals.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  ingredients,
  recipes,
  completedMeals,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
