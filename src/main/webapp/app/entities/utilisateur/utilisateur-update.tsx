import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IOperateur } from 'app/shared/model/operateur.model';
import { getEntities as getOperateurs } from 'app/entities/operateur/operateur.reducer';
import { getEntity, updateEntity, createEntity, reset } from './utilisateur.reducer';
import { IUtilisateur } from 'app/shared/model/utilisateur.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UtilisateurUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const operateurs = useAppSelector(state => state.operateur.entities);
  const utilisateurEntity = useAppSelector(state => state.utilisateur.entity);
  const loading = useAppSelector(state => state.utilisateur.loading);
  const updating = useAppSelector(state => state.utilisateur.updating);
  const updateSuccess = useAppSelector(state => state.utilisateur.updateSuccess);
  const handleClose = () => {
    props.history.push('/utilisateur');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getOperateurs({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...utilisateurEntity,
      ...values,
      operateur: operateurs.find(it => it.id.toString() === values.operateur.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...utilisateurEntity,
          operateur: utilisateurEntity?.operateur?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="userAuthApp.utilisateur.home.createOrEditLabel" data-cy="UtilisateurCreateUpdateHeading">
            <Translate contentKey="userAuthApp.utilisateur.home.createOrEditLabel">Create or edit a Utilisateur</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="utilisateur-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('userAuthApp.utilisateur.nomUtilisateur')}
                id="utilisateur-nomUtilisateur"
                name="nomUtilisateur"
                data-cy="nomUtilisateur"
                type="text"
              />
              <ValidatedField
                label={translate('userAuthApp.utilisateur.prenom')}
                id="utilisateur-prenom"
                name="prenom"
                data-cy="prenom"
                type="text"
              />
              <ValidatedField label={translate('userAuthApp.utilisateur.nom')} id="utilisateur-nom" name="nom" data-cy="nom" type="text" />
              <ValidatedField
                label={translate('userAuthApp.utilisateur.dateInscription')}
                id="utilisateur-dateInscription"
                name="dateInscription"
                data-cy="dateInscription"
                type="date"
              />
              <ValidatedField
                label={translate('userAuthApp.utilisateur.password')}
                id="utilisateur-password"
                name="password"
                data-cy="password"
                type="text"
              />
              <ValidatedField
                id="utilisateur-operateur"
                name="operateur"
                data-cy="operateur"
                label={translate('userAuthApp.utilisateur.operateur')}
                type="select"
              >
                <option value="" key="0" />
                {operateurs
                  ? operateurs.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/utilisateur" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default UtilisateurUpdate;
