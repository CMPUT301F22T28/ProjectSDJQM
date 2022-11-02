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
     * An Ingredient List
     * Value to determine if ingredient has been picked up
     */
//    private ArrayList<Ingredient> listOfIngredients;
//    private boolean pickedUp;


//    public ShoppingList(ArrayList<Ingredient> listOfIngredients, boolean pickedUp) {
//        this.listOfIngredients = listOfIngredients;


    private Ingredient ingredient;
    private boolean pickedUp;
    public ShoppingList(Ingredient ingredient, boolean pickedUp) {
        this.ingredient = ingredient;
        this.pickedUp = pickedUp;
    }

    /* Getters */
//    public ArrayList<Ingredient> getListOfIngredients() {return listOfIngredients;}
    public Ingredient getIngredient() {return ingredient;}
    public boolean getPickedUp() {return pickedUp;}

    /* Setters */
//    public void setListOfIngredients(ArrayList<Ingredient> listOfIngredients) {
//        this.listOfIngredients = listOfIngredients;
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public void setPickedUp(boolean pickedUp) {
        this.pickedUp = pickedUp;
    }

}
