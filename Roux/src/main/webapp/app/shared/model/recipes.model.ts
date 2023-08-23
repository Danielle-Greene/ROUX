import { ICompletedMeals } from 'app/shared/model/completed-meals.model';

export interface IRecipes {
  id?: number;
  recipeName?: string | null;
  recipes?: ICompletedMeals | null;
}

export const defaultValue: Readonly<IRecipes> = {};
