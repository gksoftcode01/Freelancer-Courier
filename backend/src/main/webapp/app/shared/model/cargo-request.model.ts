import dayjs from 'dayjs';
import { ICargoRequestDetails } from 'app/shared/model/cargo-request-details.model';
import { IAsk } from 'app/shared/model/ask.model';
import { IBid } from 'app/shared/model/bid.model';
import { ICargoRequestStatus } from 'app/shared/model/cargo-request-status.model';
import { IAppUser } from 'app/shared/model/app-user.model';
import { ICountry } from 'app/shared/model/country.model';
import { IStateProvince } from 'app/shared/model/state-province.model';
import { ICity } from 'app/shared/model/city.model';
import { IItemTypes } from 'app/shared/model/item-types.model';
import { IUserRate } from 'app/shared/model/user-rate.model';

export interface ICargoRequest {
  id?: number;
  budget?: number | null;
  isToDoor?: boolean | null;
  createDate?: string | null;
  agreedPrice?: number | null;
  cargoRequestDetails?: ICargoRequestDetails[] | null;
  asks?: IAsk[] | null;
  bids?: IBid[] | null;
  status?: ICargoRequestStatus | null;
  createBy?: IAppUser | null;
  takenBy?: IAppUser | null;
  fromCountry?: ICountry | null;
  toCountry?: ICountry | null;
  fromState?: IStateProvince | null;
  toState?: IStateProvince | null;
  fromCity?: ICity | null;
  toCity?: ICity | null;
  reqItemTypes?: IItemTypes[] | null;
  userRate?: IUserRate | null;
}

export const defaultValue: Readonly<ICargoRequest> = {
  isToDoor: false,
};
