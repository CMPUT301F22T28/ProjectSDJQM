/**
 *  Classname: Ingredient
 *  Based on project problem description
 *  @version 1.0
 *  @author Muchen Li.
 */

package com.example.projectsdjqm.ingredient_storage;

import java.util.Date;

public class Ingredient {
    /* Maximum Food Description is 30 characters,
    * might need to update this */
    public static final int MAX_LENGTH_NAME = 30;

    /* Choice of Location: pantry, freezer, fridge
    * might need to update this */
    public enum Location {
        Pantry,
        Freezer,
        Fridge
    }

    /*
     *  Each Ingredient entry has the following fields:
     *      Description
     *      Best Before Date
     *      Location
     *      Amount
     *      Unit
     *      Category
     */
    private String IngredientDescription;
    private Date IngredientBestBeforeDate;
    private Location IngredientLocation;
    private int IngredientAmount;
    private int IngredientUnit;
    private String IngredientCategory;

    /* Constructor for Ingredient */
    public Ingredient(String Description, Date BestBeforeDate, Location Location,
                      int Count, int Unit, String IngredientCategory) {
        this.IngredientDescription = Description;
        this.IngredientBestBeforeDate = BestBeforeDate;
        this.IngredientLocation = Location;
        this.IngredientAmount = Count;
        this.IngredientUnit = Unit;
        this.IngredientCategory = IngredientCategory;
    }

    /* Getters */
    public String getIngredientDescription() {
        return IngredientDescription;
    }

    public Date getIngredientBestBeforeDate() {
        return IngredientBestBeforeDate;
    }

    public Ingredient.Location getIngredientLocation() {
        return IngredientLocation;
    }

    public int getIngredientAmount() {
        return IngredientAmount;
    }

    public int getIngredientUnit() {
        return IngredientUnit;
    }

    public String getIngredientCategory() {return IngredientCategory;}

    /* Setters */
    public void setIngredientDescription(String ingredientDescription) {
        IngredientDescription = ingredientDescription;
    }

    public void setIngredientBestBeforeDate(Date ingredientBestBeforeDate) {
        IngredientBestBeforeDate = ingredientBestBeforeDate;
    }

    public void setIngredientLocation(Ingredient.Location ingredientLocation) {
        IngredientLocation = ingredientLocation;
    }

    public void setIngredientAmount(int ingredientAmount) {
        IngredientAmount = ingredientAmount;
    }

    public void setIngredientUnit(int ingredientUnit) {
        IngredientUnit = ingredientUnit;
    }

    public void setIngredientCategory(String ingredientCategory) {
        IngredientCategory = ingredientCategory;
    }
}
