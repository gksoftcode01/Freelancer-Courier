import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CargoRequest from './cargo-request';
import CargoRequestDetail from './cargo-request-detail';
import CargoRequestUpdate from './cargo-request-update';
import CargoRequestDeleteDialog from './cargo-request-delete-dialog';

const CargoRequestRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CargoRequest />} />
    <Route path="new" element={<CargoRequestUpdate />} />
    <Route path=":id">
      <Route index element={<CargoRequestDetail />} />
      <Route path="edit" element={<CargoRequestUpdate />} />
      <Route path="delete" element={<CargoRequestDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CargoRequestRoutes;
