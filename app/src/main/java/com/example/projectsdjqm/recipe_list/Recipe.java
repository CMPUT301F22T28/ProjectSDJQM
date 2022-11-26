package com.example.projectsdjqm.recipe_list;

import android.graphics.drawable.Drawable;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import java.util.ArrayList;

/**
 * Recipe:
 * recipe class
 * @version 1.1
 * @author Muchen Li
 * @date Oct. 25th, 2022
 */
public class Recipe {
    /*
     *  Each Recipe entry has the following fields:
     *      Title
     *      Preparation time
     *      Number of servings
     *      Recipe category
     *      Comments
     *      Photograph
     *      List of ingredients
     */
    private String Title;
    private int PreparationTime;
    private int NumberofServings;
    private String RecipeCategory;
    private String Comments;
    private Drawable Photograph;
    private ArrayList<Ingredient> listofIngredients;


    /**
     * This is a constructor to create Recipe object. packagename.classname#Recipe
     * @param Title it stores description of Recipe which is of type {@link String}
     * @param PreparationTime it stores preparation time for Recipe which is of type {@link Integer}
     * @param NumberofServings it stores serving size of Recipe which is of type {@link Integer}
     * @param RecipeCategory it stores category of Recipe which is of type {@link String}
     * @param Comments it stores comment of Recipe which is of type {@link String}
     * @param Photograph it stores photo of Recipe which is of type {@link Drawable}
     * @param listofIngredients it stores list of ingredients of Recipe which is of type
     * {@link ArrayList<Ingredient>}
     */
    public Recipe(String Title, int PreparationTime, int NumberofServings, String RecipeCategory,
                  String Comments, Drawable Photograph, ArrayList<Ingredient> listofIngredients) {
        this.Title = Title;
        this.PreparationTime = PreparationTime;
        this.NumberofServings = NumberofServings;
        this.RecipeCategory = RecipeCategory;
        this.Comments = Comments;
        this.Photograph = Photograph;
        this.listofIngredients = listofIngredients;

    }

    /**
     * This function returns the Recipe description
     * @return {@link #Title}
     */
    public String getTitle() {
        return Title;
    }

    /**
     * This function returns the Recipe preparation time
     * @return {@link #PreparationTime}
     */
    public int getPreparationTime() {
        return PreparationTime;
    }

    /**
     * This function returns the Recipe serving size
     * @return {@link #NumberofServings}
     */
    public int getNumberofServings() {
        return NumberofServings;
    }

    /**
     * This function returns the Recipe category
     * @return {@link #RecipeCategory}
     */
    public String getRecipeCategory() {
        return RecipeCategory;
    }

    /**
     * This function returns the Recipe photo
     * @return {@link #Photograph}
     */
    public Drawable getPhotograph() {
        return Photograph;
    }

    /**
     * This function returns the Recipe comments
     * @return {@link #Comments}
     */
    public String getComments() {
        return Comments;
    }

    /**
     * This function returns the ingredients of Recipe
     * @return {@link #listofIngredients}
     */
    public ArrayList<Ingredient> getListofIngredients() {
        return listofIngredients;
    }

    /**
     * This function sets the description of Recipe
     * @param title
     * This is a candidate title to set
     */
    public void setTitle(String title) {
        this.Title = title;
    }

    /**
     * This function sets the preparation time of Recipe
     * @param preparationTime
     * This is a candidate preparation time to set
     */
    public void setPreparationTime(int preparationTime) {
        this.PreparationTime = preparationTime;
    }

    /**
     * This function sets the serving size of Recipe
     * @param numberofServings
     * This is a candidate serving number to set
     */
    public void setNumberofServings(int numberofServings) {
        this.NumberofServings = numberofServings;
    }

    /**
     * This function sets the comments of Recipe
     * @param comments
     * This is a candidate comment to set
     */
    public void setComments(String comments) {
        this.Comments = comments;
    }

    /**
     * This function sets the category of Recipe
     * @param recipeCategory
     * This is a candidate category to set
     */
    public void setRecipeCategory(String recipeCategory) {

        this.RecipeCategory = recipeCategory;
    }

    /**
     * This function sets the photo of Recipe
     * @param photograph
     * This is a candidate photo to set
     */
    public void setPhotograph(Drawable photograph) {
        this.Photograph = photograph;
    }

    /**
     * This function sets the list of ingredients of Recipe
     * @param listofIngredients
     * This is a candidate list of ingredients to set
     */
    public void setListofIngredients(ArrayList<Ingredient> listofIngredients) {
        this.listofIngredients = listofIngredients;
    }
}
