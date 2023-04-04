import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICourses } from 'app/shared/model/courses.model';
import { getEntities } from './courses.reducer';

export const Courses = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const coursesList = useAppSelector(state => state.courses.entities);
  const loading = useAppSelector(state => state.courses.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="courses-heading" data-cy="CoursesHeading">
        <Translate contentKey="jhipsterFinalProjectApp.courses.home.title">Courses</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="jhipsterFinalProjectApp.courses.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/courses/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="jhipsterFinalProjectApp.courses.home.createLabel">Create new Courses</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {coursesList && coursesList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="jhipsterFinalProjectApp.courses.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterFinalProjectApp.courses.courseName">Course Name</Translate>
                </th>
                <th>
                  <Translate contentKey="jhipsterFinalProjectApp.courses.courseCost">Course Cost</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {coursesList.map((courses, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/courses/${courses.id}`} color="link" size="sm">
                      {courses.id}
                    </Button>
                  </td>
                  <td>{courses.courseName}</td>
                  <td>{courses.courseCost}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/courses/${courses.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/courses/${courses.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/courses/${courses.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
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
              <Translate contentKey="jhipsterFinalProjectApp.courses.home.notFound">No Courses found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Courses;
