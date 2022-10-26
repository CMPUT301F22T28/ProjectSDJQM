/**
 *  Classname: Food
 *
 *  Version information: version 1
 *
 *  Date: 2022/09/23
 *
 *  Copyright notice: All rights reserved, used by permission of Muchen Li.
 */

package com.example.projectsdjqm.ingredient_storage;

import java.util.Date;

public class ingredient {
    /* Maximum Food Description is 30 characters */
    public static final int MAX_LENGTH_NAME = 30;

    /* Choice of Location: pantry, freezer, fridge */
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
     */
    private String Description;
    private Date BestBeforeDate;
    private Location Location;
    private int Amount;
    private int Unit;
    private String IngredientCategory;

    /* Constructor for Ingredient */
    public ingredient(String Description, Date BestBeforeDate, Location Location,
                      int Count, int Unit, String IngredientCategory) {
        this.Description = Description;
        this.BestBeforeDate = BestBeforeDate;
        this.Location = Location;
        this.Amount = Count;
        this.Unit = Unit;
        this.IngredientCategory = IngredientCategory;
    }

    /* Getters */
    public String getDescription() {
        return Description;
    }

    public Date getBestBeforeDate() {
        return BestBeforeDate;
    }

    public ingredient.Location getLocation() {
        return Location;
    }

    public int getAmount() {
        return Amount;
    }

    public int getUnit() {
        return Unit;
    }

    public String getIngredientCategory() {return IngredientCategory;}

    /* Setters */
    public void setDescription(String description) {
        Description = description;
    }

    public void setBestBeforeDate(Date bestBeforeDate) {
        BestBeforeDate = bestBeforeDate;
    }

    public void setLocation(ingredient.Location location) {
        Location = location;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public void setUnit(int unit) {
        Unit = unit;
    }

    public void setIngredientCategory(String ingredientCategory) {
        IngredientCategory = ingredientCategory;
    }
}
