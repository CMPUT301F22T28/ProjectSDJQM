package com.example.projectsdjqm;

import static junit.framework.TestCase.assertEquals;

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
     * get the size of the list
     * increase the list by adding a new city
     * check if our current size matches the initial size plus
     one
     */
    @Test
    public void addIngredientTest() throws ParseException {
        list = MockIngredientList();
        int listSize = list.getCount();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        String dateInString = "2022-12-30";
        Date bbd = formatter.parse(dateInString);

        Ingredient.Location pantryloc = Ingredient.Location.valueOf("Pantry");

        list.addIngredient(new Ingredient("Egg",bbd,pantryloc,2,1,"food"));
        assertEquals(list.getCount(),listSize + 1);
    }
}
