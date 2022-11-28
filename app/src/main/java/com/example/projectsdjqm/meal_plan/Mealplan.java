package com.example.projectsdjqm.meal_plan;

import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.recipe_list.Recipe;

import java.util.ArrayList;
import java.util.Date;

/**
 * Mealplan:
 * mealplan class that has four attributes:
 * ArrayList<Recipe> recipeList,
 * ArrayList<Ingredient> ingredientList,
 * Date mealplan_date,
 * ArrayList<Integer> recipeScale
 * @author Jianming Ma
 * @date Nov.23rd, 2022
 */
public class Mealplan {
    /*
     *  Each Mealplan entry has the following fields:
     *      recipeList
     *      ingredientList
     */


    // attr init
    private ArrayList<Integer> recipeScale;
    private Date mealplan_date;
    private ArrayList<Recipe> recipeList;
    private ArrayList<Ingredient> ingredientList;


    /**
     * constructor of Mealplan class
     * @param recipeList List of recipes within a mealplan
     * @param ingredientList List of ingredients within a mealplan
     * @param mealplan_date Date of certain mealplan
     * @param recipeScale List of scales within a recipe list
     */
    public Mealplan (ArrayList<Recipe> recipeList,
                     ArrayList<Ingredient> ingredientList,
                     Date mealplan_date,
                     ArrayList<Integer> recipeScale) {
        this.recipeList = recipeList;
        this.ingredientList = ingredientList;
        this.mealplan_date = mealplan_date;
        this.recipeScale = recipeScale;
    }


    /**
     * getter (recipeList)
     * @return
     *          recipeList recipe list of current mealplan
     */
    public ArrayList<Recipe> getRecipeList() {
        return recipeList;
    }

    /**
     * setter (recipeList)
     * @param recipeList
     */
    public void setRecipeList(ArrayList<Recipe> recipeList) {
        this.recipeList = recipeList;
    }

    /**
     * getter (ingredientList)
     * @return
     *          Ingredient recipe list of current mealplan
     */
    public ArrayList<Ingredient> getIngredientList() {
        return ingredientList;
    }

    /**
     * setter (ingredientList)
     * @param ingredientList
     */
    public void setIngredientList(ArrayList<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    /**
     * getter (date)
     * @return
     *      Date of current mealplan
     */
    public Date getMealplan_date() {
        return mealplan_date;
    }

    /**
     * setter (date)
     * @param mealplan_date
     */
    public void setMealplan_date(Date mealplan_date) {
        this.mealplan_date = mealplan_date;
    }
    /**
     * getter (recipeScaleList)
     * @return
     *      recipe Scale list of current mealplan
     */
    public ArrayList<Integer> getRecipeScale() {
        return recipeScale;
    }
    /**
     * getter (recipeScaleList)
     * @param recipeScale
     */
    public void setRecipeScale(ArrayList<Integer> recipeScale) {
        this.recipeScale = recipeScale;
    }
}
