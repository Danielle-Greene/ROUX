import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Recipes from './recipes';
import RecipesDetail from './recipes-detail';
import RecipesUpdate from './recipes-update';
import RecipesDeleteDialog from './recipes-delete-dialog';

const RecipesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Recipes />} />
    <Route path="new" element={<RecipesUpdate />} />
    <Route path=":id">
      <Route index element={<RecipesDetail />} />
      <Route path="edit" element={<RecipesUpdate />} />
      <Route path="delete" element={<RecipesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default RecipesRoutes;
