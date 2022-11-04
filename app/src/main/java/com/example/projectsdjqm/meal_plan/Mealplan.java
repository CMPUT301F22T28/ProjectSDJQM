package com.example.projectsdjqm.meal_plan;

import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.recipe_list.Recipe;

import java.util.ArrayList;

/**
 * Mealplan:
 * mealplan class
 */
public class Mealplan {
    /*
     *  Each Mealplan entry has the following fields:
     *      recipeList
     *      ingredientList
     */


    // attr init
    private ArrayList<Recipe> recipeList;
    private ArrayList<Ingredient> ingredientList;


    // mealplan constructor
    public Mealplan (ArrayList<Recipe> recipeList,
                     ArrayList<Ingredient> ingredientList) {
        this.recipeList = recipeList;
        this.ingredientList = ingredientList;
    }

    // getter (recipe)
    public ArrayList<Recipe> getRecipeList() {
        return recipeList;
    }
    // setter (recipe)
    public void setRecipeList(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    // getter (ingredient)
    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }
    // setter (ingredient)
    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }





}
