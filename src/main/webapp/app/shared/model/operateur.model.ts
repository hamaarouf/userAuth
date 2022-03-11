import { IUtilisateur } from 'app/shared/model/utilisateur.model';

export interface IOperateur {
  id?: number;
  centreRc?: string | null;
  numeroRc?: string | null;
  utilisateurs?: IUtilisateur[] | null;
}

export const defaultValue: Readonly<IOperateur> = {};
