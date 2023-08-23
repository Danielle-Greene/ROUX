import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CompletedMeals from './completed-meals';
import CompletedMealsDetail from './completed-meals-detail';
import CompletedMealsUpdate from './completed-meals-update';
import CompletedMealsDeleteDialog from './completed-meals-delete-dialog';

const CompletedMealsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CompletedMeals />} />
    <Route path="new" element={<CompletedMealsUpdate />} />
    <Route path=":id">
      <Route index element={<CompletedMealsDetail />} />
      <Route path="edit" element={<CompletedMealsUpdate />} />
      <Route path="delete" element={<CompletedMealsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CompletedMealsRoutes;
