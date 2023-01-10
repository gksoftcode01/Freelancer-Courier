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
import { IBid } from 'app/shared/model/bid.model';
import { BidAskStatus } from 'app/shared/model/enumerations/bid-ask-status.model';
import { getEntity, updateEntity, createEntity, reset } from './bid.reducer';

export const BidUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const appUsers = useAppSelector(state => state.appUser.entities);
  const cargoRequests = useAppSelector(state => state.cargoRequest.entities);
  const bidEntity = useAppSelector(state => state.bid.entity);
  const loading = useAppSelector(state => state.bid.loading);
  const updating = useAppSelector(state => state.bid.updating);
  const updateSuccess = useAppSelector(state => state.bid.updateSuccess);
  const bidAskStatusValues = Object.keys(BidAskStatus);

  const handleClose = () => {
    navigate('/bid');
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
      ...bidEntity,
      ...values,
      fromUser: appUsers.find(it => it.id.toString() === values.fromUser.toString()),
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
          ...bidEntity,
          fromUser: bidEntity?.fromUser?.id,
          cargoRequest: bidEntity?.cargoRequest?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="freelancerCourierApp.bid.home.createOrEditLabel" data-cy="BidCreateUpdateHeading">
            <Translate contentKey="freelancerCourierApp.bid.home.createOrEditLabel">Create or edit a Bid</Translate>
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
                  id="bid-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField label={translate('freelancerCourierApp.bid.notes')} id="bid-notes" name="notes" data-cy="notes" type="text" />
              <ValidatedField label={translate('freelancerCourierApp.bid.price')} id="bid-price" name="price" data-cy="price" type="text" />
              <ValidatedField
                label={translate('freelancerCourierApp.bid.status')}
                id="bid-status"
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
                id="bid-fromUser"
                name="fromUser"
                data-cy="fromUser"
                label={translate('freelancerCourierApp.bid.fromUser')}
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
                id="bid-cargoRequest"
                name="cargoRequest"
                data-cy="cargoRequest"
                label={translate('freelancerCourierApp.bid.cargoRequest')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/bid" replace color="info">
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

export default BidUpdate;
