package com.example.projectsdjqm;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import android.app.Activity;

import android.widget.EditText;
import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.projectsdjqm.ingredient_storage.IngredientActivity;
import com.example.projectsdjqm.meal_plan.MealPlanActivity;
import com.example.projectsdjqm.recipe_list.Recipe;
import com.example.projectsdjqm.recipe_list.RecipeList;
import com.example.projectsdjqm.recipe_list.RecipeListActivity;
import com.example.projectsdjqm.shopping_list.ShoppingListActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Objects;

@RunWith(AndroidJUnit4.class)
public class RecipeTest {
    private Solo solo;

    @Rule
    public ActivityTestRule<RecipeListActivity> rule =
            new ActivityTestRule<>(RecipeListActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }
    /**
     * Check activity switch when pressing the bottom navigation bar
     * @return
     */

    @Test
    public void checkActivitySwitch(){
        // Asserts that the current activity is the RecipeListActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);
        // press the main page navigation bar within the recipe page to check intent shifting
        onView(withId(R.id.navigation_home))
                .perform(click());
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        // press the ingredient page navigation bar within the main page to check intent shifting
        onView(withId(R.id.navigation_ingredient_storage))
                .perform(click());
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class );
        // press the mealplan page navigation bar within the ingredient page to check intent shifting
        onView(withId(R.id.navigation_meal_plan))
                .perform(click());
        solo.assertCurrentActivity("Wrong Activity", MealPlanActivity.class);
        // press the recipe page navigation bar within the mealplan page to check intent shifting
        onView(withId(R.id.navigation_recipe_list))
                .perform(click());
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class);
        // press the shoppinglist page navigation bar within the recipe page to check intent shifting
        onView(withId(R.id.navigation_shopping_list))
                .perform(click());
        solo.assertCurrentActivity("Wrong Activity", ShoppingListActivity.class);
    }

    /**
     * Check for adding,viewing,editing and deleting an ingredient
     */

    @Test
    public void check_modify_recipe(){
        // Asserts that the current activity is the RecipeListActivity.
        // Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class );
        // try to add an ingredient with description, category, bbd, location, amount and unit
        solo.clickOnView(solo.getView(R.id.add_recipe));
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_title), "test title");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_preparation_time), "20");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_servings), "2");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_category), "test category");
        solo.enterText((EditText) solo.getView(R.id.edit_recipe_comments), "test comments");
//        solo.clickOnView(solo.getView(R.id.take_photo));
//        solo.clickOnButton("ONLY THIS TIME");
        solo.clickOnView(solo.getView(R.id.ingredient_select_button));
        solo.enterText((EditText) solo.getView(R.id.description_l), "test description");
        solo.enterText((EditText) solo.getView(R.id.count_l), "1");
        solo.enterText((EditText) solo.getView(R.id.unit_cost_l), "kg");
        solo.enterText((EditText) solo.getView(R.id.category_l), "test ingredient category");
        solo.clickOnButton("ok");
        solo.clickOnButton("OK");
        solo.waitForText("intent test",1,2000);
        RecipeListActivity activity = (RecipeListActivity) solo.getCurrentActivity();

        RecipeList recipeAdapter = activity.recipeAdapter;

//        check whether the recipe is being added into the foodbook
        for (int i=0; i<recipeAdapter.getCount(); i++) {
            Recipe recipe = recipeAdapter.getItem(i);
            if (Objects.equals(recipe.getTitle(), "test title")) {
                assertEquals(20, recipe.getPreparationTime());
                assertEquals(2, recipe.getNumberofServings());
                assertEquals("test category", recipe.getRecipeCategory());
                assertEquals("test comments", recipe.getComments());
            }
        }

        // check whether attributes of a recipe can be viewed by user
        solo.waitForText("test title",1,1000);
        solo.waitForText("20",1,1000);
        solo.waitForText("2",1,1000);
        solo.waitForText("test category", 1,1000);
        solo.waitForText("test comments", 1, 1000);

        // check whether an recipe can be edit by a user and do the following changes to
        // certain recipe
//        solo.clickOnButton("edit");
//        solo.waitForText("test recipe",1,1000);
//        onView(withId(R.id.edit_recipe_preparation_time)).perform(
//                clearText(),
//                typeText("40"));
//        onView(withId(R.id.edit_recipe_comments)).perform(
//                clearText(),
//                typeText("test edit comment"));
//        onView(withId(R.id.edit_recipe_category)).perform(
//                clearText(),
//                typeText("test edit category"));
//        onView(withId(R.id.edit_recipe_servings)).perform(
//                clearText(),
//                typeText("5"));
//        solo.clickOnButton("OK");
//        solo.waitForText("test edit comment", 1, 2000);
//
//        for (int i=0; i<recipeAdapter.getCount(); i++) {
//            Recipe recipe = recipeAdapter.getItem(i);
//            if (Objects.equals(recipe.getTitle(), "test title")) {
//                assertEquals(40, recipe.getPreparationTime());
//                assertEquals(5, recipe.getNumberofServings());
//                assertEquals("test edit category", recipe.getRecipeCategory());
//                assertEquals("test edit comments", recipe.getComments());
//            }
//        }

        // delete this recipe
//        onView(withId(R.id.recipe_delete)).perform(click());
//        assertFalse(solo.searchText("test recipe"));
//        assertFalse(solo.searchText("test category"));
//        assertFalse(solo.searchText("test comments"));
        solo.assertCurrentActivity("Wrong Activity", RecipeListActivity.class );
    }

    @Test
    public void testSort() {
        solo.assertCurrentActivity("Wrong activity", RecipeListActivity.class);
        onView(withId(R.id.recipe_sort_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Category"))).perform(click());
        onView(withId(R.id.recipe_sort_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Title"))).perform(click());
        onView(withId(R.id.recipe_sort_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Preparation Time"))).perform(click());
        onView(withId(R.id.recipe_sort_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Serving Size"))).perform(click());

    }
}
