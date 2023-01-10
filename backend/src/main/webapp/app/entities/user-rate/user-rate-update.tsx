import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICargoRequest } from 'app/shared/model/cargo-request.model';
import { getEntities as getCargoRequests } from 'app/entities/cargo-request/cargo-request.reducer';
import { IAppUser } from 'app/shared/model/app-user.model';
import { getEntities as getAppUsers } from 'app/entities/app-user/app-user.reducer';
import { IUserRate } from 'app/shared/model/user-rate.model';
import { getEntity, updateEntity, createEntity, reset } from './user-rate.reducer';

export const UserRateUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cargoRequests = useAppSelector(state => state.cargoRequest.entities);
  const appUsers = useAppSelector(state => state.appUser.entities);
  const userRateEntity = useAppSelector(state => state.userRate.entity);
  const loading = useAppSelector(state => state.userRate.loading);
  const updating = useAppSelector(state => state.userRate.updating);
  const updateSuccess = useAppSelector(state => state.userRate.updateSuccess);

  const handleClose = () => {
    navigate('/user-rate');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getCargoRequests({}));
    dispatch(getAppUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.rateDate = convertDateTimeToServer(values.rateDate);

    const entity = {
      ...userRateEntity,
      ...values,
      cargoRequest: cargoRequests.find(it => it.id.toString() === values.cargoRequest.toString()),
      appUser: appUsers.find(it => it.id.toString() === values.appUser.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          rateDate: displayDefaultDateTime(),
        }
      : {
          ...userRateEntity,
          rateDate: convertDateTimeFromServer(userRateEntity.rateDate),
          cargoRequest: userRateEntity?.cargoRequest?.id,
          appUser: userRateEntity?.appUser?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="freelancerCourierApp.userRate.home.createOrEditLabel" data-cy="UserRateCreateUpdateHeading">
            <Translate contentKey="freelancerCourierApp.userRate.home.createOrEditLabel">Create or edit a UserRate</Translate>
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
                  id="user-rate-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('freelancerCourierApp.userRate.rate')}
                id="user-rate-rate"
                name="rate"
                data-cy="rate"
                type="text"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.userRate.note')}
                id="user-rate-note"
                name="note"
                data-cy="note"
                type="text"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.userRate.rateDate')}
                id="user-rate-rateDate"
                name="rateDate"
                data-cy="rateDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="user-rate-cargoRequest"
                name="cargoRequest"
                data-cy="cargoRequest"
                label={translate('freelancerCourierApp.userRate.cargoRequest')}
                type="select"
              >
                <option value="" key="0" />
                {cargoRequests
                  ? cargoRequests.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="user-rate-appUser"
                name="appUser"
                data-cy="appUser"
                label={translate('freelancerCourierApp.userRate.appUser')}
                type="select"
              >
                <option value="" key="0" />
                {appUsers
                  ? appUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/user-rate" replace color="info">
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

export default UserRateUpdate;
