package rocks.zipcode.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import rocks.zipcode.web.rest.TestUtil;

class RecipesTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Recipes.class);
        Recipes recipes1 = new Recipes();
        recipes1.setId(1L);
        Recipes recipes2 = new Recipes();
        recipes2.setId(recipes1.getId());
        assertThat(recipes1).isEqualTo(recipes2);
        recipes2.setId(2L);
        assertThat(recipes1).isNotEqualTo(recipes2);
        recipes1.setId(null);
        assertThat(recipes1).isNotEqualTo(recipes2);
    }
}
