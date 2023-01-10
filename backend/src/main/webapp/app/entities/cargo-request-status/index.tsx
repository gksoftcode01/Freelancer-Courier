import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import CargoRequestStatus from './cargo-request-status';
import CargoRequestStatusDetail from './cargo-request-status-detail';
import CargoRequestStatusUpdate from './cargo-request-status-update';
import CargoRequestStatusDeleteDialog from './cargo-request-status-delete-dialog';

const CargoRequestStatusRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<CargoRequestStatus />} />
    <Route path="new" element={<CargoRequestStatusUpdate />} />
    <Route path=":id">
      <Route index element={<CargoRequestStatusDetail />} />
      <Route path="edit" element={<CargoRequestStatusUpdate />} />
      <Route path="delete" element={<CargoRequestStatusDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default CargoRequestStatusRoutes;
