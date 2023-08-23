import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ingredients from './ingredients';
import IngredientsDetail from './ingredients-detail';
import IngredientsUpdate from './ingredients-update';
import IngredientsDeleteDialog from './ingredients-delete-dialog';

const IngredientsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Ingredients />} />
    <Route path="new" element={<IngredientsUpdate />} />
    <Route path=":id">
      <Route index element={<IngredientsDetail />} />
      <Route path="edit" element={<IngredientsUpdate />} />
      <Route path="delete" element={<IngredientsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default IngredientsRoutes;
