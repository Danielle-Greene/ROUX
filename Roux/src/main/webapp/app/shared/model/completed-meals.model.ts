export interface ICompletedMeals {
  id?: number;
  prepSteps?: string | null;
  ingredientList?: string | null;
  cookingSteps?: string | null;
}

export const defaultValue: Readonly<ICompletedMeals> = {};
