package recipesearch.resources;

import se.chalmers.ait.dat215.lab2.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeBackendController {


    List<Recipe> recipeList;
    String cousine;
    String mainIngredient;
    String difficulty;
    int maxPrice;
    int maxTime;


    public List<Recipe> getRecipes() {

        List<Recipe> returnList = new ArrayList<Recipe>();

        int element = 5;

        if (setElementProcent(element)) return recipeList;


        //set value to recipe
        for (int i = 0; i < recipeList.size(); i++) {

            checkMatching(i, element);
        }
        //rank recipe based on value

            sortList(element, returnList);



        return returnList;
    }

    private List<Recipe> sortList(int element, List<Recipe> returnList  ) {
        for(int o = element; o>=0;o--){

            for(int i = 0;i<recipeList.size();i++){

                if(recipeList.get(i).getMatch() == (o*(100/element)) ){
                    returnList.add(recipeList.get(i));
                }
            }
        }
        return returnList;
    }

    private void checkMatching(int i, int element) {
        if (recipeList.get(i).getCuisine().equals(cousine)){
            recipeList.get(i).setMatch(recipeList.get(i).getMatch()+(100/element));
        }
        if (recipeList.get(i).getMainIngredient().equals(mainIngredient)){
            recipeList.get(i).setMatch(recipeList.get(i).getMatch()+(100/element));
        }
        if (recipeList.get(i).getDifficulty().equals(difficulty)){
            recipeList.get(i).setMatch(recipeList.get(i).getMatch()+(100/element));
        }
        if (recipeList.get(i).getPrice() < maxPrice){
            recipeList.get(i).setMatch(recipeList.get(i).getMatch()+(100/element));
        }
        if (recipeList.get(i).getTime() < maxTime){
            recipeList.get(i).setMatch(recipeList.get(i).getMatch()+(100/element));
        }
    }

    private boolean setElementProcent(int element) {
        if(cousine == null){
            element--;
        }
        if(mainIngredient == null){
            element--;
        }
        if(difficulty == null){
            element--;
        }
        if(maxPrice == 0){
            element--;
        }
        if(maxTime == 0){
            element--;
        }
        if(element == 0){
            return true;
        }
        return false;
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
