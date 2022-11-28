/**
 * Ingredient:
 * ingredient class
 * @author Muchen Li
 * @date Oct.25th, 2022
 */
package com.example.projectsdjqm.ingredient_storage;

import java.util.Date;

public class Ingredient {
    // Maximum Food Description is 30 characters
    public static final int MAX_LENGTH_NAME = 30;

    // Choice of Location: pantry, freezer, fridge
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
    private String IngredientUnit;
    private String IngredientCategory;

    /**
     * This is a constructor to create Ingredient object. packagename.classname#Ingredient
     * @param Description it stores description of Ingredient which is of type {@link String}
     * @param BestBeforeDate it stores best before date for Ingredient which is of type {@link Date}
     * @param Location it stores location of Ingredient which is of type {@link Location}
     * @param Count it stores amount of Ingredient which is of type {@link Integer}
     * @param Unit it stores unit of Ingredient which is of type {@link String}
     * @param IngredientCategory it stores category of Ingredient which is of type {@link String}
     */
    public Ingredient(String Description, Date BestBeforeDate, Location Location,
                      int Count, String Unit, String IngredientCategory) {
        this.IngredientDescription = Description;
        this.IngredientBestBeforeDate = BestBeforeDate;
        this.IngredientLocation = Location;
        this.IngredientAmount = Count;
        this.IngredientUnit = Unit;
        this.IngredientCategory = IngredientCategory;
    }

    // getters
    /**
     * This function returns the Ingredient description
     * @return {@link #IngredientDescription}
     */
    public String getIngredientDescription() {
        return IngredientDescription;
    }

    /**
     * This function returns the Ingredient best before date
     * @return {@link #IngredientBestBeforeDate}
     */
    public Date getIngredientBestBeforeDate() {
        return IngredientBestBeforeDate;
    }

    /**
     * This function returns the Ingredient storage location
     * @return {@link #IngredientLocation}
     */
    public Ingredient.Location getIngredientLocation() {
        return IngredientLocation;
    }

    /**
     * This function returns the Ingredient amount
     * @return {@link #IngredientAmount}
     */
    public int getIngredientAmount() {
        return IngredientAmount;
    }

    /**
     * This function returns the Ingredient unit
     * @return {@link #IngredientUnit}
     */
    public String getIngredientUnit() {
        return IngredientUnit;
    }

    /**
     * This function returns the Ingredient category
     * @return {@link #IngredientCategory}
     */
    public String getIngredientCategory() {return IngredientCategory;}


    // setters

    /**
     * This function sets the description of ingredient
     * @param ingredientDescription candidate description to set
     */
    public void setIngredientDescription(String ingredientDescription) {
        this.IngredientDescription = ingredientDescription;
    }

    /**
     * This function sets the best before date of ingredient
     * @param ingredientBestBeforeDate candidate best before date to set
     */
    public void setIngredientBestBeforeDate(Date ingredientBestBeforeDate) {
        this.IngredientBestBeforeDate = ingredientBestBeforeDate;
    }

    /**
     * This function sets the storage location of ingredient
     * @param ingredientLocation candidate location to set
     */
    public void setIngredientLocation(Ingredient.Location ingredientLocation) {
        this.IngredientLocation = ingredientLocation;
    }

    /**
     * This function sets the amount of ingredient
     * @param ingredientAmount candidate amount to set
     */
    public void setIngredientAmount(int ingredientAmount) {
        this.IngredientAmount = ingredientAmount;
    }

    /**
     * This function sets the unit of ingredient
     * @param ingredientUnit candidate unit to set
     */
    public void setIngredientUnit(String ingredientUnit) {
        this.IngredientUnit = ingredientUnit;
    }

    /**
     * This function sets the category of ingredient
     * @param ingredientCategory candidate category to set
     */
    public void setIngredientCategory(String ingredientCategory) {
        this.IngredientCategory = ingredientCategory;
    }

}
