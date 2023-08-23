import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ingredients from './ingredients';
import Recipes from './recipes';
import CompletedMeals from './completed-meals';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="ingredients/*" element={<Ingredients />} />
        <Route path="recipes/*" element={<Recipes />} />
        <Route path="completed-meals/*" element={<CompletedMeals />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
