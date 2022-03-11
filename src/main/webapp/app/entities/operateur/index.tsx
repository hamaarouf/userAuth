import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Operateur from './operateur';
import OperateurDetail from './operateur-detail';
import OperateurUpdate from './operateur-update';
import OperateurDeleteDialog from './operateur-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OperateurUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OperateurUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OperateurDetail} />
      <ErrorBoundaryRoute path={match.url} component={Operateur} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OperateurDeleteDialog} />
  </>
);

export default Routes;
