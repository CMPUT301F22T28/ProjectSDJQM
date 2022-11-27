package com.example.projectsdjqm;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNull;

import static org.junit.Assert.assertTrue;

import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.recipe_list.Recipe;

import org.junit.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RecipeUnitTest {

    private Ingredient mockIngredient() {
        return new Ingredient(
                "orange",
                new Date(),
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

    private ArrayList<Recipe> MockRecipeList() {
        return new ArrayList<>();
    }

    @Test
    public void testGetTitle() {
        Recipe recipe = mockRecipe();
        assertEquals("Beijing Beef", recipe.getTitle());
    }

    @Test
    public void testGetPreparationTime() {
        Recipe recipe = mockRecipe();
        assertEquals(20, recipe.getPreparationTime());
    }

    @Test
    public void testGetServingSize() {
        Recipe recipe = mockRecipe();
        assertEquals(2, recipe.getNumberofServings());
    }

    @Test
    public void testGetCategory() {
        Recipe recipe = mockRecipe();
        assertEquals("meat", recipe.getRecipeCategory());
    }

    @Test
    public void testGetPhoto() {
        Recipe recipe = mockRecipe();
        assertNull(recipe.getPhotograph());
    }

    @Test
    public void testGetListOfIngredients() {
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(mockIngredient());
        Recipe recipe = new Recipe("Beijing Beef",
                20,
                2,
                "meat",
                "some comments",
                null,
                ingredientList);
        assertEquals(ingredientList, recipe.getListofIngredients());

    }

    @Test
    public void testSetTitle() {
        Recipe recipe = mockRecipe();
        assertEquals("Beijing Beef", recipe.getTitle());
        recipe.setTitle("Beef Beijing");
        assertEquals("Beef Beijing", recipe.getTitle());
    }

    @Test
    public void testSetPreparationTime() {
        Recipe recipe = mockRecipe();
        assertEquals(20, recipe.getPreparationTime());
        recipe.setPreparationTime(30);
        assertEquals(30, recipe.getPreparationTime());
    }

    @Test
    public void testSetServingNumber() {
        Recipe recipe = mockRecipe();
        assertEquals(2, recipe.getNumberofServings());
        recipe.setNumberofServings(5);
        assertEquals(5, recipe.getNumberofServings());
    }

    @Test
    public void testSetCategory() {
        Recipe recipe = mockRecipe();
        assertEquals("meat", recipe.getRecipeCategory());
        recipe.setRecipeCategory("category");
        assertEquals("category", recipe.getRecipeCategory());
    }

    @Test
    public void testSetComments() {
        Recipe recipe = mockRecipe();
        assertEquals("some comments", recipe.getComments());
        recipe.setComments("comments");
        assertEquals("comments", recipe.getComments());
    }

    @Test
    public void testSetIngredientList() {
//        Recipe recipe = mockRecipe();
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(mockIngredient());
        Recipe recipe = new Recipe(
                "Beijing Beef",
                20,
                2,
                "meat",
                "some comments",
                null,
                ingredientList);
        assertEquals(ingredientList,recipe.getListofIngredients());
        ingredientList.add(new Ingredient(
                "ing1",
                new Date(),
                Ingredient.Location.Fridge,
                1,
                "kg",
                "category1"));
        recipe.setListofIngredients(ingredientList);
        assertEquals(ingredientList, recipe.getListofIngredients());
    }

    @Test
    public void hasRecipeTest() throws ParseException {
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ingredientList.add(mockIngredient());
        Recipe recipe = new Recipe(
                "Beijing Beef",
                20,
                2,
                "meat",
                "some comments",
                null,
                ingredientList);
        ArrayList<Recipe> recipeList = MockRecipeList();
        recipeList.add(recipe);
        assertTrue(recipeList.contains(recipe));
    }
}
