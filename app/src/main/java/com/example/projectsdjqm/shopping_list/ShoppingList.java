/**
 * ShoppingList
 * @version 2.1
 * @author Qingya Ye, Muchen Li
 * @date Nov 25, 2022
 * shoppinglist class
 */
package com.example.projectsdjqm.shopping_list;

import com.example.projectsdjqm.ingredient_storage.Ingredient;

public class ShoppingList {
    /*
     * Each ShoppingList class has the following:
     * An Ingredient List
     * Value to determine if ingredient has been picked up
     */

    /**
     * This variable stores ingredient which is of type {@link Ingredient}
     */
    private Ingredient ingredient;
    /**
     * This variable stores a flag indicator to track whether the ingredient is picked up
     * which is of type {@link Boolean}
     */
    private boolean pickedUp;

    /**
     * This is a constructor to create shoppingList object
     * @param ingredient it stores ingredient which is of type {@link Ingredient}
     * @param pickedUp it stores indicator to track whether the ingredient is picked up
     * which is of type {@link Boolean}
     */
    public ShoppingList(Ingredient ingredient, boolean pickedUp) {
        this.ingredient = ingredient;
        this.pickedUp = pickedUp;
    }

    /**
     * This function returns the Ingredient object
     * @return {@link #ingredient}
     */
    public Ingredient getIngredient() {return ingredient;}
    /**
     * This function returns the boolean indicator
     * @return {@link #pickedUp}
     */
    public boolean getPickedUp() {return pickedUp;}

    /**
     * This function set the Ingredient object
     */
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }
    /**
     * This function set the boolean indicator
     */
    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

}
