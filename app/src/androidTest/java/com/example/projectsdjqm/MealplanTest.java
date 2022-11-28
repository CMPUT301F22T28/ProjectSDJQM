package com.example.projectsdjqm;


import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasToString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;


import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientActivity;
import com.example.projectsdjqm.meal_plan.MealPlanActivity;
import com.example.projectsdjqm.meal_plan.Mealplan;
import com.example.projectsdjqm.meal_plan.MealplanList;
import com.example.projectsdjqm.recipe_list.Recipe;
import com.example.projectsdjqm.recipe_list.RecipeList;
import com.example.projectsdjqm.recipe_list.RecipeListActivity;
import com.example.projectsdjqm.shopping_list.ShoppingListActivity;
import com.robotium.solo.Solo;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class MealplanTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MealPlanActivity> rule =
            new ActivityTestRule<>(MealPlanActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Check activity switch when pressing the bottom navigation bar
     * @return
     */

    @Test
    public void checkActivitySwitch(){
        // Asserts that the current activity is the RecipeListActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MealPlanActivity.class);
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
     * Check for adding a mealplan
     * @return
     */

    @Test
    public void check_add_mealplan() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MealPlanActivity.class );
        // try to add mealplan with date, recipeList, ingredientList and scale
        onView(withId(R.id.add_meal_plan)).perform(click());
        onView(withId(R.id.mealplan_r_add_button)).perform(click());
        onData(anything()).inAdapterView(withId(R.id.mealplan_r_storage_list)).atPosition(0).perform(click());
        onData(anything()).inAdapterView(withId(R.id.mealplan_in_storage_list)).atPosition(0).perform(click());
        onView(withId(R.id.add_storage_button)).perform(click());
        solo.waitForText("Beijing beef", 1, 2000);
        onView(isAssignableFrom(DatePicker.class)).perform(setDate(2022, 12, 3));
        solo.clickOnButton("OK");

        MealPlanActivity activity = (MealPlanActivity) solo.getCurrentActivity();

        MealplanList mealplanAdapter = activity.mealplanAdapter;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // check whether the mealplan is being added into the foodbook
        for (int i=0; i<mealplanAdapter.getCount(); i++) {
            Mealplan mealplan = mealplanAdapter.getItem(i);
            if (Objects.equals(String.format(dateFormat.format(mealplan.getMealplan_date())), "2022-12-3")) {
                assertEquals("Carrot", mealplan.getIngredientList().get(0).getIngredientDescription());
                assertEquals("Beijing beef", mealplan.getRecipeList().get(0).getTitle());
                assertEquals(Optional.of(1), mealplan.getRecipeScale().get(0));
            }
        }

        // check whether attributes of a mealplan can be viewed by user
        solo.waitForText("Beijing beef", 1, 2000);
        solo.waitForText("Carrot", 1, 2000);
        solo.waitForText("1", 1, 2000);

        solo.assertCurrentActivity("Wrong Activity", MealPlanActivity.class );


    }

    /**
     * Check for modifying recipe Scale
     * @return
     */
    @Test
    public void modify_recipe_scale() {
        solo.assertCurrentActivity("Wrong Activity", MealPlanActivity.class );

        MealPlanActivity activity = (MealPlanActivity) solo.getCurrentActivity();

        MealplanList mealplanAdapter = activity.mealplanAdapter;
        Mealplan mealplan = mealplanAdapter.getItem(0);
//        assertEquals(1, Optional.ofNullable(mealplan.getRecipeScale().get(0)));
        solo.waitForText("1", 1, 2000);
    }

}