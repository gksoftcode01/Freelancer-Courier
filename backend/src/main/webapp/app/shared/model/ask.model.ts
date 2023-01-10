import { IAppUser } from 'app/shared/model/app-user.model';
import { ICargoRequest } from 'app/shared/model/cargo-request.model';
import { BidAskStatus } from 'app/shared/model/enumerations/bid-ask-status.model';

export interface IAsk {
  id?: number;
  notes?: string | null;
  price?: number | null;
  status?: BidAskStatus | null;
  toUser?: IAppUser | null;
  cargoRequest?: ICargoRequest | null;
}

export const defaultValue: Readonly<IAsk> = {};
