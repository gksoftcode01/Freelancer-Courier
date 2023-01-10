import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, openFile, byteSize } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cargo-request-details.reducer';

export const CargoRequestDetailsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const cargoRequestDetailsEntity = useAppSelector(state => state.cargoRequestDetails.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cargoRequestDetailsDetailsHeading">
          <Translate contentKey="freelancerCourierApp.cargoRequestDetails.detail.title">CargoRequestDetails</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cargoRequestDetailsEntity.id}</dd>
          <dt>
            <span id="itemDesc">
              <Translate contentKey="freelancerCourierApp.cargoRequestDetails.itemDesc">Item Desc</Translate>
            </span>
          </dt>
          <dd>{cargoRequestDetailsEntity.itemDesc}</dd>
          <dt>
            <span id="itemWeight">
              <Translate contentKey="freelancerCourierApp.cargoRequestDetails.itemWeight">Item Weight</Translate>
            </span>
          </dt>
          <dd>{cargoRequestDetailsEntity.itemWeight}</dd>
          <dt>
            <span id="itemHeight">
              <Translate contentKey="freelancerCourierApp.cargoRequestDetails.itemHeight">Item Height</Translate>
            </span>
          </dt>
          <dd>{cargoRequestDetailsEntity.itemHeight}</dd>
          <dt>
            <span id="itemWidth">
              <Translate contentKey="freelancerCourierApp.cargoRequestDetails.itemWidth">Item Width</Translate>
            </span>
          </dt>
          <dd>{cargoRequestDetailsEntity.itemWidth}</dd>
          <dt>
            <span id="itemPhoto">
              <Translate contentKey="freelancerCourierApp.cargoRequestDetails.itemPhoto">Item Photo</Translate>
            </span>
          </dt>
          <dd>
            {cargoRequestDetailsEntity.itemPhoto ? (
              <div>
                {cargoRequestDetailsEntity.itemPhotoContentType ? (
                  <a onClick={openFile(cargoRequestDetailsEntity.itemPhotoContentType, cargoRequestDetailsEntity.itemPhoto)}>
                    <Translate contentKey="entity.action.open">Open</Translate>&nbsp;
                  </a>
                ) : null}
                <span>
                  {cargoRequestDetailsEntity.itemPhotoContentType}, {byteSize(cargoRequestDetailsEntity.itemPhoto)}
                </span>
              </div>
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="freelancerCourierApp.cargoRequestDetails.cargoRequest">Cargo Request</Translate>
          </dt>
          <dd>{cargoRequestDetailsEntity.cargoRequest ? cargoRequestDetailsEntity.cargoRequest.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/cargo-request-details" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/cargo-request-details/${cargoRequestDetailsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CargoRequestDetailsDetail;
