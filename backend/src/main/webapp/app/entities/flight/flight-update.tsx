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
import { ICountry } from 'app/shared/model/country.model';
import { getEntities as getCountries } from 'app/entities/country/country.reducer';
import { IStateProvince } from 'app/shared/model/state-province.model';
import { getEntities as getStateProvinces } from 'app/entities/state-province/state-province.reducer';
import { ICity } from 'app/shared/model/city.model';
import { getEntities as getCities } from 'app/entities/city/city.reducer';
import { IItemTypes } from 'app/shared/model/item-types.model';
import { getEntities as getItemTypes } from 'app/entities/item-types/item-types.reducer';
import { IFlight } from 'app/shared/model/flight.model';
import { FlightStatus } from 'app/shared/model/enumerations/flight-status.model';
import { getEntity, updateEntity, createEntity, reset } from './flight.reducer';

export const FlightUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const appUsers = useAppSelector(state => state.appUser.entities);
  const countries = useAppSelector(state => state.country.entities);
  const stateProvinces = useAppSelector(state => state.stateProvince.entities);
  const cities = useAppSelector(state => state.city.entities);
  const itemTypes = useAppSelector(state => state.itemTypes.entities);
  const flightEntity = useAppSelector(state => state.flight.entity);
  const loading = useAppSelector(state => state.flight.loading);
  const updating = useAppSelector(state => state.flight.updating);
  const updateSuccess = useAppSelector(state => state.flight.updateSuccess);
  const flightStatusValues = Object.keys(FlightStatus);

  const handleClose = () => {
    navigate('/flight');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getAppUsers({}));
    dispatch(getCountries({}));
    dispatch(getStateProvinces({}));
    dispatch(getCities({}));
    dispatch(getItemTypes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.flightDate = convertDateTimeToServer(values.flightDate);
    values.createDate = convertDateTimeToServer(values.createDate);

    const entity = {
      ...flightEntity,
      ...values,
      availableItemTypes: mapIdList(values.availableItemTypes),
      createBy: appUsers.find(it => it.id.toString() === values.createBy.toString()),
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
          flightDate: displayDefaultDateTime(),
          createDate: displayDefaultDateTime(),
        }
      : {
          status: 'Available',
          ...flightEntity,
          flightDate: convertDateTimeFromServer(flightEntity.flightDate),
          createDate: convertDateTimeFromServer(flightEntity.createDate),
          createBy: flightEntity?.createBy?.id,
          fromCountry: flightEntity?.fromCountry?.id,
          toCountry: flightEntity?.toCountry?.id,
          fromState: flightEntity?.fromState?.id,
          toState: flightEntity?.toState?.id,
          fromCity: flightEntity?.fromCity?.id,
          toCity: flightEntity?.toCity?.id,
          availableItemTypes: flightEntity?.availableItemTypes?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="freelancerCourierApp.flight.home.createOrEditLabel" data-cy="FlightCreateUpdateHeading">
            <Translate contentKey="freelancerCourierApp.flight.home.createOrEditLabel">Create or edit a Flight</Translate>
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
                  id="flight-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('freelancerCourierApp.flight.flightDate')}
                id="flight-flightDate"
                name="flightDate"
                data-cy="flightDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.flight.maxWeight')}
                id="flight-maxWeight"
                name="maxWeight"
                data-cy="maxWeight"
                type="text"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.flight.notes')}
                id="flight-notes"
                name="notes"
                data-cy="notes"
                type="text"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.flight.budget')}
                id="flight-budget"
                name="budget"
                data-cy="budget"
                type="text"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.flight.createDate')}
                id="flight-createDate"
                name="createDate"
                data-cy="createDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.flight.toDoorAvailable')}
                id="flight-toDoorAvailable"
                name="toDoorAvailable"
                data-cy="toDoorAvailable"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.flight.status')}
                id="flight-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {flightStatusValues.map(flightStatus => (
                  <option value={flightStatus} key={flightStatus}>
                    {translate('freelancerCourierApp.FlightStatus.' + flightStatus)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                id="flight-createBy"
                name="createBy"
                data-cy="createBy"
                label={translate('freelancerCourierApp.flight.createBy')}
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
                id="flight-fromCountry"
                name="fromCountry"
                data-cy="fromCountry"
                label={translate('freelancerCourierApp.flight.fromCountry')}
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
                id="flight-toCountry"
                name="toCountry"
                data-cy="toCountry"
                label={translate('freelancerCourierApp.flight.toCountry')}
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
                id="flight-fromState"
                name="fromState"
                data-cy="fromState"
                label={translate('freelancerCourierApp.flight.fromState')}
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
                id="flight-toState"
                name="toState"
                data-cy="toState"
                label={translate('freelancerCourierApp.flight.toState')}
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
                id="flight-fromCity"
                name="fromCity"
                data-cy="fromCity"
                label={translate('freelancerCourierApp.flight.fromCity')}
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
                id="flight-toCity"
                name="toCity"
                data-cy="toCity"
                label={translate('freelancerCourierApp.flight.toCity')}
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
                label={translate('freelancerCourierApp.flight.availableItemTypes')}
                id="flight-availableItemTypes"
                data-cy="availableItemTypes"
                type="select"
                multiple
                name="availableItemTypes"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/flight" replace color="info">
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

export default FlightUpdate;
