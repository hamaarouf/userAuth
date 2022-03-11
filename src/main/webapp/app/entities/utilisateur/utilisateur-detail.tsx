import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './utilisateur.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UtilisateurDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const utilisateurEntity = useAppSelector(state => state.utilisateur.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="utilisateurDetailsHeading">
          <Translate contentKey="userAuthApp.utilisateur.detail.title">Utilisateur</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{utilisateurEntity.id}</dd>
          <dt>
            <span id="nomUtilisateur">
              <Translate contentKey="userAuthApp.utilisateur.nomUtilisateur">Nom Utilisateur</Translate>
            </span>
          </dt>
          <dd>{utilisateurEntity.nomUtilisateur}</dd>
          <dt>
            <span id="prenom">
              <Translate contentKey="userAuthApp.utilisateur.prenom">Prenom</Translate>
            </span>
          </dt>
          <dd>{utilisateurEntity.prenom}</dd>
          <dt>
            <span id="nom">
              <Translate contentKey="userAuthApp.utilisateur.nom">Nom</Translate>
            </span>
          </dt>
          <dd>{utilisateurEntity.nom}</dd>
          <dt>
            <span id="dateInscription">
              <Translate contentKey="userAuthApp.utilisateur.dateInscription">Date Inscription</Translate>
            </span>
          </dt>
          <dd>
            {utilisateurEntity.dateInscription ? (
              <TextFormat value={utilisateurEntity.dateInscription} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="password">
              <Translate contentKey="userAuthApp.utilisateur.password">Password</Translate>
            </span>
          </dt>
          <dd>{utilisateurEntity.password}</dd>
          <dt>
            <Translate contentKey="userAuthApp.utilisateur.operateur">Operateur</Translate>
          </dt>
          <dd>{utilisateurEntity.operateur ? utilisateurEntity.operateur.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/utilisateur" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/utilisateur/${utilisateurEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UtilisateurDetail;
