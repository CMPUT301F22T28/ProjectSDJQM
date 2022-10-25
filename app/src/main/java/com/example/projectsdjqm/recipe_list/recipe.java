/**
 *  Classname: Food
 *
 *  Version information: version 1
 *
 *  Date: 2022/09/23
 *
 *  Copyright notice: All rights reserved, used by permission of Muchen Li.
 */

package com.example.projectsdjqm.recipe_list;

import android.media.Image;

import com.example.projectsdjqm.ingredient_storage.ingredient;

import java.util.ArrayList;
import java.util.Date;

public class recipe {
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
    private String PreparationTime;
    private int NumberofServings;
    private String RecipeCategory;
    private String Comments;
    private Image Photograph;
    private ArrayList<ingredient> ListofIngredients;

    /* Constructor for Food */
    public recipe(String Title, String PreparationTime, int NumberofServings, String RecipeCategory,
                  String Comments, Image Photograph, ArrayList<ingredient> ListofIngredients) {
        this.Title = Title;
        this.PreparationTime = PreparationTime;
        this.NumberofServings = NumberofServings;
        this.RecipeCategory = RecipeCategory;
        this.Comments = Comments;
        this.Photograph = Photograph;
        this.ListofIngredients = ListofIngredients;

    }

    /* Getters */
    public String getTitle() {
        return Title;
    }

    public String getPreparationTime() {
        return PreparationTime;
    }

    public int getNumberofServings() {
        return NumberofServings;
    }

    public String getRecipeCategory() {
        return RecipeCategory;
    }

    public Image getPhotograph() {
        return Photograph;
    }

    public String getComments() {
        return Comments;
    }

    public ArrayList<ingredient> getListofIngredients() {
        return ListofIngredients;
    }

    /* Setters */
    public void setTitle(String title) {
        Title = title;
    }

    public void setPreparationTime(String preparationTime) {
        PreparationTime = preparationTime;
    }

    public void setNumberofServings(int numberofServings) {
        NumberofServings = numberofServings;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public void setRecipeCategory(String recipeCategory) {
        RecipeCategory = recipeCategory;
    }

    public void setPhotograph(Image photograph) {
        Photograph = photograph;
    }

    public void setListofIngredients(ArrayList<ingredient> listofIngredients) {
        ListofIngredients = listofIngredients;
    }
}
