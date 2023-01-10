import { IAppUser } from 'app/shared/model/app-user.model';

export interface ICountry {
  id?: number;
  name?: string | null;
  appUsers?: IAppUser[] | null;
}

export const defaultValue: Readonly<ICountry> = {};
