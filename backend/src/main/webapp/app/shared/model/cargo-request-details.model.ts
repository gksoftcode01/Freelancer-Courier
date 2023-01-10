import { ICargoRequest } from 'app/shared/model/cargo-request.model';

export interface ICargoRequestDetails {
  id?: number;
  itemDesc?: string | null;
  itemWeight?: number | null;
  itemHeight?: number | null;
  itemWidth?: number | null;
  itemPhotoContentType?: string | null;
  itemPhoto?: string | null;
  cargoRequest?: ICargoRequest | null;
}

export const defaultValue: Readonly<ICargoRequestDetails> = {};
