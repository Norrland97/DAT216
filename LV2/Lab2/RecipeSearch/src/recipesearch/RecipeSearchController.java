
package recipesearch;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    RecipeBackendController RBC = new RecipeBackendController(null,null,null,0,0);

    @FXML
    private List<Recipe> recipeList = RBC.getRecipes();

    @FXML
    private ComboBox mainIngredient;

    @FXML
    private Pane chickenPane;

    @FXML
    private FlowPane recipeFlowPane;

    private List<RecipeListItem> RLI;


    @Override
    public void initialize(URL url, ResourceBundle rb) {

        RLI = new ArrayList<>();
        updateRecipeList();

        mainIngredient.getItems().addAll("Visa alla","Kyckling","bepa","cepa");
        mainIngredient.getSelectionModel().select(0);

        mainIngredient.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                RBC.setMainIngredient(newValue.toString());
                updateRecipeList();
                System.out.print(newValue.toString());

            }
        });


    }


    private void updateRecipeList(){

        recipeFlowPane.getChildren().clear();
        recipeList = RBC.getRecipes();

        for(int  i = 0;i<recipeList.size();i++){

            RLI.add(new RecipeListItem(recipeList.get(i)));
            recipeFlowPane.getChildren().add(RLI.get(i));
        }

    }





}