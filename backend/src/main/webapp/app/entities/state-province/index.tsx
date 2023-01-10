import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StateProvince from './state-province';
import StateProvinceDetail from './state-province-detail';
import StateProvinceUpdate from './state-province-update';
import StateProvinceDeleteDialog from './state-province-delete-dialog';

const StateProvinceRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<StateProvince />} />
    <Route path="new" element={<StateProvinceUpdate />} />
    <Route path=":id">
      <Route index element={<StateProvinceDetail />} />
      <Route path="edit" element={<StateProvinceUpdate />} />
      <Route path="delete" element={<StateProvinceDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default StateProvinceRoutes;
