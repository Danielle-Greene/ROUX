import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './recipes.reducer';

export const RecipesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const recipesEntity = useAppSelector(state => state.recipes.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="recipesDetailsHeading">Recipes</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{recipesEntity.id}</dd>
          <dt>
            <span id="recipeName">Recipe Name</span>
          </dt>
          <dd>{recipesEntity.recipeName}</dd>
          <dt>Recipes</dt>
          <dd>{recipesEntity.recipes ? recipesEntity.recipes.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/recipes" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/recipes/${recipesEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default RecipesDetail;
