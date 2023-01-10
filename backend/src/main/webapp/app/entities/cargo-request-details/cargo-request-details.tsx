import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { openFile, byteSize, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICargoRequestDetails } from 'app/shared/model/cargo-request-details.model';
import { getEntities, reset } from './cargo-request-details.reducer';

export const CargoRequestDetails = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );
  const [sorting, setSorting] = useState(false);

  const cargoRequestDetailsList = useAppSelector(state => state.cargoRequestDetails.entities);
  const loading = useAppSelector(state => state.cargoRequestDetails.loading);
  const totalItems = useAppSelector(state => state.cargoRequestDetails.totalItems);
  const links = useAppSelector(state => state.cargoRequestDetails.links);
  const entity = useAppSelector(state => state.cargoRequestDetails.entity);
  const updateSuccess = useAppSelector(state => state.cargoRequestDetails.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  return (
    <div>
      <h2 id="cargo-request-details-heading" data-cy="CargoRequestDetailsHeading">
        <Translate contentKey="freelancerCourierApp.cargoRequestDetails.home.title">Cargo Request Details</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="freelancerCourierApp.cargoRequestDetails.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/cargo-request-details/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="freelancerCourierApp.cargoRequestDetails.home.createLabel">Create new Cargo Request Details</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={cargoRequestDetailsList ? cargoRequestDetailsList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {cargoRequestDetailsList && cargoRequestDetailsList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="freelancerCourierApp.cargoRequestDetails.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('itemDesc')}>
                    <Translate contentKey="freelancerCourierApp.cargoRequestDetails.itemDesc">Item Desc</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('itemWeight')}>
                    <Translate contentKey="freelancerCourierApp.cargoRequestDetails.itemWeight">Item Weight</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('itemHeight')}>
                    <Translate contentKey="freelancerCourierApp.cargoRequestDetails.itemHeight">Item Height</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('itemWidth')}>
                    <Translate contentKey="freelancerCourierApp.cargoRequestDetails.itemWidth">Item Width</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('itemPhoto')}>
                    <Translate contentKey="freelancerCourierApp.cargoRequestDetails.itemPhoto">Item Photo</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.cargoRequestDetails.cargoRequest">Cargo Request</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {cargoRequestDetailsList.map((cargoRequestDetails, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/cargo-request-details/${cargoRequestDetails.id}`} color="link" size="sm">
                        {cargoRequestDetails.id}
                      </Button>
                    </td>
                    <td>{cargoRequestDetails.itemDesc}</td>
                    <td>{cargoRequestDetails.itemWeight}</td>
                    <td>{cargoRequestDetails.itemHeight}</td>
                    <td>{cargoRequestDetails.itemWidth}</td>
                    <td>
                      {cargoRequestDetails.itemPhoto ? (
                        <div>
                          {cargoRequestDetails.itemPhotoContentType ? (
                            <a onClick={openFile(cargoRequestDetails.itemPhotoContentType, cargoRequestDetails.itemPhoto)}>
                              <Translate contentKey="entity.action.open">Open</Translate>
                              &nbsp;
                            </a>
                          ) : null}
                          <span>
                            {cargoRequestDetails.itemPhotoContentType}, {byteSize(cargoRequestDetails.itemPhoto)}
                          </span>
                        </div>
                      ) : null}
                    </td>
                    <td>
                      {cargoRequestDetails.cargoRequest ? (
                        <Link to={`/cargo-request/${cargoRequestDetails.cargoRequest.id}`}>{cargoRequestDetails.cargoRequest.id}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          tag={Link}
                          to={`/cargo-request-details/${cargoRequestDetails.id}`}
                          color="info"
                          size="sm"
                          data-cy="entityDetailsButton"
                        >
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`/cargo-request-details/${cargoRequestDetails.id}/edit`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`/cargo-request-details/${cargoRequestDetails.id}/delete`}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && (
              <div className="alert alert-warning">
                <Translate contentKey="freelancerCourierApp.cargoRequestDetails.home.notFound">No Cargo Request Details found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default CargoRequestDetails;
