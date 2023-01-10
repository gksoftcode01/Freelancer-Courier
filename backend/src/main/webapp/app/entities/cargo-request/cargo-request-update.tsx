import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICargoRequestStatus } from 'app/shared/model/cargo-request-status.model';
import { getEntities as getCargoRequestStatuses } from 'app/entities/cargo-request-status/cargo-request-status.reducer';
import { IAppUser } from 'app/shared/model/app-user.model';
import { getEntities as getAppUsers } from 'app/entities/app-user/app-user.reducer';
import { ICountry } from 'app/shared/model/country.model';
import { getEntities as getCountries } from 'app/entities/country/country.reducer';
import { IStateProvince } from 'app/shared/model/state-province.model';
import { getEntities as getStateProvinces } from 'app/entities/state-province/state-province.reducer';
import { ICity } from 'app/shared/model/city.model';
import { getEntities as getCities } from 'app/entities/city/city.reducer';
import { IItemTypes } from 'app/shared/model/item-types.model';
import { getEntities as getItemTypes } from 'app/entities/item-types/item-types.reducer';
import { IUserRate } from 'app/shared/model/user-rate.model';
import { getEntities as getUserRates } from 'app/entities/user-rate/user-rate.reducer';
import { ICargoRequest } from 'app/shared/model/cargo-request.model';
import { getEntity, updateEntity, createEntity, reset } from './cargo-request.reducer';

export const CargoRequestUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cargoRequestStatuses = useAppSelector(state => state.cargoRequestStatus.entities);
  const appUsers = useAppSelector(state => state.appUser.entities);
  const countries = useAppSelector(state => state.country.entities);
  const stateProvinces = useAppSelector(state => state.stateProvince.entities);
  const cities = useAppSelector(state => state.city.entities);
  const itemTypes = useAppSelector(state => state.itemTypes.entities);
  const userRates = useAppSelector(state => state.userRate.entities);
  const cargoRequestEntity = useAppSelector(state => state.cargoRequest.entity);
  const loading = useAppSelector(state => state.cargoRequest.loading);
  const updating = useAppSelector(state => state.cargoRequest.updating);
  const updateSuccess = useAppSelector(state => state.cargoRequest.updateSuccess);

  const handleClose = () => {
    navigate('/cargo-request');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getCargoRequestStatuses({}));
    dispatch(getAppUsers({}));
    dispatch(getCountries({}));
    dispatch(getStateProvinces({}));
    dispatch(getCities({}));
    dispatch(getItemTypes({}));
    dispatch(getUserRates({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.createDate = convertDateTimeToServer(values.createDate);

    const entity = {
      ...cargoRequestEntity,
      ...values,
      reqItemTypes: mapIdList(values.reqItemTypes),
      status: cargoRequestStatuses.find(it => it.id.toString() === values.status.toString()),
      createBy: appUsers.find(it => it.id.toString() === values.createBy.toString()),
      takenBy: appUsers.find(it => it.id.toString() === values.takenBy.toString()),
      fromCountry: countries.find(it => it.id.toString() === values.fromCountry.toString()),
      toCountry: countries.find(it => it.id.toString() === values.toCountry.toString()),
      fromState: stateProvinces.find(it => it.id.toString() === values.fromState.toString()),
      toState: stateProvinces.find(it => it.id.toString() === values.toState.toString()),
      fromCity: cities.find(it => it.id.toString() === values.fromCity.toString()),
      toCity: cities.find(it => it.id.toString() === values.toCity.toString()),
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
          createDate: displayDefaultDateTime(),
        }
      : {
          ...cargoRequestEntity,
          createDate: convertDateTimeFromServer(cargoRequestEntity.createDate),
          status: cargoRequestEntity?.status?.id,
          createBy: cargoRequestEntity?.createBy?.id,
          takenBy: cargoRequestEntity?.takenBy?.id,
          fromCountry: cargoRequestEntity?.fromCountry?.id,
          toCountry: cargoRequestEntity?.toCountry?.id,
          fromState: cargoRequestEntity?.fromState?.id,
          toState: cargoRequestEntity?.toState?.id,
          fromCity: cargoRequestEntity?.fromCity?.id,
          toCity: cargoRequestEntity?.toCity?.id,
          reqItemTypes: cargoRequestEntity?.reqItemTypes?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="freelancerCourierApp.cargoRequest.home.createOrEditLabel" data-cy="CargoRequestCreateUpdateHeading">
            <Translate contentKey="freelancerCourierApp.cargoRequest.home.createOrEditLabel">Create or edit a CargoRequest</Translate>
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
                  id="cargo-request-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('freelancerCourierApp.cargoRequest.budget')}
                id="cargo-request-budget"
                name="budget"
                data-cy="budget"
                type="text"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.cargoRequest.isToDoor')}
                id="cargo-request-isToDoor"
                name="isToDoor"
                data-cy="isToDoor"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.cargoRequest.createDate')}
                id="cargo-request-createDate"
                name="createDate"
                data-cy="createDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.cargoRequest.agreedPrice')}
                id="cargo-request-agreedPrice"
                name="agreedPrice"
                data-cy="agreedPrice"
                type="text"
              />
              <ValidatedField
                id="cargo-request-status"
                name="status"
                data-cy="status"
                label={translate('freelancerCourierApp.cargoRequest.status')}
                type="select"
              >
                <option value="" key="0" />
                {cargoRequestStatuses
                  ? cargoRequestStatuses.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="cargo-request-createBy"
                name="createBy"
                data-cy="createBy"
                label={translate('freelancerCourierApp.cargoRequest.createBy')}
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
                id="cargo-request-takenBy"
                name="takenBy"
                data-cy="takenBy"
                label={translate('freelancerCourierApp.cargoRequest.takenBy')}
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
                id="cargo-request-fromCountry"
                name="fromCountry"
                data-cy="fromCountry"
                label={translate('freelancerCourierApp.cargoRequest.fromCountry')}
                type="select"
              >
                <option value="" key="0" />
                {countries
                  ? countries.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="cargo-request-toCountry"
                name="toCountry"
                data-cy="toCountry"
                label={translate('freelancerCourierApp.cargoRequest.toCountry')}
                type="select"
              >
                <option value="" key="0" />
                {countries
                  ? countries.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="cargo-request-fromState"
                name="fromState"
                data-cy="fromState"
                label={translate('freelancerCourierApp.cargoRequest.fromState')}
                type="select"
              >
                <option value="" key="0" />
                {stateProvinces
                  ? stateProvinces.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="cargo-request-toState"
                name="toState"
                data-cy="toState"
                label={translate('freelancerCourierApp.cargoRequest.toState')}
                type="select"
              >
                <option value="" key="0" />
                {stateProvinces
                  ? stateProvinces.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="cargo-request-fromCity"
                name="fromCity"
                data-cy="fromCity"
                label={translate('freelancerCourierApp.cargoRequest.fromCity')}
                type="select"
              >
                <option value="" key="0" />
                {cities
                  ? cities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="cargo-request-toCity"
                name="toCity"
                data-cy="toCity"
                label={translate('freelancerCourierApp.cargoRequest.toCity')}
                type="select"
              >
                <option value="" key="0" />
                {cities
                  ? cities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                label={translate('freelancerCourierApp.cargoRequest.reqItemTypes')}
                id="cargo-request-reqItemTypes"
                data-cy="reqItemTypes"
                type="select"
                multiple
                name="reqItemTypes"
              >
                <option value="" key="0" />
                {itemTypes
                  ? itemTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cargo-request" replace color="info">
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

export default CargoRequestUpdate;
