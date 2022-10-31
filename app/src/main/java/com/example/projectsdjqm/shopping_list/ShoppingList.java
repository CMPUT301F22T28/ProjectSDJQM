/**
 * Classname: ShoppingList
 *
 * Version information: Version 1
 *
 * Date: 2022/09/29
 */

package com.example.projectsdjqm.shopping_list;

import com.example.projectsdjqm.ingredient_storage.Ingredient;
import java.util.ArrayList;

public class ShoppingList {
    /*
     * Each ShoppingList class has the following:
     * An Ingredient
     * Value to determine if ingredient has been picked up
     */
    private Ingredient ingredient;
    private boolean pickedUp;


    public ShoppingList(Ingredient ingredient, boolean pickedUp) {
        this.ingredient = ingredient;
        this.pickedUp = pickedUp;
    }

    /* Getters */
    public Ingredient getIngredient() {return ingredient;}

    public boolean getPickedUp() {return pickedUp;}

    /* Setters */
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }




}