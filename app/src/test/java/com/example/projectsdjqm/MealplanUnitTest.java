package com.example.projectsdjqm;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

import static org.junit.Assert.assertTrue;

import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.meal_plan.Mealplan;
import com.example.projectsdjqm.recipe_list.Recipe;

import org.junit.Test;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MealplanUnitTest {

    private Ingredient mockIngredient() {
        Date mealplan_date = new GregorianCalendar(2022, Calendar.DECEMBER, 3).getTime();
        return new Ingredient(
                "orange",
                mealplan_date,
                Ingredient.Location.Fridge,
                1,
                "kg",
                "fruit");
    }

    private ArrayList<Ingredient> mockIngredientLists() {
        ArrayList<Ingredient> list = new ArrayList<>();
        list.add(mockIngredient());
        return list;
    }

    private Recipe mockRecipe() {
        ArrayList<Ingredient> list = mockIngredientLists();
        return new Recipe("Beijing Beef",
                20,
                2,
                "meat",
                "some comments",
                null,
                list);
    }

    private ArrayList<Recipe> mockRecipeList() {
        ArrayList<Recipe> list = new ArrayList<>();
        list.add(mockRecipe());
        return list;
    }

    private Mealplan mockMealplan() {
        ArrayList<Ingredient> Ingredient_List = mockIngredientLists();
        ArrayList<Recipe> recipe_List = mockRecipeList();
        Date mealplan_date = new GregorianCalendar(2022, Calendar.DECEMBER, 3).getTime();
        ArrayList<Integer> recipeScale = new ArrayList<>();
        recipeScale.add(1);
        return new Mealplan(recipe_List,Ingredient_List,mealplan_date,recipeScale);
    }

    private ArrayList<Mealplan> mockMealplanList() {
        ArrayList<Mealplan> list = new ArrayList<>();
        list.add(mockMealplan());
        return new ArrayList<>();
    }

    @Test
    public void testGetRecipeList() {
        ArrayList<Ingredient> Ingredient_List = mockIngredientLists();
        ArrayList<Recipe> recipe_List = mockRecipeList();
        Date mealplan_date = new GregorianCalendar(2022, Calendar.DECEMBER, 3).getTime();
        ArrayList<Integer> recipeScale = new ArrayList<>();
        recipeScale.add(1);
        Mealplan mealplan = new Mealplan(recipe_List,Ingredient_List,mealplan_date,recipeScale);
        assertEquals(recipe_List, mealplan.getRecipeList());
    }


    @Test
    public void testGetIngredientList() {
        ArrayList<Ingredient> Ingredient_List = mockIngredientLists();
        ArrayList<Recipe> recipe_List = mockRecipeList();
        Date mealplan_date = new GregorianCalendar(2022, Calendar.DECEMBER, 3).getTime();
        ArrayList<Integer> recipeScale = new ArrayList<>();
        recipeScale.add(1);
        Mealplan mealplan = new Mealplan(recipe_List,Ingredient_List,mealplan_date,recipeScale);
        assertEquals(Ingredient_List, mealplan.getIngredientList());
    }

    @Test
    public void testGetDate() {
        Mealplan mealplan = mockMealplan();
        Date mealplan_date = new GregorianCalendar(2022, Calendar.DECEMBER, 3).getTime();
        assertEquals(mealplan_date, mealplan.getMealplan_date());
    }

    @Test
    public void testGetRecipeScale() {
        Mealplan mealplan = mockMealplan();
        ArrayList<Integer> recipeScale = new ArrayList<>();
        recipeScale.add(1);
        assertEquals(recipeScale, mealplan.getRecipeScale());
    }


    @Test
    public void testSetRecipeList() {
        ArrayList<Ingredient> Ingredient_List = mockIngredientLists();
        ArrayList<Recipe> recipe_List = mockRecipeList();
        Date mealplan_date = new GregorianCalendar(2022, Calendar.DECEMBER, 3).getTime();
        ArrayList<Integer> recipeScale = new ArrayList<>();
        recipeScale.add(1);
        Mealplan mealplan = new Mealplan(recipe_List,Ingredient_List,mealplan_date,recipeScale);
        assertEquals(recipe_List, mealplan.getRecipeList());
        Recipe recipe = new Recipe("Beijing Beef",
                20,
                2,
                "meat",
                "some comments",
                null,
                Ingredient_List);
        recipe_List.add(recipe);
        mealplan.setRecipeList(recipe_List);
        assertEquals(recipe_List, mealplan.getRecipeList());
    }

    @Test
    public void testSetIngredientList() {
        ArrayList<Ingredient> Ingredient_List = mockIngredientLists();
        ArrayList<Recipe> recipe_List = mockRecipeList();
        Date mealplan_date = new GregorianCalendar(2022, Calendar.DECEMBER, 3).getTime();
        ArrayList<Integer> recipeScale = new ArrayList<>();
        recipeScale.add(1);
        Mealplan mealplan = new Mealplan(recipe_List,Ingredient_List,mealplan_date,recipeScale);
        assertEquals(Ingredient_List, mealplan.getIngredientList());
        Ingredient ingredient = new Ingredient(
                "orange",
                new Date(),
                Ingredient.Location.Fridge,
                1,
                "kg",
                "fruit");
        Ingredient_List.add(ingredient);
        mealplan.setIngredientList(Ingredient_List);
        assertEquals(Ingredient_List, mealplan.getIngredientList());
    }

    @Test
    public void testSetDate() {
        Mealplan mealplan = mockMealplan();
        Date mealplan_date = new GregorianCalendar(2022, Calendar.DECEMBER, 3).getTime();
        assertEquals(mealplan_date, mealplan.getMealplan_date());
        Date new_mealplan_date = new GregorianCalendar(2022, Calendar.DECEMBER, 3).getTime();
        mealplan.setMealplan_date(new_mealplan_date);
        assertEquals(new_mealplan_date, mealplan.getMealplan_date());
    }

    @Test
    public void testSetRecipeScale() {
        Mealplan mealplan = mockMealplan();
        ArrayList<Integer> recipeScale = new ArrayList<>();
        recipeScale.add(1);
        assertEquals(recipeScale, mealplan.getRecipeScale());
        recipeScale.add(1);
        mealplan.setRecipeScale(recipeScale);
        assertEquals(recipeScale, mealplan.getRecipeScale());
    }

    @Test
    public void hasMealplanTest() throws ParseException {
        ArrayList<Ingredient> IngredientList = new ArrayList<>();
        IngredientList.add(mockIngredient());
        ArrayList<Recipe> recipeList = new ArrayList<>();
        recipeList.add(mockRecipe());
        Date mealplan_date = new GregorianCalendar(2022, Calendar.DECEMBER, 3).getTime();
        ArrayList<Integer> recipeScale = new ArrayList<>();
        recipeScale.add(1);
        Mealplan mealplan = new Mealplan(recipeList,IngredientList,mealplan_date,recipeScale);
        ArrayList<Mealplan> MealplanList = mockMealplanList();
        MealplanList.add(mealplan);
        assertTrue(MealplanList.contains(mealplan));
    }
}
