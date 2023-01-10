import { IAppUser } from 'app/shared/model/app-user.model';

export interface IStateProvince {
  id?: number;
  name?: string | null;
  appUsers?: IAppUser[] | null;
}

export const defaultValue: Readonly<IStateProvince> = {};
