import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './operateur.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OperateurDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const operateurEntity = useAppSelector(state => state.operateur.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="operateurDetailsHeading">
          <Translate contentKey="userAuthApp.operateur.detail.title">Operateur</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{operateurEntity.id}</dd>
          <dt>
            <span id="centreRc">
              <Translate contentKey="userAuthApp.operateur.centreRc">Centre Rc</Translate>
            </span>
          </dt>
          <dd>{operateurEntity.centreRc}</dd>
          <dt>
            <span id="numeroRc">
              <Translate contentKey="userAuthApp.operateur.numeroRc">Numero Rc</Translate>
            </span>
          </dt>
          <dd>{operateurEntity.numeroRc}</dd>
        </dl>
        <Button tag={Link} to="/operateur" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/operateur/${operateurEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OperateurDetail;
