package com.example.projectsdjqm;

import static junit.framework.TestCase.assertEquals;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientActivity;
import com.example.projectsdjqm.ingredient_storage.IngredientList;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IngredientListUnitTest {
    private IngredientList list;

    /**
     * create a mocklist for ingredient
     * @return
     */
    public IngredientList MockIngredientList() {
        list = new IngredientList(null, new ArrayList<>());
        return list;
    }

    /**
     * create a mock ingredient
     * add the ingredient to the list
     * check if list contains the mock ingredient
     */
    @Test
    public void hasIngredientTest() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        String dateInString = "2022-12-30";
        Date bbd = formatter.parse(dateInString);

        Ingredient.Location freezerloc = Ingredient.Location.valueOf("Freezer");
        Ingredient test = new Ingredient("strawberry",bbd,freezerloc,5,2,"fruit");

        list = MockIngredientList();
        list.addIngredient(test);
        assertTrue(list.hasIngredient(test));
    }

    /**
     * create an empty ingredient list
     * check that countIngredients returns 0
     * add 3 mock ingredients to list
     * check that countIngredient returns 3
     */
    @Test
    public void countCitiesTest() throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        String dateInString1 = "2022-12-30";
        String dateInString2 = "2022-12-20";
        String dateInString3 = "2022-12-10";

        Date bbd1 = formatter.parse(dateInString1);
        Date bbd2 = formatter.parse(dateInString2);
        Date bbd3 = formatter.parse(dateInString3);

        Ingredient.Location freezerloc = Ingredient.Location.valueOf("Freezer");
        Ingredient.Location pantryloc = Ingredient.Location.valueOf("Pantry");
        Ingredient.Location fridgeloc = Ingredient.Location.valueOf("Fridge");

        Ingredient test1 = new Ingredient("strawberry",bbd1,freezerloc,5,2,"fruit");
        Ingredient test2 = new Ingredient("potato",bbd2,pantryloc,1,3,"vegetable");
        Ingredient test3 = new Ingredient("cheesecake",bbd3,fridgeloc,2,1,"dessert");

        list = MockIngredientList();
        assertEquals(0, list.countIngredients());

        list.addIngredient(test1);
        list.addIngredient(test2);
        list.addIngredient(test3);
        assertEquals(3, list.countIngredients());
    }
}

