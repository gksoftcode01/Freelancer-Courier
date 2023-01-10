import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ItemTypes from './item-types';
import ItemTypesDetail from './item-types-detail';
import ItemTypesUpdate from './item-types-update';
import ItemTypesDeleteDialog from './item-types-delete-dialog';

const ItemTypesRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ItemTypes />} />
    <Route path="new" element={<ItemTypesUpdate />} />
    <Route path=":id">
      <Route index element={<ItemTypesDetail />} />
      <Route path="edit" element={<ItemTypesUpdate />} />
      <Route path="delete" element={<ItemTypesDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ItemTypesRoutes;
