import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { ICompletedMeals } from 'app/shared/model/completed-meals.model';
import { getEntities } from './completed-meals.reducer';

export const CompletedMeals = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const completedMealsList = useAppSelector(state => state.completedMeals.entities);
  const loading = useAppSelector(state => state.completedMeals.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="completed-meals-heading" data-cy="CompletedMealsHeading">
        Completed Meals
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/completed-meals/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Completed Meals
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {completedMealsList && completedMealsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Prep Steps</th>
                <th>Ingredient List</th>
                <th>Cooking Steps</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {completedMealsList.map((completedMeals, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/completed-meals/${completedMeals.id}`} color="link" size="sm">
                      {completedMeals.id}
                    </Button>
                  </td>
                  <td>{completedMeals.prepSteps}</td>
                  <td>{completedMeals.ingredientList}</td>
                  <td>{completedMeals.cookingSteps}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/completed-meals/${completedMeals.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/completed-meals/${completedMeals.id}/edit`}
                        color="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button
                        tag={Link}
                        to={`/completed-meals/${completedMeals.id}/delete`}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Completed Meals found</div>
        )}
      </div>
    </div>
  );
};

export default CompletedMeals;
