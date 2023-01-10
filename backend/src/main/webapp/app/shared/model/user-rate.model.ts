import dayjs from 'dayjs';
import { ICargoRequest } from 'app/shared/model/cargo-request.model';
import { IAppUser } from 'app/shared/model/app-user.model';

export interface IUserRate {
  id?: number;
  rate?: number | null;
  note?: string | null;
  rateDate?: string | null;
  cargoRequest?: ICargoRequest | null;
  appUser?: IAppUser | null;
}

export const defaultValue: Readonly<IUserRate> = {};
