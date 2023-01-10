import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICargoRequest } from 'app/shared/model/cargo-request.model';
import { getEntities, reset } from './cargo-request.reducer';

export const CargoRequest = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );
  const [sorting, setSorting] = useState(false);

  const cargoRequestList = useAppSelector(state => state.cargoRequest.entities);
  const loading = useAppSelector(state => state.cargoRequest.loading);
  const totalItems = useAppSelector(state => state.cargoRequest.totalItems);
  const links = useAppSelector(state => state.cargoRequest.links);
  const entity = useAppSelector(state => state.cargoRequest.entity);
  const updateSuccess = useAppSelector(state => state.cargoRequest.updateSuccess);

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
      <h2 id="cargo-request-heading" data-cy="CargoRequestHeading">
        <Translate contentKey="freelancerCourierApp.cargoRequest.home.title">Cargo Requests</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="freelancerCourierApp.cargoRequest.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/cargo-request/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="freelancerCourierApp.cargoRequest.home.createLabel">Create new Cargo Request</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={cargoRequestList ? cargoRequestList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {cargoRequestList && cargoRequestList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="freelancerCourierApp.cargoRequest.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('budget')}>
                    <Translate contentKey="freelancerCourierApp.cargoRequest.budget">Budget</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('isToDoor')}>
                    <Translate contentKey="freelancerCourierApp.cargoRequest.isToDoor">Is To Door</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('createDate')}>
                    <Translate contentKey="freelancerCourierApp.cargoRequest.createDate">Create Date</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('agreedPrice')}>
                    <Translate contentKey="freelancerCourierApp.cargoRequest.agreedPrice">Agreed Price</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.cargoRequest.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.cargoRequest.createBy">Create By</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.cargoRequest.takenBy">Taken By</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.cargoRequest.fromCountry">From Country</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.cargoRequest.toCountry">To Country</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.cargoRequest.fromState">From State</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.cargoRequest.toState">To State</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.cargoRequest.fromCity">From City</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.cargoRequest.toCity">To City</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {cargoRequestList.map((cargoRequest, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/cargo-request/${cargoRequest.id}`} color="link" size="sm">
                        {cargoRequest.id}
                      </Button>
                    </td>
                    <td>{cargoRequest.budget}</td>
                    <td>{cargoRequest.isToDoor ? 'true' : 'false'}</td>
                    <td>
                      {cargoRequest.createDate ? <TextFormat type="date" value={cargoRequest.createDate} format={APP_DATE_FORMAT} /> : null}
                    </td>
                    <td>{cargoRequest.agreedPrice}</td>
                    <td>
                      {cargoRequest.status ? (
                        <Link to={`/cargo-request-status/${cargoRequest.status.id}`}>{cargoRequest.status.name}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {cargoRequest.createBy ? <Link to={`/app-user/${cargoRequest.createBy.id}`}>{cargoRequest.createBy.id}</Link> : ''}
                    </td>
                    <td>
                      {cargoRequest.takenBy ? <Link to={`/app-user/${cargoRequest.takenBy.id}`}>{cargoRequest.takenBy.id}</Link> : ''}
                    </td>
                    <td>
                      {cargoRequest.fromCountry ? (
                        <Link to={`/country/${cargoRequest.fromCountry.id}`}>{cargoRequest.fromCountry.name}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {cargoRequest.toCountry ? (
                        <Link to={`/country/${cargoRequest.toCountry.id}`}>{cargoRequest.toCountry.name}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {cargoRequest.fromState ? (
                        <Link to={`/state-province/${cargoRequest.fromState.id}`}>{cargoRequest.fromState.name}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {cargoRequest.toState ? (
                        <Link to={`/state-province/${cargoRequest.toState.id}`}>{cargoRequest.toState.name}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td>
                      {cargoRequest.fromCity ? <Link to={`/city/${cargoRequest.fromCity.id}`}>{cargoRequest.fromCity.name}</Link> : ''}
                    </td>
                    <td>{cargoRequest.toCity ? <Link to={`/city/${cargoRequest.toCity.id}`}>{cargoRequest.toCity.name}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/cargo-request/${cargoRequest.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`/cargo-request/${cargoRequest.id}/edit`}
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
                          to={`/cargo-request/${cargoRequest.id}/delete`}
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
                <Translate contentKey="freelancerCourierApp.cargoRequest.home.notFound">No Cargo Requests found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default CargoRequest;
