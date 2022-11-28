package com.example.projectsdjqm;

import static org.junit.Assert.assertEquals;

import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.shopping_list.ShoppingList;


import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

public class ShoppingListUnitTest {

    // create a mock ingredient
    private Ingredient mockIngredient() {
        return new Ingredient(
        "chicken breast",
                new Date(),
                Ingredient.Location.Freezer,
                1,
                "kg",
                "meat");
    }


    @Test
    public void testGetIngredientList() {
        Ingredient ingredient = mockIngredient();
        boolean pickedUp = false;

        ShoppingList shoppingList = new ShoppingList(ingredient, pickedUp);
        assertEquals(ingredient, shoppingList.getIngredient());

    }

   
//    @Test
    public void testSetIngredientList() {
        Ingredient ingredient = mockIngredient();
        boolean pickedUp = false;

        ShoppingList shoppingList = new ShoppingList(ingredient, pickedUp);
        assertEquals(ingredient, shoppingList.getIngredient());

        Ingredient newIngredient = new Ingredient(
                "chicken breast",
                new Date(),
                Ingredient.Location.Freezer,
                1,
                "kg",
                "meat");
        assertEquals(newIngredient, shoppingList.getIngredient());

    }




}