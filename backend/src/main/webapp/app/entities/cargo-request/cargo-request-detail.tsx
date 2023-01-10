import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cargo-request.reducer';

export const CargoRequestDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cargoRequestEntity = useAppSelector(state => state.cargoRequest.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cargoRequestDetailsHeading">
          <Translate contentKey="freelancerCourierApp.cargoRequest.detail.title">CargoRequest</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cargoRequestEntity.id}</dd>
          <dt>
            <span id="budget">
              <Translate contentKey="freelancerCourierApp.cargoRequest.budget">Budget</Translate>
            </span>
          </dt>
          <dd>{cargoRequestEntity.budget}</dd>
          <dt>
            <span id="isToDoor">
              <Translate contentKey="freelancerCourierApp.cargoRequest.isToDoor">Is To Door</Translate>
            </span>
          </dt>
          <dd>{cargoRequestEntity.isToDoor ? 'true' : 'false'}</dd>
          <dt>
            <span id="createDate">
              <Translate contentKey="freelancerCourierApp.cargoRequest.createDate">Create Date</Translate>
            </span>
          </dt>
          <dd>
            {cargoRequestEntity.createDate ? (
              <TextFormat value={cargoRequestEntity.createDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="agreedPrice">
              <Translate contentKey="freelancerCourierApp.cargoRequest.agreedPrice">Agreed Price</Translate>
            </span>
          </dt>
          <dd>{cargoRequestEntity.agreedPrice}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.cargoRequest.status">Status</Translate>
          </dt>
          <dd>{cargoRequestEntity.status ? cargoRequestEntity.status.name : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.cargoRequest.createBy">Create By</Translate>
          </dt>
          <dd>{cargoRequestEntity.createBy ? cargoRequestEntity.createBy.id : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.cargoRequest.takenBy">Taken By</Translate>
          </dt>
          <dd>{cargoRequestEntity.takenBy ? cargoRequestEntity.takenBy.id : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.cargoRequest.fromCountry">From Country</Translate>
          </dt>
          <dd>{cargoRequestEntity.fromCountry ? cargoRequestEntity.fromCountry.name : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.cargoRequest.toCountry">To Country</Translate>
          </dt>
          <dd>{cargoRequestEntity.toCountry ? cargoRequestEntity.toCountry.name : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.cargoRequest.fromState">From State</Translate>
          </dt>
          <dd>{cargoRequestEntity.fromState ? cargoRequestEntity.fromState.name : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.cargoRequest.toState">To State</Translate>
          </dt>
          <dd>{cargoRequestEntity.toState ? cargoRequestEntity.toState.name : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.cargoRequest.fromCity">From City</Translate>
          </dt>
          <dd>{cargoRequestEntity.fromCity ? cargoRequestEntity.fromCity.name : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.cargoRequest.toCity">To City</Translate>
          </dt>
          <dd>{cargoRequestEntity.toCity ? cargoRequestEntity.toCity.name : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.cargoRequest.reqItemTypes">Req Item Types</Translate>
          </dt>
          <dd>
            {cargoRequestEntity.reqItemTypes
              ? cargoRequestEntity.reqItemTypes.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {cargoRequestEntity.reqItemTypes && i === cargoRequestEntity.reqItemTypes.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/cargo-request" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cargo-request/${cargoRequestEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CargoRequestDetail;
