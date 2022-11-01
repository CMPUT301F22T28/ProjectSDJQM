/**
 * Ingredient
 * Based on project problem description
 * @version 1.0
 * @author Muchen Li
 * @date Oct 24, 2022
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
    //private boolean IngredientPickedUp;
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
        //this.IngredientPickedUp = IngredientPickedUp;
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

    //public boolean getIngredientPickedUp() {return IngredientPickedUp;}

    /* Setters */
    public void setIngredientDescription(String ingredientDescription) {
        this.IngredientDescription = ingredientDescription;
    }

    public void setIngredientBestBeforeDate(Date ingredientBestBeforeDate) {
        this.IngredientBestBeforeDate = ingredientBestBeforeDate;
    }

    public void setIngredientLocation(Ingredient.Location ingredientLocation) {
        this.IngredientLocation = ingredientLocation;
    }

    public void setIngredientAmount(int ingredientAmount) {
        this.IngredientAmount = ingredientAmount;
    }

    public void setIngredientUnit(int ingredientUnit) {
        this.IngredientUnit = ingredientUnit;
    }

    public void setIngredientCategory(String ingredientCategory) {
        this.IngredientCategory = ingredientCategory;
    }

}
