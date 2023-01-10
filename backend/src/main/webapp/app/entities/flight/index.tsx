import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Flight from './flight';
import FlightDetail from './flight-detail';
import FlightUpdate from './flight-update';
import FlightDeleteDialog from './flight-delete-dialog';

const FlightRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Flight />} />
    <Route path="new" element={<FlightUpdate />} />
    <Route path=":id">
      <Route index element={<FlightDetail />} />
      <Route path="edit" element={<FlightUpdate />} />
      <Route path="delete" element={<FlightDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default FlightRoutes;
