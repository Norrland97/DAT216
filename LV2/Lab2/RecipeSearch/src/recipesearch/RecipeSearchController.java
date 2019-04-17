
package recipesearch;

import java.net.URL;
import java.util.*;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import se.chalmers.ait.dat215.lab2.Recipe;
import se.chalmers.ait.dat215.lab2.RecipeDatabase;


public class RecipeSearchController implements Initializable {

    RecipeDatabase db = RecipeDatabase.getSharedInstance();
    RecipeBackendController RBC = new RecipeBackendController();

    /* recipe and detail panel */

    @FXML
    private AnchorPane recipeDetailPanel;

    @FXML
    private SplitPane searchPanel;

    private Map<String, RecipeListItem> recipeListItemMap = new HashMap<String, RecipeListItem>();

    /*------------------------------------*/
    /* SEARCHPANEL */

    @FXML
    private ComboBox mainIngredient;

    @FXML
    private ComboBox cousine;

    @FXML
    private FlowPane recipeFlowPane;

    @FXML
    private ToggleGroup difficultyToggleGroup;

    @FXML
    private Slider maxTimeSlider;

    @FXML
    private Spinner maxPriceSpinner;

    private List<RecipeListItem> RLI;

    @FXML
    private RadioButton all;
    @FXML
    private RadioButton easy;
    @FXML
    private RadioButton medium;
    @FXML
    private RadioButton hard;
/*-----------------------------------------------------------*/
    /* RECIPEDETAILPANEL */

    @FXML
    private Label recipeDetailLabel;

    @FXML
    private ImageView recipeDetailImage;

    @FXML
    private Button closeWindow;

    @FXML
    private ImageView cuisineImage;

    @FXML
    private Label priceLabel;

    @FXML
    private Label timeLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private Label ingredienseLabel;

    @FXML
    private Label titleLabel;

    @FXML
    private ImageView difficultyImage;

    @FXML
    private ImageView mainIngredientImage;

    @FXML
    private ImageView closeImageView;







    @Override
    public void initialize(URL url, ResourceBundle rb) {

        populateMainIngredientComboBox();
        populateCousine();
        Platform.runLater(()->mainIngredient.requestFocus());


        for (Recipe recipe : RBC.getRecipes()) {
            RecipeListItem recipeListItem = new RecipeListItem(recipe, this);
            recipeListItemMap.put(recipe.getName(), recipeListItem);
        }


        RLI = new ArrayList<>();
        updateRecipeList();

        /* RADIOBUTTONS */
        difficultyToggleGroup = new ToggleGroup();
        all.setToggleGroup(difficultyToggleGroup);
        easy.setToggleGroup(difficultyToggleGroup);
        medium.setToggleGroup(difficultyToggleGroup);
        hard.setToggleGroup(difficultyToggleGroup);
        all.setSelected(true);

        difficultyToggleGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {

            @Override
            public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {

                if (difficultyToggleGroup.getSelectedToggle() != null) {
                    RadioButton selected = (RadioButton) difficultyToggleGroup.getSelectedToggle();
                    RBC.setDifficulty(selected.getText());
                    updateRecipeList();
                }
            }
        });

        /* SPINNER */

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(10, 100, 30, 10);
        maxPriceSpinner.setValueFactory(valueFactory);
        maxPriceSpinner.valueProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                RBC.setMaxTime((int) newValue);
                updateRecipeList();
                System.out.print(newValue.toString());
            }
        });

        maxPriceSpinner.focusedProperty().addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

                if(newValue){
                    //focusgained - do nothing
                }
                else{
                    Integer value = Integer.valueOf(maxPriceSpinner.getEditor().getText());
                    RBC.setMaxPrice(value);
                    updateRecipeList();
                }

            }
        });

        /* SLIDER */

        maxTimeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                if(newValue != null && !newValue.equals(oldValue) && !maxTimeSlider.isValueChanging()) {
                    RBC.setMaxTime(newValue.intValue());
                    System.out.print(newValue.toString());
                }

            }
        });

        /* COMBOBOXES */

        mainIngredient.getItems().addAll("Visa alla","Kyckling","Kött","Fisk","Vegetarisk");
        mainIngredient.getSelectionModel().select(0);

        cousine.getItems().addAll("Visa alla","Sverige","Grekland","Indien","Asien","Afrika","Frankrike");
        cousine.getSelectionModel().select(0);

        mainIngredient.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                RBC.setMainIngredient(newValue.toString());
                updateRecipeList();
                System.out.print(newValue.toString());
            }
        });
        cousine.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                RBC.setCuisine(newValue.toString());
                updateRecipeList();
                System.out.print(newValue.toString());
            }
        });

    }

    @FXML
    public void closeRecipeView(){
        searchPanel.toFront();
    }

    public void openRecipeView(Recipe recipe){
        recipeDetailPanel.toFront();
        populateRecipeDetailView(recipe);
    }

    private void populateRecipeDetailView(Recipe recipe){

        recipeDetailImage.setImage(getSquareImage(recipeDetailImage.getImage()));
        recipeDetailLabel.setText(recipe.getInstruction());
        recipeDetailImage.setImage(recipe.getFXImage());
        cuisineImage.setImage(getCuisineImage(recipe.getCuisine()));
        priceLabel.setText(Integer.toString(recipe.getPrice()));
        timeLabel.setText(Integer.toString(recipe.getTime()));
        descriptionLabel.setText(recipe.getDescription());
        ingredienseLabel.setText(String.valueOf(recipe.getIngredients()));
        titleLabel.setText(recipe.getName());
        difficultyImage.setImage(getDifficultyImage(recipe.getDifficulty()));
        mainIngredientImage.setImage(getMainingredientImage(recipe.getMainIngredient()));
    }

    @FXML
    public void closeButtonMouseEntered(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_hover.png")));
    }

    @FXML
    public void closeButtonMousePressed(){
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close_pressed.png")));    }

    @FXML
    public void closeButtonMouseExited() {
        closeImageView.setImage(new Image(getClass().getClassLoader().getResourceAsStream(
                "RecipeSearch/resources/icon_close.png")));
    }
    @FXML
    public void mouseTrap(Event event){
        event.consume();
    }


    private void updateRecipeList(){

        recipeFlowPane.getChildren().clear();
        List<Recipe> recipeList = RBC.getRecipes();

        for(Recipe recipe: recipeList){

            RecipeListItem RLI = recipeListItemMap.get(recipe.getName());
            recipeFlowPane.getChildren().add(RLI);
        }

    }

    private void populateMainIngredientComboBox() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Kött":
                                        iconPath = "RecipeSearch/resources/icon_main_meat.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Fisk":
                                        iconPath = "RecipeSearch/resources/icon_main_fish.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Kyckling":
                                        iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Vegetarisk":
                                        iconPath = "RecipeSearch/resources/icon_main_veg.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        mainIngredient.setButtonCell(cellFactory.call(null));
        mainIngredient.setCellFactory(cellFactory);
    }

    private void populateCousine() {
        Callback<ListView<String>, ListCell<String>> cellFactory = new Callback<ListView<String>, ListCell<String>>() {

            @Override
            public ListCell<String> call(ListView<String> p) {

                return new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);

                        setText(item);

                        if (item == null || empty) {
                            setGraphic(null);
                        } else {
                            Image icon = null;
                            String iconPath;
                            try {
                                switch (item) {

                                    case "Sverige":
                                        iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Grekland":
                                        iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Indien":
                                        iconPath = "RecipeSearch/resources/icon_flag_india.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Asien":
                                        iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Afrika":
                                        iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                    case "Frankrike":
                                        iconPath = "RecipeSearch/resources/icon_flag_france.png";
                                        icon = new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
                                        break;
                                }
                            } catch (NullPointerException ex) {
                                //This should never happen in this lab but could load a default image in case of a NullPointer
                            }
                            ImageView iconImageView = new ImageView(icon);
                            iconImageView.setFitHeight(12);
                            iconImageView.setPreserveRatio(true);
                            setGraphic(iconImageView);
                        }
                    }
                };
            }
        };
        cousine.setButtonCell(cellFactory.call(null));
        cousine.setCellFactory(cellFactory);
    }

    public Image getCuisineImage(String cuisine) {
        String iconPath;
        switch (cuisine) {
            case "Sverige" :
                iconPath = "RecipeSearch/resources/icon_flag_sweden.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Grekland":
                iconPath = "RecipeSearch/resources/icon_flag_greece.png";
                System.out.print("Grekland!!");
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Indien":
                iconPath = "RecipeSearch/resources/icon_flag_india.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Asien":
                iconPath = "RecipeSearch/resources/icon_flag_asia.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Afrika":
                iconPath = "RecipeSearch/resources/icon_flag_africa.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Frankrike":
                iconPath = "RecipeSearch/resources/icon_flag_france.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));

        }
        return null ;

    }

    public Image getMainingredientImage(String main) {
        String iconPath;
        Image image = null;
        switch (main) {
            case "Kyckling" :
                iconPath = "RecipeSearch/resources/icon_main_chicken.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Kött":
                iconPath = "RecipeSearch/resources/icon_main_meat.png";
                System.out.print("Grekland!!");
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Fisk":
                iconPath = "RecipeSearch/resources/icon_main_fish.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Vegetarisk":
                iconPath = "RecipeSearch/resources/icon_main_veg.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        return null ;
    }
    public Image getDifficultyImage(String diff) {
        String iconPath;
        switch (diff) {
            case "Lätt" :
                iconPath = "RecipeSearch/resources/icon_difficulty_easy.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Mellan":
                iconPath = "RecipeSearch/resources/icon_difficulty_medium.png";
                System.out.print("Grekland!!");
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
            case "Svår":
                iconPath = "RecipeSearch/resources/icon_difficulty_hard.png";
                return new Image(getClass().getClassLoader().getResourceAsStream(iconPath));
        }
        return null ;
    }

    public Image getSquareImage(Image image){

        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;

        if(image.getWidth() > image.getHeight()){
            width = (int) image.getHeight();
            height = (int) image.getHeight();
            x = (int)(image.getWidth() - width)/2;
            y = 0;
        }

        else if(image.getHeight() > image.getWidth()){
            width = (int) image.getWidth();
            height = (int) image.getWidth();
            x = 0;
            y = (int) (image.getHeight() - height)/2;
        }

        else{
            //Width equals Height, return original image
            return image;
        }
        return new WritableImage(image.getPixelReader(), x, y, width, height);
    }


}