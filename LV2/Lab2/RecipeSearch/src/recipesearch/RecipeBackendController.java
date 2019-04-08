package recipesearch;

import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;
import se.chalmers.ait.dat215.lab2.SearchFilter;

import java.util.ArrayList;
import java.util.List;

public class RecipeBackendController {

    public RecipeBackendController(String cousine, String mainIngredient, String difficulty, int maxPrice, int maxTime) {
        this.cousine = cousine;
        this.mainIngredient = mainIngredient;
        this.difficulty = difficulty;
        this.maxPrice = maxPrice;
        this.maxTime = maxTime;
    }

    String cousine;
    String mainIngredient;
    String difficulty;
    int maxPrice;
    int maxTime;

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    SearchFilter searchFilter;


    public List<Recipe> getRecipes() {

        searchFilter = new SearchFilter(difficulty, maxTime, cousine, maxPrice, mainIngredient);
        return db.search(searchFilter);
    }


    public void setCuisine(String cuisine) {
        this.cousine = cuisine;
    }

    public void setMainIngredient(String mainIngredient) {
        this.mainIngredient = mainIngredient;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

}
