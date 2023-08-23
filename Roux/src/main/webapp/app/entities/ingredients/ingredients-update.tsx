import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IRecipes } from 'app/shared/model/recipes.model';
import { getEntities as getRecipes } from 'app/entities/recipes/recipes.reducer';
import { IIngredients } from 'app/shared/model/ingredients.model';
import { getEntity, updateEntity, createEntity, reset } from './ingredients.reducer';

export const IngredientsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const recipes = useAppSelector(state => state.recipes.entities);
  const ingredientsEntity = useAppSelector(state => state.ingredients.entity);
  const loading = useAppSelector(state => state.ingredients.loading);
  const updating = useAppSelector(state => state.ingredients.updating);
  const updateSuccess = useAppSelector(state => state.ingredients.updateSuccess);

  const handleClose = () => {
    navigate('/ingredients');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getRecipes({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...ingredientsEntity,
      ...values,
      ingredients: recipes.find(it => it.id.toString() === values.ingredients.toString()),
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
          ...ingredientsEntity,
          ingredients: ingredientsEntity?.ingredients?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rouxApp.ingredients.home.createOrEditLabel" data-cy="IngredientsCreateUpdateHeading">
            Create or edit a Ingredients
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="ingredients-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Ingredient Name"
                id="ingredients-ingredientName"
                name="ingredientName"
                data-cy="ingredientName"
                type="text"
              />
              <ValidatedField id="ingredients-ingredients" name="ingredients" data-cy="ingredients" label="Ingredients" type="select">
                <option value="" key="0" />
                {recipes
                  ? recipes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/ingredients" replace color="info">
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

export default IngredientsUpdate;
