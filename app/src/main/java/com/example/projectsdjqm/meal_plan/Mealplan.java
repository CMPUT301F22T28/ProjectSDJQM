package com.example.projectsdjqm.meal_plan;

import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.recipe_list.Recipe;

import java.util.ArrayList;
import java.util.Date;

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
    private Date mealplan_date;
    private ArrayList<Recipe> recipeList;
    private ArrayList<Ingredient> ingredientList;


    // mealplan constructor
    public Mealplan (ArrayList<Recipe> recipeList,
                     ArrayList<Ingredient> ingredientList,
                     Date mealplan_date) {
        this.recipeList = recipeList;
        this.ingredientList = ingredientList;
        this.mealplan_date = mealplan_date;
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

    // getter (date)
    public Date getMealplan_date() {
        return mealplan_date;
    }
    // setter (date)
    public void setMealplan_date(Date mealplan_date) {
        this.mealplan_date = mealplan_date;
    }





}
