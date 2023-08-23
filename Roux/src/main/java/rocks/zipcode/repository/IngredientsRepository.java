package rocks.zipcode.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.Ingredients;

/**
 * Spring Data JPA repository for the Ingredients entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngredientsRepository extends JpaRepository<Ingredients, Long> {}
