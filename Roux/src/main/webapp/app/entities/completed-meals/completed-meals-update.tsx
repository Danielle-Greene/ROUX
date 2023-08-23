import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICompletedMeals } from 'app/shared/model/completed-meals.model';
import { getEntity, updateEntity, createEntity, reset } from './completed-meals.reducer';

export const CompletedMealsUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const completedMealsEntity = useAppSelector(state => state.completedMeals.entity);
  const loading = useAppSelector(state => state.completedMeals.loading);
  const updating = useAppSelector(state => state.completedMeals.updating);
  const updateSuccess = useAppSelector(state => state.completedMeals.updateSuccess);

  const handleClose = () => {
    navigate('/completed-meals');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...completedMealsEntity,
      ...values,
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
          ...completedMealsEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="rouxApp.completedMeals.home.createOrEditLabel" data-cy="CompletedMealsCreateUpdateHeading">
            Create or edit a Completed Meals
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField name="id" required readOnly id="completed-meals-id" label="ID" validate={{ required: true }} />
              ) : null}
              <ValidatedField label="Prep Steps" id="completed-meals-prepSteps" name="prepSteps" data-cy="prepSteps" type="text" />
              <ValidatedField
                label="Ingredient List"
                id="completed-meals-ingredientList"
                name="ingredientList"
                data-cy="ingredientList"
                type="text"
              />
              <ValidatedField
                label="Cooking Steps"
                id="completed-meals-cookingSteps"
                name="cookingSteps"
                data-cy="cookingSteps"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/completed-meals" replace color="info">
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

export default CompletedMealsUpdate;
