package recipesearch;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import recipesearch.RecipeSearchController;
import se.chalmers.ait.dat215.lab2.Recipe;
import javafx.scene.image.Image;

import javax.swing.*;
import java.io.IOException;

public class RecipeListItem extends AnchorPane {


    @FXML
    protected ImageView recipeImage;

    @FXML
    protected Text recipeText;

    public RecipeListItem(Recipe recipe){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Recipe_ListItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        this.recipeImage.setImage(recipe.getFXImage());
        this.recipeText.setText(recipe.getName());


    }
}