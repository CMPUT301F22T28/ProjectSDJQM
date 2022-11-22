/**
 * Recipe
 * Based on project problem description
 * @version 1.1
 * @author Qingya Ye
 * @date Nov 1, 2022
 */

package com.example.projectsdjqm.recipe_list;

import android.graphics.drawable.Drawable;

import com.example.projectsdjqm.ingredient_storage.Ingredient;

import java.util.ArrayList;


/**
 * Recipe:
 * recipe class
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


    /* Constructor for Food */
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

    // getters
    public String getTitle() {
        return Title;
    }

    public int getPreparationTime() {
        return PreparationTime;
    }

    public int getNumberofServings() {
        return NumberofServings;
    }

    public String getRecipeCategory() {
        return RecipeCategory;
    }

    public Drawable getPhotograph() {
        return Photograph;
    }

    public String getComments() {
        return Comments;
    }

    public ArrayList<Ingredient> getListofIngredients() {
        return listofIngredients;
    }

    
    
    
    // setters
    public void setTitle(String title) {
        this.Title = title;
    }

    public void setPreparationTime(int preparationTime) {
        this.PreparationTime = preparationTime;
    }

    public void setNumberofServings(int numberofServings) {
        this.NumberofServings = numberofServings;
    }

    public void setComments(String comments) {
        this.Comments = comments;
    }

    public void setRecipeCategory(String recipeCategory) {

        this.RecipeCategory = recipeCategory;
    }

    public void setPhotograph(Drawable photograph) {
        this.Photograph = photograph;
    }

    public void setListofIngredients(ArrayList<Ingredient> listofIngredients) {
        this.listofIngredients = listofIngredients;
    }
}
