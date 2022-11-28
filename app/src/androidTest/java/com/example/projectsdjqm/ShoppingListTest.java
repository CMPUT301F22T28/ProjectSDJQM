package com.example.projectsdjqm;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;

import android.app.Activity;
import android.widget.EditText;

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

import java.util.Objects;

@RunWith(AndroidJUnit4.class)
public class ShoppingListTest {
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
    public void check_pickup_ingredient(){
        // Asserts that the current activity is the RecipeListActivity.
        // Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", ShoppingListActivity.class );
        // try to click on the checkbox to see if it's prompt dialog
        solo.clickOnView(solo.getView(R.id.box));


    }

    @Test
    public void testSort() {
        solo.assertCurrentActivity("Wrong activity", RecipeListActivity.class);
        onView(withId(R.id.shopping_list_sort_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Category"))).perform(click());
        onView(withId(R.id.shopping_list_sort_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("Description"))).perform(click());
        onView(withId(R.id.shopping_list_sort_spinner)).perform(click());
    }
}
