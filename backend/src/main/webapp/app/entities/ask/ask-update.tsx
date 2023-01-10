import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IAppUser } from 'app/shared/model/app-user.model';
import { getEntities as getAppUsers } from 'app/entities/app-user/app-user.reducer';
import { ICargoRequest } from 'app/shared/model/cargo-request.model';
import { getEntities as getCargoRequests } from 'app/entities/cargo-request/cargo-request.reducer';
import { IAsk } from 'app/shared/model/ask.model';
import { BidAskStatus } from 'app/shared/model/enumerations/bid-ask-status.model';
import { getEntity, updateEntity, createEntity, reset } from './ask.reducer';

export const AskUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const appUsers = useAppSelector(state => state.appUser.entities);
  const cargoRequests = useAppSelector(state => state.cargoRequest.entities);
  const askEntity = useAppSelector(state => state.ask.entity);
  const loading = useAppSelector(state => state.ask.loading);
  const updating = useAppSelector(state => state.ask.updating);
  const updateSuccess = useAppSelector(state => state.ask.updateSuccess);
  const bidAskStatusValues = Object.keys(BidAskStatus);

  const handleClose = () => {
    navigate('/ask');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getAppUsers({}));
    dispatch(getCargoRequests({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...askEntity,
      ...values,
      toUser: appUsers.find(it => it.id.toString() === values.toUser.toString()),
      cargoRequest: cargoRequests.find(it => it.id.toString() === values.cargoRequest.toString()),
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
          status: 'Approve',
          ...askEntity,
          toUser: askEntity?.toUser?.id,
          cargoRequest: askEntity?.cargoRequest?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="freelancerCourierApp.ask.home.createOrEditLabel" data-cy="AskCreateUpdateHeading">
            <Translate contentKey="freelancerCourierApp.ask.home.createOrEditLabel">Create or edit a Ask</Translate>
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
                  id="ask-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('freelancerCourierApp.ask.notes')} id="ask-notes" name="notes" data-cy="notes" type="text" />
              <ValidatedField label={translate('freelancerCourierApp.ask.price')} id="ask-price" name="price" data-cy="price" type="text" />
              <ValidatedField
                label={translate('freelancerCourierApp.ask.status')}
                id="ask-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {bidAskStatusValues.map(bidAskStatus => (
                  <option value={bidAskStatus} key={bidAskStatus}>
                    {translate('freelancerCourierApp.BidAskStatus.' + bidAskStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="ask-toUser"
                name="toUser"
                data-cy="toUser"
                label={translate('freelancerCourierApp.ask.toUser')}
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
              <ValidatedField
                id="ask-cargoRequest"
                name="cargoRequest"
                data-cy="cargoRequest"
                label={translate('freelancerCourierApp.ask.cargoRequest')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ask" replace color="info">
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

export default AskUpdate;
