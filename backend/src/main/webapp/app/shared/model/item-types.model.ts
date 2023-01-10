import { IFlight } from 'app/shared/model/flight.model';
import { ICargoRequest } from 'app/shared/model/cargo-request.model';

export interface IItemTypes {
  id?: number;
  name?: string;
  flights?: IFlight[] | null;
  cargoRequests?: ICargoRequest[] | null;
}

export const defaultValue: Readonly<IItemTypes> = {};
