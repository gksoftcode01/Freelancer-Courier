import dayjs from 'dayjs';
import { IAppUser } from 'app/shared/model/app-user.model';
import { ICountry } from 'app/shared/model/country.model';
import { IStateProvince } from 'app/shared/model/state-province.model';
import { ICity } from 'app/shared/model/city.model';
import { IItemTypes } from 'app/shared/model/item-types.model';
import { FlightStatus } from 'app/shared/model/enumerations/flight-status.model';

export interface IFlight {
  id?: number;
  flightDate?: string | null;
  maxWeight?: number | null;
  notes?: string | null;
  budget?: number | null;
  createDate?: string | null;
  toDoorAvailable?: boolean | null;
  status?: FlightStatus | null;
  createBy?: IAppUser | null;
  fromCountry?: ICountry | null;
  toCountry?: ICountry | null;
  fromState?: IStateProvince | null;
  toState?: IStateProvince | null;
  fromCity?: ICity | null;
  toCity?: ICity | null;
  availableItemTypes?: IItemTypes[] | null;
}

export const defaultValue: Readonly<IFlight> = {
  toDoorAvailable: false,
};
