import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import Bid from './bid';
import BidDetail from './bid-detail';
import BidUpdate from './bid-update';
import BidDeleteDialog from './bid-delete-dialog';

const BidRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<Bid />} />
    <Route path="new" element={<BidUpdate />} />
    <Route path=":id">
      <Route index element={<BidDetail />} />
      <Route path="edit" element={<BidUpdate />} />
      <Route path="delete" element={<BidDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default BidRoutes;
