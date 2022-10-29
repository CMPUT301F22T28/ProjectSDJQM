package com.example.projectsdjqm.meal_plan;

import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.recipe_list.Recipe;

import java.util.ArrayList;

// mealplan class
public class Mealplan {
    /*
     *  Each Mealplan entry has the following fields:
     *      recipeList
     *      ingredientList
     */


    private ArrayList<Recipe> recipeList;
    private ArrayList<Ingredient> ingredientList;


    public Mealplan (ArrayList<Recipe> recipeList,
                     ArrayList<Ingredient> ingredientList) {
        this.recipeList = recipeList;
        this.ingredientList = ingredientList;
    }

    // recipe getter
    public ArrayList<Recipe> getRecipeList() {
        return recipeList;
    }
    // recipe setter
    public void setRecipeList(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    // ingredient getter
    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }
    // ingredient setter
    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }





}
