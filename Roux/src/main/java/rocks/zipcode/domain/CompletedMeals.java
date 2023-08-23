package rocks.zipcode.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * not an ignored comment
 */
@Schema(description = "not an ignored comment")
@Entity
@Table(name = "completed_meals")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CompletedMeals implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "prep_steps")
    private String prepSteps;

    @Column(name = "ingredient_list")
    private String ingredientList;

    @Column(name = "cooking_steps")
    private String cookingSteps;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CompletedMeals id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrepSteps() {
        return this.prepSteps;
    }

    public CompletedMeals prepSteps(String prepSteps) {
        this.setPrepSteps(prepSteps);
        return this;
    }

    public void setPrepSteps(String prepSteps) {
        this.prepSteps = prepSteps;
    }

    public String getIngredientList() {
        return this.ingredientList;
    }

    public CompletedMeals ingredientList(String ingredientList) {
        this.setIngredientList(ingredientList);
        return this;
    }

    public void setIngredientList(String ingredientList) {
        this.ingredientList = ingredientList;
    }

    public String getCookingSteps() {
        return this.cookingSteps;
    }

    public CompletedMeals cookingSteps(String cookingSteps) {
        this.setCookingSteps(cookingSteps);
        return this;
    }

    public void setCookingSteps(String cookingSteps) {
        this.cookingSteps = cookingSteps;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CompletedMeals)) {
            return false;
        }
        return id != null && id.equals(((CompletedMeals) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CompletedMeals{" +
            "id=" + getId() +
            ", prepSteps='" + getPrepSteps() + "'" +
            ", ingredientList='" + getIngredientList() + "'" +
            ", cookingSteps='" + getCookingSteps() + "'" +
            "}";
    }
}
