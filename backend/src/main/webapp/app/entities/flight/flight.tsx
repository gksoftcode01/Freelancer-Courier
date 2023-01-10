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

import { IFlight } from 'app/shared/model/flight.model';
import { getEntities, reset } from './flight.reducer';

export const Flight = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(location, ITEMS_PER_PAGE, 'id'), location.search)
  );
  const [sorting, setSorting] = useState(false);

  const flightList = useAppSelector(state => state.flight.entities);
  const loading = useAppSelector(state => state.flight.loading);
  const totalItems = useAppSelector(state => state.flight.totalItems);
  const links = useAppSelector(state => state.flight.links);
  const entity = useAppSelector(state => state.flight.entity);
  const updateSuccess = useAppSelector(state => state.flight.updateSuccess);

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
      <h2 id="flight-heading" data-cy="FlightHeading">
        <Translate contentKey="freelancerCourierApp.flight.home.title">Flights</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="freelancerCourierApp.flight.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/flight/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="freelancerCourierApp.flight.home.createLabel">Create new Flight</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          dataLength={flightList ? flightList.length : 0}
          next={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
        >
          {flightList && flightList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  <th className="hand" onClick={sort('id')}>
                    <Translate contentKey="freelancerCourierApp.flight.id">ID</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('flightDate')}>
                    <Translate contentKey="freelancerCourierApp.flight.flightDate">Flight Date</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('maxWeight')}>
                    <Translate contentKey="freelancerCourierApp.flight.maxWeight">Max Weight</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('notes')}>
                    <Translate contentKey="freelancerCourierApp.flight.notes">Notes</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('budget')}>
                    <Translate contentKey="freelancerCourierApp.flight.budget">Budget</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('createDate')}>
                    <Translate contentKey="freelancerCourierApp.flight.createDate">Create Date</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('toDoorAvailable')}>
                    <Translate contentKey="freelancerCourierApp.flight.toDoorAvailable">To Door Available</Translate>{' '}
                    <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('status')}>
                    <Translate contentKey="freelancerCourierApp.flight.status">Status</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.flight.createBy">Create By</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.flight.fromCountry">From Country</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.flight.toCountry">To Country</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.flight.fromState">From State</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.flight.toState">To State</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.flight.fromCity">From City</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    <Translate contentKey="freelancerCourierApp.flight.toCity">To City</Translate> <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {flightList.map((flight, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    <td>
                      <Button tag={Link} to={`/flight/${flight.id}`} color="link" size="sm">
                        {flight.id}
                      </Button>
                    </td>
                    <td>{flight.flightDate ? <TextFormat type="date" value={flight.flightDate} format={APP_DATE_FORMAT} /> : null}</td>
                    <td>{flight.maxWeight}</td>
                    <td>{flight.notes}</td>
                    <td>{flight.budget}</td>
                    <td>{flight.createDate ? <TextFormat type="date" value={flight.createDate} format={APP_DATE_FORMAT} /> : null}</td>
                    <td>{flight.toDoorAvailable ? 'true' : 'false'}</td>
                    <td>
                      <Translate contentKey={`freelancerCourierApp.FlightStatus.${flight.status}`} />
                    </td>
                    <td>{flight.createBy ? <Link to={`/app-user/${flight.createBy.id}`}>{flight.createBy.id}</Link> : ''}</td>
                    <td>{flight.fromCountry ? <Link to={`/country/${flight.fromCountry.id}`}>{flight.fromCountry.name}</Link> : ''}</td>
                    <td>{flight.toCountry ? <Link to={`/country/${flight.toCountry.id}`}>{flight.toCountry.name}</Link> : ''}</td>
                    <td>{flight.fromState ? <Link to={`/state-province/${flight.fromState.id}`}>{flight.fromState.name}</Link> : ''}</td>
                    <td>{flight.toState ? <Link to={`/state-province/${flight.toState.id}`}>{flight.toState.name}</Link> : ''}</td>
                    <td>{flight.fromCity ? <Link to={`/city/${flight.fromCity.id}`}>{flight.fromCity.name}</Link> : ''}</td>
                    <td>{flight.toCity ? <Link to={`/city/${flight.toCity.id}`}>{flight.toCity.name}</Link> : ''}</td>
                    <td className="text-end">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`/flight/${flight.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/flight/${flight.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`/flight/${flight.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
                <Translate contentKey="freelancerCourierApp.flight.home.notFound">No Flights found</Translate>
              </div>
            )
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default Flight;
