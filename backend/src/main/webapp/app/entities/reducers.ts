import stateProvince from 'app/entities/state-province/state-province.reducer';
import country from 'app/entities/country/country.reducer';
import city from 'app/entities/city/city.reducer';
import userRate from 'app/entities/user-rate/user-rate.reducer';
import itemTypes from 'app/entities/item-types/item-types.reducer';
import appUser from 'app/entities/app-user/app-user.reducer';
import flight from 'app/entities/flight/flight.reducer';
import cargoRequestStatus from 'app/entities/cargo-request-status/cargo-request-status.reducer';
import cargoRequest from 'app/entities/cargo-request/cargo-request.reducer';
import cargoRequestDetails from 'app/entities/cargo-request-details/cargo-request-details.reducer';
import bid from 'app/entities/bid/bid.reducer';
import ask from 'app/entities/ask/ask.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  stateProvince,
  country,
  city,
  userRate,
  itemTypes,
  appUser,
  flight,
  cargoRequestStatus,
  cargoRequest,
  cargoRequestDetails,
  bid,
  ask,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
