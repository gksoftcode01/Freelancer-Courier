import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './app-user.reducer';

export const AppUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const appUserEntity = useAppSelector(state => state.appUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="appUserDetailsHeading">
          <Translate contentKey="freelancerCourierApp.appUser.detail.title">AppUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{appUserEntity.id}</dd>
          <dt>
            <span id="birthDate">
              <Translate contentKey="freelancerCourierApp.appUser.birthDate">Birth Date</Translate>
            </span>
          </dt>
          <dd>{appUserEntity.birthDate ? <TextFormat value={appUserEntity.birthDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="gender">
              <Translate contentKey="freelancerCourierApp.appUser.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{appUserEntity.gender}</dd>
          <dt>
            <span id="registerDate">
              <Translate contentKey="freelancerCourierApp.appUser.registerDate">Register Date</Translate>
            </span>
          </dt>
          <dd>
            {appUserEntity.registerDate ? <TextFormat value={appUserEntity.registerDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="freelancerCourierApp.appUser.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{appUserEntity.phoneNumber}</dd>
          <dt>
            <span id="mobileNumber">
              <Translate contentKey="freelancerCourierApp.appUser.mobileNumber">Mobile Number</Translate>
            </span>
          </dt>
          <dd>{appUserEntity.mobileNumber}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.appUser.user">User</Translate>
          </dt>
          <dd>{appUserEntity.user ? appUserEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.appUser.country">Country</Translate>
          </dt>
          <dd>{appUserEntity.country ? appUserEntity.country.name : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.appUser.stateProvince">State Province</Translate>
          </dt>
          <dd>{appUserEntity.stateProvince ? appUserEntity.stateProvince.name : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.appUser.country">Country</Translate>
          </dt>
          <dd>{appUserEntity.country ? appUserEntity.country.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/app-user" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/app-user/${appUserEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AppUserDetail;
