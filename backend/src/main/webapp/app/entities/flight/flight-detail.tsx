import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './flight.reducer';

export const FlightDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const flightEntity = useAppSelector(state => state.flight.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="flightDetailsHeading">
          <Translate contentKey="freelancerCourierApp.flight.detail.title">Flight</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{flightEntity.id}</dd>
          <dt>
            <span id="flightDate">
              <Translate contentKey="freelancerCourierApp.flight.flightDate">Flight Date</Translate>
            </span>
          </dt>
          <dd>{flightEntity.flightDate ? <TextFormat value={flightEntity.flightDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="maxWeight">
              <Translate contentKey="freelancerCourierApp.flight.maxWeight">Max Weight</Translate>
            </span>
          </dt>
          <dd>{flightEntity.maxWeight}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="freelancerCourierApp.flight.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{flightEntity.notes}</dd>
          <dt>
            <span id="budget">
              <Translate contentKey="freelancerCourierApp.flight.budget">Budget</Translate>
            </span>
          </dt>
          <dd>{flightEntity.budget}</dd>
          <dt>
            <span id="createDate">
              <Translate contentKey="freelancerCourierApp.flight.createDate">Create Date</Translate>
            </span>
          </dt>
          <dd>{flightEntity.createDate ? <TextFormat value={flightEntity.createDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="toDoorAvailable">
              <Translate contentKey="freelancerCourierApp.flight.toDoorAvailable">To Door Available</Translate>
            </span>
          </dt>
          <dd>{flightEntity.toDoorAvailable ? 'true' : 'false'}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="freelancerCourierApp.flight.status">Status</Translate>
            </span>
          </dt>
          <dd>{flightEntity.status}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.flight.createBy">Create By</Translate>
          </dt>
          <dd>{flightEntity.createBy ? flightEntity.createBy.id : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.flight.fromCountry">From Country</Translate>
          </dt>
          <dd>{flightEntity.fromCountry ? flightEntity.fromCountry.name : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.flight.toCountry">To Country</Translate>
          </dt>
          <dd>{flightEntity.toCountry ? flightEntity.toCountry.name : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.flight.fromState">From State</Translate>
          </dt>
          <dd>{flightEntity.fromState ? flightEntity.fromState.name : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.flight.toState">To State</Translate>
          </dt>
          <dd>{flightEntity.toState ? flightEntity.toState.name : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.flight.fromCity">From City</Translate>
          </dt>
          <dd>{flightEntity.fromCity ? flightEntity.fromCity.name : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.flight.toCity">To City</Translate>
          </dt>
          <dd>{flightEntity.toCity ? flightEntity.toCity.name : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.flight.availableItemTypes">Available Item Types</Translate>
          </dt>
          <dd>
            {flightEntity.availableItemTypes
              ? flightEntity.availableItemTypes.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.name}</a>
                    {flightEntity.availableItemTypes && i === flightEntity.availableItemTypes.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/flight" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/flight/${flightEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FlightDetail;
