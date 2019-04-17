package recipesearch;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import se.chalmers.ait.dat215.lab2.Recipe;

import java.io.IOException;

public class RecipeListItem extends AnchorPane {
    private RecipeSearchController parentController;
    private Recipe recipe;


    @FXML
    private Text recipeText;

    @FXML
    private ImageView recipeImage;

    @FXML
    private ImageView mainIngredientImage;

    @FXML
    private ImageView cuisineImage;


    @FXML
    private ImageView difficultyImage;

    @FXML
    private Label timeLabel;

    @FXML
    private Label priceLabel;

    @FXML
    private Label description;


    public RecipeListItem(Recipe recipe, RecipeSearchController recipeSearchController){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("recipe_listitem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        this.recipe = recipe;
        this.parentController = recipeSearchController;

        this.recipeImage.setImage(recipe.getFXImage());
        this.recipeText.setText(recipe.getName());
        this.timeLabel.setText(Integer.toString(recipe.getTime()));
        this.priceLabel.setText(Integer.toString(this.recipe.getPrice()));
        this.mainIngredientImage.setImage(this.parentController.getMainingredientImage(recipe.getMainIngredient()));
        this.difficultyImage.setImage(this.parentController.getDifficultyImage(recipe.getDifficulty()));
        this.description.setText(recipe.getDescription());
        this.cuisineImage.setImage(this.parentController.getCuisineImage(recipe.getCuisine()));
    }
    @FXML
    protected void onClick(Event event){
        parentController.openRecipeView(this.recipe);

    }
}



