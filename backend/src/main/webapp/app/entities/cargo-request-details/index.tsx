import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CargoRequestDetails from './cargo-request-details';
import CargoRequestDetailsDetail from './cargo-request-details-detail';
import CargoRequestDetailsUpdate from './cargo-request-details-update';
import CargoRequestDetailsDeleteDialog from './cargo-request-details-delete-dialog';

const CargoRequestDetailsRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CargoRequestDetails />} />
    <Route path="new" element={<CargoRequestDetailsUpdate />} />
    <Route path=":id">
      <Route index element={<CargoRequestDetailsDetail />} />
      <Route path="edit" element={<CargoRequestDetailsUpdate />} />
      <Route path="delete" element={<CargoRequestDetailsDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CargoRequestDetailsRoutes;
