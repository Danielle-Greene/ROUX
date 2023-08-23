package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class CompletedMealsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CompletedMeals.class);
        CompletedMeals completedMeals1 = new CompletedMeals();
        completedMeals1.setId(1L);
        CompletedMeals completedMeals2 = new CompletedMeals();
        completedMeals2.setId(completedMeals1.getId());
        assertThat(completedMeals1).isEqualTo(completedMeals2);
        completedMeals2.setId(2L);
        assertThat(completedMeals1).isNotEqualTo(completedMeals2);
        completedMeals1.setId(null);
        assertThat(completedMeals1).isNotEqualTo(completedMeals2);
    }
}
