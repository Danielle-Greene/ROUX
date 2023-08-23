import { IRecipes } from 'app/shared/model/recipes.model';

export interface IIngredients {
  id?: number;
  ingredientName?: string | null;
  ingredients?: IRecipes | null;
}

export const defaultValue: Readonly<IIngredients> = {};
