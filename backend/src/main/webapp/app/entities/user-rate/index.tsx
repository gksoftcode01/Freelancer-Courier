import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import UserRate from './user-rate';
import UserRateDetail from './user-rate-detail';
import UserRateUpdate from './user-rate-update';
import UserRateDeleteDialog from './user-rate-delete-dialog';

const UserRateRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<UserRate />} />
    <Route path="new" element={<UserRateUpdate />} />
    <Route path=":id">
      <Route index element={<UserRateDetail />} />
      <Route path="edit" element={<UserRateUpdate />} />
      <Route path="delete" element={<UserRateDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default UserRateRoutes;
