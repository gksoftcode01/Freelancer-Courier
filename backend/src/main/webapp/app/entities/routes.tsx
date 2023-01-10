import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import StateProvince from './state-province';
import Country from './country';
import City from './city';
import UserRate from './user-rate';
import ItemTypes from './item-types';
import AppUser from './app-user';
import Flight from './flight';
import CargoRequestStatus from './cargo-request-status';
import CargoRequest from './cargo-request';
import CargoRequestDetails from './cargo-request-details';
import Bid from './bid';
import Ask from './ask';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="state-province/*" element={<StateProvince />} />
        <Route path="country/*" element={<Country />} />
        <Route path="city/*" element={<City />} />
        <Route path="user-rate/*" element={<UserRate />} />
        <Route path="item-types/*" element={<ItemTypes />} />
        <Route path="app-user/*" element={<AppUser />} />
        <Route path="flight/*" element={<Flight />} />
        <Route path="cargo-request-status/*" element={<CargoRequestStatus />} />
        <Route path="cargo-request/*" element={<CargoRequest />} />
        <Route path="cargo-request-details/*" element={<CargoRequestDetails />} />
        <Route path="bid/*" element={<Bid />} />
        <Route path="ask/*" element={<Ask />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
