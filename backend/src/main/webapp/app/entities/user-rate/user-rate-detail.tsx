import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './user-rate.reducer';

export const UserRateDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const userRateEntity = useAppSelector(state => state.userRate.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userRateDetailsHeading">
          <Translate contentKey="freelancerCourierApp.userRate.detail.title">UserRate</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{userRateEntity.id}</dd>
          <dt>
            <span id="rate">
              <Translate contentKey="freelancerCourierApp.userRate.rate">Rate</Translate>
            </span>
          </dt>
          <dd>{userRateEntity.rate}</dd>
          <dt>
            <span id="note">
              <Translate contentKey="freelancerCourierApp.userRate.note">Note</Translate>
            </span>
          </dt>
          <dd>{userRateEntity.note}</dd>
          <dt>
            <span id="rateDate">
              <Translate contentKey="freelancerCourierApp.userRate.rateDate">Rate Date</Translate>
            </span>
          </dt>
          <dd>{userRateEntity.rateDate ? <TextFormat value={userRateEntity.rateDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.userRate.cargoRequest">Cargo Request</Translate>
          </dt>
          <dd>{userRateEntity.cargoRequest ? userRateEntity.cargoRequest.id : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.userRate.appUser">App User</Translate>
          </dt>
          <dd>{userRateEntity.appUser ? userRateEntity.appUser.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-rate" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/user-rate/${userRateEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default UserRateDetail;
