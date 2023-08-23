import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IIngredients } from 'app/shared/model/ingredients.model';
import { getEntities } from './ingredients.reducer';

export const Ingredients = () => {
  const dispatch = useAppDispatch();

  const location = useLocation();
  const navigate = useNavigate();

  const ingredientsList = useAppSelector(state => state.ingredients.entities);
  const loading = useAppSelector(state => state.ingredients.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  return (
    <div>
      <h2 id="ingredients-heading" data-cy="IngredientsHeading">
        Ingredients
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh list
          </Button>
          <Link to="/ingredients/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create a new Ingredients
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {ingredientsList && ingredientsList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Ingredient Name</th>
                <th>Ingredients</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {ingredientsList.map((ingredients, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/ingredients/${ingredients.id}`} color="link" size="sm">
                      {ingredients.id}
                    </Button>
                  </td>
                  <td>{ingredients.ingredientName}</td>
                  <td>
                    {ingredients.ingredients ? <Link to={`/recipes/${ingredients.ingredients.id}`}>{ingredients.ingredients.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/ingredients/${ingredients.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`/ingredients/${ingredients.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`/ingredients/${ingredients.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Ingredients found</div>
        )}
      </div>
    </div>
  );
};

export default Ingredients;
