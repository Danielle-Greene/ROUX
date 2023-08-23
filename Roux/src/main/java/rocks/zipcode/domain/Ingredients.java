package rocks.zipcode.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Ingredients.
 */
@Entity
@Table(name = "ingredients")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ingredients implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "ingredient_name")
    private String ingredientName;

    @ManyToOne
    @JsonIgnoreProperties(value = { "recipes" }, allowSetters = true)
    private Recipes ingredients;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ingredients id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIngredientName() {
        return this.ingredientName;
    }

    public Ingredients ingredientName(String ingredientName) {
        this.setIngredientName(ingredientName);
        return this;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Recipes getIngredients() {
        return this.ingredients;
    }

    public void setIngredients(Recipes recipes) {
        this.ingredients = recipes;
    }

    public Ingredients ingredients(Recipes recipes) {
        this.setIngredients(recipes);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ingredients)) {
            return false;
        }
        return id != null && id.equals(((Ingredients) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ingredients{" +
            "id=" + getId() +
            ", ingredientName='" + getIngredientName() + "'" +
            "}";
    }
}
