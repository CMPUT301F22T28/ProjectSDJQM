/**
 * Recipe
 * @version 1.0
 * @author Muchen Li
 * @date Oct 24, 2022
 */

package com.example.projectsdjqm.recipe_list;

import android.media.Image;

import com.example.projectsdjqm.ingredient_storage.Ingredient;

import java.util.ArrayList;

public class Recipe {
    /*
     *  Each Recipe entry has the following fields:
     *      RecipeTitle
     *      Preparation time
     *      Number of servings
     *      Recipe category
     *      RecipeComments
     *      RecipePhotograph
     *      List of ingredients
     */
    private String RecipeTitle;
    private String RecipePreparationTime;
    private int RecipeNumberofServings;
    private String RecipeCategory;
    private String RecipeComments;
    private Image RecipePhotograph;
    private ArrayList<Ingredient> listofIngredients;

    /* Constructor for Food */
    public Recipe(String RecipeTitle, String RecipePreparationTime, int RecipeNumberofServings, String RecipeCategory,
                  String RecipeComments, Image RecipePhotograph, ArrayList<Ingredient> listofIngredients) {
        this.RecipeTitle = RecipeTitle;
        this.RecipePreparationTime = RecipePreparationTime;
        this.RecipeNumberofServings = RecipeNumberofServings;
        this.RecipeCategory = RecipeCategory;
        this.RecipeComments = RecipeComments;
        this.RecipePhotograph = RecipePhotograph;
        this.listofIngredients = listofIngredients;

    }

    /* Getters */
    public String getRecipeTitle() {
        return RecipeTitle;
    }

    public String getRecipePreparationTime() {
        return RecipePreparationTime;
    }

    public int getRecipeNumberofServings() {
        return RecipeNumberofServings;
    }

    public String getRecipeCategory() {
        return RecipeCategory;
    }

    public Image getRecipePhotograph() {
        return RecipePhotograph;
    }

    public String getRecipeComments() {
        return RecipeComments;
    }

    public ArrayList<Ingredient> getListofIngredients() {
        return listofIngredients;
    }

    /* Setters */
    public void setRecipeTitle(String recipeTitle) {
        RecipeTitle = recipeTitle;
    }

    public void setRecipePreparationTime(String recipePreparationTime) {
        RecipePreparationTime = recipePreparationTime;
    }

    public void setRecipeNumberofServings(int recipeNumberofServings) {
        RecipeNumberofServings = recipeNumberofServings;
    }

    public void setRecipeComments(String recipeComments) {
        RecipeComments = recipeComments;
    }

    public void setRecipeCategory(String recipeCategory) {
        RecipeCategory = recipeCategory;
    }

    public void setRecipePhotograph(Image recipePhotograph) {
        RecipePhotograph = recipePhotograph;
    }

    public void setListofIngredients(ArrayList<Ingredient> listofIngredients) {
        this.listofIngredients = listofIngredients;
    }
}
