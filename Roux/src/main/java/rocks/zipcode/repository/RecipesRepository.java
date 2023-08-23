package rocks.zipcode.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.Recipes;

/**
 * Spring Data JPA repository for the Recipes entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RecipesRepository extends JpaRepository<Recipes, Long> {}
