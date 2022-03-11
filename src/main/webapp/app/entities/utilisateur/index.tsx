import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Utilisateur from './utilisateur';
import UtilisateurDetail from './utilisateur-detail';
import UtilisateurUpdate from './utilisateur-update';
import UtilisateurDeleteDialog from './utilisateur-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UtilisateurUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UtilisateurUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UtilisateurDetail} />
      <ErrorBoundaryRoute path={match.url} component={Utilisateur} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UtilisateurDeleteDialog} />
  </>
);

export default Routes;
