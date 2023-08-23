package rocks.zipcode.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import rocks.zipcode.domain.CompletedMeals;

/**
 * Spring Data JPA repository for the CompletedMeals entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CompletedMealsRepository extends JpaRepository<CompletedMeals, Long> {}
