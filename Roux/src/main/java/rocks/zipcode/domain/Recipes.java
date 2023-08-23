package rocks.zipcode.domain;

import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Recipes.
 */
@Entity
@Table(name = "recipes")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Recipes implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "recipe_name")
    private String recipeName;

    @OneToOne
    @JoinColumn(unique = true)
    private CompletedMeals recipes;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Recipes id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRecipeName() {
        return this.recipeName;
    }

    public Recipes recipeName(String recipeName) {
        this.setRecipeName(recipeName);
        return this;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public CompletedMeals getRecipes() {
        return this.recipes;
    }

    public void setRecipes(CompletedMeals completedMeals) {
        this.recipes = completedMeals;
    }

    public Recipes recipes(CompletedMeals completedMeals) {
        this.setRecipes(completedMeals);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Recipes)) {
            return false;
        }
        return id != null && id.equals(((Recipes) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Recipes{" +
            "id=" + getId() +
            ", recipeName='" + getRecipeName() + "'" +
            "}";
    }
}
