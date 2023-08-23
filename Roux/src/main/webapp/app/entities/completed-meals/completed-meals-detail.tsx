import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './completed-meals.reducer';

export const CompletedMealsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const completedMealsEntity = useAppSelector(state => state.completedMeals.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="completedMealsDetailsHeading">Completed Meals</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{completedMealsEntity.id}</dd>
          <dt>
            <span id="prepSteps">Prep Steps</span>
          </dt>
          <dd>{completedMealsEntity.prepSteps}</dd>
          <dt>
            <span id="ingredientList">Ingredient List</span>
          </dt>
          <dd>{completedMealsEntity.ingredientList}</dd>
          <dt>
            <span id="cookingSteps">Cooking Steps</span>
          </dt>
          <dd>{completedMealsEntity.cookingSteps}</dd>
        </dl>
        <Button tag={Link} to="/completed-meals" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/completed-meals/${completedMealsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default CompletedMealsDetail;
