import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './bid.reducer';

export const BidDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const bidEntity = useAppSelector(state => state.bid.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="bidDetailsHeading">
          <Translate contentKey="freelancerCourierApp.bid.detail.title">Bid</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{bidEntity.id}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="freelancerCourierApp.bid.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{bidEntity.notes}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="freelancerCourierApp.bid.price">Price</Translate>
            </span>
          </dt>
          <dd>{bidEntity.price}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="freelancerCourierApp.bid.status">Status</Translate>
            </span>
          </dt>
          <dd>{bidEntity.status}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.bid.fromUser">From User</Translate>
          </dt>
          <dd>{bidEntity.fromUser ? bidEntity.fromUser.id : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.bid.cargoRequest">Cargo Request</Translate>
          </dt>
          <dd>{bidEntity.cargoRequest ? bidEntity.cargoRequest.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/bid" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/bid/${bidEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BidDetail;
