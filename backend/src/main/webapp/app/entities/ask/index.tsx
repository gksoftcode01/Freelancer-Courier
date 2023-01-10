import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Ask from './ask';
import AskDetail from './ask-detail';
import AskUpdate from './ask-update';
import AskDeleteDialog from './ask-delete-dialog';

const AskRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Ask />} />
    <Route path="new" element={<AskUpdate />} />
    <Route path=":id">
      <Route index element={<AskDetail />} />
      <Route path="edit" element={<AskUpdate />} />
      <Route path="delete" element={<AskDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default AskRoutes;
