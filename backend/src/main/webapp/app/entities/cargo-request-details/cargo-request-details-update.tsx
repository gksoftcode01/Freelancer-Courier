import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm, ValidatedBlobField } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICargoRequest } from 'app/shared/model/cargo-request.model';
import { getEntities as getCargoRequests } from 'app/entities/cargo-request/cargo-request.reducer';
import { ICargoRequestDetails } from 'app/shared/model/cargo-request-details.model';
import { getEntity, updateEntity, createEntity, reset } from './cargo-request-details.reducer';

export const CargoRequestDetailsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cargoRequests = useAppSelector(state => state.cargoRequest.entities);
  const cargoRequestDetailsEntity = useAppSelector(state => state.cargoRequestDetails.entity);
  const loading = useAppSelector(state => state.cargoRequestDetails.loading);
  const updating = useAppSelector(state => state.cargoRequestDetails.updating);
  const updateSuccess = useAppSelector(state => state.cargoRequestDetails.updateSuccess);

  const handleClose = () => {
    navigate('/cargo-request-details');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(id));
    }

    dispatch(getCargoRequests({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...cargoRequestDetailsEntity,
      ...values,
      cargoRequest: cargoRequests.find(it => it.id.toString() === values.cargoRequest.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...cargoRequestDetailsEntity,
          cargoRequest: cargoRequestDetailsEntity?.cargoRequest?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="freelancerCourierApp.cargoRequestDetails.home.createOrEditLabel" data-cy="CargoRequestDetailsCreateUpdateHeading">
            <Translate contentKey="freelancerCourierApp.cargoRequestDetails.home.createOrEditLabel">
              Create or edit a CargoRequestDetails
            </Translate>
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
                  id="cargo-request-details-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('freelancerCourierApp.cargoRequestDetails.itemDesc')}
                id="cargo-request-details-itemDesc"
                name="itemDesc"
                data-cy="itemDesc"
                type="text"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.cargoRequestDetails.itemWeight')}
                id="cargo-request-details-itemWeight"
                name="itemWeight"
                data-cy="itemWeight"
                type="text"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.cargoRequestDetails.itemHeight')}
                id="cargo-request-details-itemHeight"
                name="itemHeight"
                data-cy="itemHeight"
                type="text"
              />
              <ValidatedField
                label={translate('freelancerCourierApp.cargoRequestDetails.itemWidth')}
                id="cargo-request-details-itemWidth"
                name="itemWidth"
                data-cy="itemWidth"
                type="text"
              />
              <ValidatedBlobField
                label={translate('freelancerCourierApp.cargoRequestDetails.itemPhoto')}
                id="cargo-request-details-itemPhoto"
                name="itemPhoto"
                data-cy="itemPhoto"
                openActionLabel={translate('entity.action.open')}
              />
              <ValidatedField
                id="cargo-request-details-cargoRequest"
                name="cargoRequest"
                data-cy="cargoRequest"
                label={translate('freelancerCourierApp.cargoRequestDetails.cargoRequest')}
                type="select"
              >
                <option value="" key="0" />
                {cargoRequests
                  ? cargoRequests.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/cargo-request-details" replace color="info">
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

export default CargoRequestDetailsUpdate;
