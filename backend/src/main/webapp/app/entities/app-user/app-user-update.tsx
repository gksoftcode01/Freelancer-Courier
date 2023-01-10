import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { ICountry } from 'app/shared/model/country.model';
import { getEntities as getCountries } from 'app/entities/country/country.reducer';
import { IStateProvince } from 'app/shared/model/state-province.model';
import { getEntities as getStateProvinces } from 'app/entities/state-province/state-province.reducer';
import { ICity } from 'app/shared/model/city.model';
import { getEntities as getCities } from 'app/entities/city/city.reducer';
import { IAppUser } from 'app/shared/model/app-user.model';
import { GenderType } from 'app/shared/model/enumerations/gender-type.model';
import { getEntity, updateEntity, createEntity, reset } from './app-user.reducer';

export const AppUserUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const countries = useAppSelector(state => state.country.entities);
  const stateProvinces = useAppSelector(state => state.stateProvince.entities);
  const cities = useAppSelector(state => state.city.entities);
  const appUserEntity = useAppSelector(state => state.appUser.entity);
  const loading = useAppSelector(state => state.appUser.loading);
  const updating = useAppSelector(state => state.appUser.updating);
  const updateSuccess = useAppSelector(state => state.appUser.updateSuccess);
  const genderTypeValues = Object.keys(GenderType);

  const handleClose = () => {
    navigate('/app-user');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
    dispatch(getCountries({}));
    dispatch(getStateProvinces({}));
    dispatch(getCities({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.birthDate = convertDateTimeToServer(values.birthDate);
    values.registerDate = convertDateTimeToServer(values.registerDate);

    const entity = {
      ...appUserEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user.toString()),
      country: countries.find(it => it.id.toString() === values.country.toString()),
      stateProvince: stateProvinces.find(it => it.id.toString() === values.stateProvince.toString()),
      city: cities.find(it => it.id.toString() === values.city.toString()),
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
          birthDate: displayDefaultDateTime(),
          registerDate: displayDefaultDateTime(),
        }
      : {
          gender: 'Male',
          ...appUserEntity,
          birthDate: convertDateTimeFromServer(appUserEntity.birthDate),
          registerDate: convertDateTimeFromServer(appUserEntity.registerDate),
          user: appUserEntity?.user?.id,
          country: appUserEntity?.country?.id,
          stateProvince: appUserEntity?.stateProvince?.id,
        city: appUserEntity?.city?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="freelancerCourierApp.appUser.home.createOrEditLabel" data-cy="AppUserCreateUpdateHeading">
            <Translate contentKey="freelancerCourierApp.appUser.home.createOrEditLabel">Create or edit a AppUser</Translate>
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
                  id="app-user-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('freelancerCourierApp.appUser.birthDate')}
                id="app-user-birthDate"
                name="birthDate"
                data-cy="birthDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.appUser.gender')}
                id="app-user-gender"
                name="gender"
                data-cy="gender"
                type="select"
              >
                {genderTypeValues.map(genderType => (
                  <option value={genderType} key={genderType}>
                    {translate('freelancerCourierApp.GenderType.' + genderType)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('freelancerCourierApp.appUser.registerDate')}
                id="app-user-registerDate"
                name="registerDate"
                data-cy="registerDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.appUser.phoneNumber')}
                id="app-user-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.appUser.mobileNumber')}
                id="app-user-mobileNumber"
                name="mobileNumber"
                data-cy="mobileNumber"
                type="text"
              />
              <ValidatedField
                id="app-user-user"
                name="user"
                data-cy="user"
                label={translate('freelancerCourierApp.appUser.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="app-user-country"
                name="country"
                data-cy="country"
                label={translate('freelancerCourierApp.appUser.country')}
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
                id="app-user-stateProvince"
                name="stateProvince"
                data-cy="stateProvince"
                label={translate('freelancerCourierApp.appUser.stateProvince')}
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
                id="app-user-country"
                name="country"
                data-cy="country"
                label={translate('freelancerCourierApp.appUser.country')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/app-user" replace color="info">
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

export default AppUserUpdate;
