import dayjs from 'dayjs';
import { IUser } from 'app/shared/model/user.model';
import { IUserRate } from 'app/shared/model/user-rate.model';
import { ICountry } from 'app/shared/model/country.model';
import { IStateProvince } from 'app/shared/model/state-province.model';
import { ICity } from 'app/shared/model/city.model';
import { GenderType } from 'app/shared/model/enumerations/gender-type.model';

export interface IAppUser {
  id?: number;
  birthDate?: string | null;
  gender?: GenderType | null;
  registerDate?: string | null;
  phoneNumber?: string | null;
  mobileNumber?: string | null;
  user?: IUser | null;
  userRates?: IUserRate[] | null;
  country?: ICountry | null;
  stateProvince?: IStateProvince | null;
  city?: ICity | null;
}

export const defaultValue: Readonly<IAppUser> = {};
