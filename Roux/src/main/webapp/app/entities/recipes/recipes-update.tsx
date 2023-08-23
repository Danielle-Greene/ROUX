import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICompletedMeals } from 'app/shared/model/completed-meals.model';
import { getEntities as getCompletedMeals } from 'app/entities/completed-meals/completed-meals.reducer';
import { IRecipes } from 'app/shared/model/recipes.model';
import { getEntity, updateEntity, createEntity, reset } from './recipes.reducer';

export const RecipesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const completedMeals = useAppSelector(state => state.completedMeals.entities);
  const recipesEntity = useAppSelector(state => state.recipes.entity);
  const loading = useAppSelector(state => state.recipes.loading);
  const updating = useAppSelector(state => state.recipes.updating);
  const updateSuccess = useAppSelector(state => state.recipes.updateSuccess);

  const handleClose = () => {
    navigate('/recipes');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCompletedMeals({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...recipesEntity,
      ...values,
      recipes: completedMeals.find(it => it.id.toString() === values.recipes.toString()),
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
          ...recipesEntity,
          recipes: recipesEntity?.recipes?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rouxApp.recipes.home.createOrEditLabel" data-cy="RecipesCreateUpdateHeading">
            Create or edit a Recipes
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="recipes-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Recipe Name" id="recipes-recipeName" name="recipeName" data-cy="recipeName" type="text" />
              <ValidatedField id="recipes-recipes" name="recipes" data-cy="recipes" label="Recipes" type="select">
                <option value="" key="0" />
                {completedMeals
                  ? completedMeals.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/recipes" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default RecipesUpdate;
