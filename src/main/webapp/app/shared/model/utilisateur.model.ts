import dayjs from 'dayjs';
import { IOperateur } from 'app/shared/model/operateur.model';

export interface IUtilisateur {
  id?: number;
  nomUtilisateur?: string | null;
  prenom?: string | null;
  nom?: string | null;
  dateInscription?: string | null;
  password?: string | null;
  operateur?: IOperateur | null;
}

export const defaultValue: Readonly<IUtilisateur> = {};
