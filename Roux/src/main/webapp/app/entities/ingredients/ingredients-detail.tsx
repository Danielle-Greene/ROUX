import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './ingredients.reducer';

export const IngredientsDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const ingredientsEntity = useAppSelector(state => state.ingredients.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="ingredientsDetailsHeading">Ingredients</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{ingredientsEntity.id}</dd>
          <dt>
            <span id="ingredientName">Ingredient Name</span>
          </dt>
          <dd>{ingredientsEntity.ingredientName}</dd>
          <dt>Ingredients</dt>
          <dd>{ingredientsEntity.ingredients ? ingredientsEntity.ingredients.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/ingredients" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/ingredients/${ingredientsEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default IngredientsDetail;
