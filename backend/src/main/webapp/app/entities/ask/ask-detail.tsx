import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ask.reducer';

export const AskDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const askEntity = useAppSelector(state => state.ask.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="askDetailsHeading">
          <Translate contentKey="freelancerCourierApp.ask.detail.title">Ask</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{askEntity.id}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="freelancerCourierApp.ask.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{askEntity.notes}</dd>
          <dt>
            <span id="price">
              <Translate contentKey="freelancerCourierApp.ask.price">Price</Translate>
            </span>
          </dt>
          <dd>{askEntity.price}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="freelancerCourierApp.ask.status">Status</Translate>
            </span>
          </dt>
          <dd>{askEntity.status}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.ask.toUser">To User</Translate>
          </dt>
          <dd>{askEntity.toUser ? askEntity.toUser.id : ''}</dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.ask.cargoRequest">Cargo Request</Translate>
          </dt>
          <dd>{askEntity.cargoRequest ? askEntity.cargoRequest.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ask" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ask/${askEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AskDetail;
