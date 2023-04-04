import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './courses.reducer';

export const CoursesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const coursesEntity = useAppSelector(state => state.courses.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="coursesDetailsHeading">
          <Translate contentKey="jhipsterFinalProjectApp.courses.detail.title">Courses</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{coursesEntity.id}</dd>
          <dt>
            <span id="courseName">
              <Translate contentKey="jhipsterFinalProjectApp.courses.courseName">Course Name</Translate>
            </span>
          </dt>
          <dd>{coursesEntity.courseName}</dd>
          <dt>
            <span id="courseCost">
              <Translate contentKey="jhipsterFinalProjectApp.courses.courseCost">Course Cost</Translate>
            </span>
          </dt>
          <dd>{coursesEntity.courseCost}</dd>
        </dl>
        <Button tag={Link} to="/courses" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/courses/${coursesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CoursesDetail;
