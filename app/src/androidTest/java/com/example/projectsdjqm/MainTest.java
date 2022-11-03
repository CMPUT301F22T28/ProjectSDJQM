package com.example.projectsdjqm;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;


import android.icu.text.SimpleDateFormat;
import android.net.ParseException;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientActivity;
import com.example.projectsdjqm.ingredient_storage.IngredientList;
import com.example.projectsdjqm.meal_plan.MealPlanActivity;
import com.example.projectsdjqm.recipe_list.Recipe;
import com.example.projectsdjqm.recipe_list.RecipeListActivity;
import com.example.projectsdjqm.shopping_list.ShoppingList;
import com.example.projectsdjqm.shopping_list.ShoppingListActivity;
import com.robotium.solo.Solo;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.PickerActions.setDate;
import static androidx.test.espresso.contrib.PickerActions.setTime;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import android.widget.DatePicker;
import android.widget.TimePicker;


@RunWith(AndroidJUnit4.class)
public class MainTest {

    private Solo solo;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
    }

    /**
     * Gets the Activity
     * @throws Exception
     */
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
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        // press the main page navigation bar within the main page to check intent shifting
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
    public void check_add_ingredient(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        // navigate to ingredient page and check whether intent is swapping correctly
        onView(withId(R.id.navigation_ingredient_storage))
                .perform(click());
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class );
        // try to add an ingredient with description, category, bbd, location, amount and unit
        solo.clickOnView(solo.getView(R.id.add_ingredient));
        solo.enterText((EditText) solo.getView(R.id.edit_ingredient_desc), "frozenbroccoli");
        solo.enterText((EditText) solo.getView(R.id.edit_ingredient_category), "food");
        onView(withId(R.id.edit_bestbeforedate_picker)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(setDate(2022, 12, 3));
        solo.enterText((EditText) solo.getView(R.id.edit_amount), "10");
        solo.enterText((EditText) solo.getView(R.id.edit_unit), "5");
        solo.clickOnButton("OK");
        solo.waitForText("frozenbroccoli", 1, 2000);
        IngredientActivity activity = (IngredientActivity)solo.getCurrentActivity();
        final ListView ingredientlist = activity.ingredientlistview; // Get the listview
        Ingredient newingre = (Ingredient) ingredientlist.getItemAtPosition(0); // Get item from first position

        // check whether this ingredient is being added into the foodbook, retrieve
        // attribute of the added ingredient and verify
        assertEquals("frozenbroccoli", newingre.getIngredientDescription());
        assertEquals("food", newingre.getIngredientCategory());
        String date = "Sat Dec 03 00:00:00 GMT 2022";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        LocalDateTime localDate = LocalDateTime.parse(date, formatter);
        long timeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
        assertEquals(new Date(timeInMilliseconds), newingre.getIngredientBestBeforeDate());
        assertEquals(Ingredient.Location.Pantry, newingre.getIngredientLocation());
        assertEquals(10, newingre.getIngredientAmount());
        assertEquals(5, newingre.getIngredientUnit());

        // check whether attributes of a ingredient can be viewed by user
        solo.waitForText("frozenbroccoli", 1, 2000);
        solo.waitForText("food", 1, 2000);
        solo.waitForText("2022-12-3", 1, 2000);
        solo.waitForText("Pantry", 1, 2000);
        solo.waitForText("10", 1, 2000);
        solo.waitForText("5", 1, 2000);

        // check whether an ingredient can be edit by a user and do the following editing to
        // certain ingredient
        solo.clickOnButton("Edit");
        solo.waitForText("frozenbroccoli", 1, 2000);
        onView(withId(R.id.edit_ingredient_desc)).perform(clearText(), typeText("broccoli"));
        onView(withId(R.id.edit_ingredient_category)).perform(clearText(), typeText("veggie"));
        onView(withId(R.id.edit_bestbeforedate_picker)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(setDate(2022, 11, 8));
        onView(withId(R.id.edit_amount)).perform(clearText(), typeText("12"));
        onView(withId(R.id.edit_unit)).perform(clearText(), typeText("8"));
        solo.clickOnButton("OK");
        solo.waitForText("broccoli", 1, 2000);
        Ingredient editedingre = (Ingredient) ingredientlist.getItemAtPosition(0); // Get item from first position

        // check whether the ingredient within the foodbook is edited, retrievd
        // attributes of the ingredient and verify
        assertEquals("broccoli", editedingre.getIngredientDescription());
        assertEquals("veggie", editedingre.getIngredientCategory());
//        String edit_date = "Tues Nov 08 00:00:00 GMT 2022";
//        LocalDateTime edit_localDate = LocalDateTime.parse(edit_date, formatter);
//        long edit_timeInMilliseconds = edit_localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
//        assertEquals(new Date(timeInMilliseconds), editedingre.getIngredientBestBeforeDate());
        assertEquals(Ingredient.Location.Pantry, editedingre.getIngredientLocation());
        assertEquals(12, editedingre.getIngredientAmount());
        assertEquals(8, editedingre.getIngredientUnit());

        // check whether attributes of a ingredient can be viewed by user
        solo.waitForText("broccoli", 1, 2000);
        solo.waitForText("food", 1, 2000);
//        solo.waitForText("2022-11-8", 1, 2000);
        solo.waitForText("Pantry", 1, 2000);
        solo.waitForText("12", 1, 2000);
        solo.waitForText("8", 1, 2000);
    }

    @Test
    public void useless_test() {
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        // navigate to ingredient page and check whether intent is swapping correctly
        onView(withId(R.id.navigation_ingredient_storage))
                .perform(click());
    }
//    @Test
//    public void checkCiyListItem(){
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//        solo.clickOnButton("ADD CITY");
//        solo.enterText((EditText) solo.getView(R.id.editText_name), "Edmonton");
//        solo.clickOnButton("CONFIRM");
//        solo.waitForText("Edmonton", 1, 2000);
//        // Get MainActivity to access its variables and methods.
//        MainActivity activity = (MainActivity) solo.getCurrentActivity();
//        final ListView cityList = activity.cityList; // Get the listview
//        String city = (String) cityList.getItemAtPosition(0); // Get item from first position
//        assertEquals("Calgary", city);
//    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }




}


