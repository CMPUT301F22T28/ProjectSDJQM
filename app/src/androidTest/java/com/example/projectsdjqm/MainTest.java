package com.example.projectsdjqm;


import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
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
import com.example.projectsdjqm.recipe_list.RecipeListActivity;
import com.example.projectsdjqm.shopping_list.ShoppingListActivity;
import com.robotium.solo.Solo;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

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
    public void check_modify_ingredient(){
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
        onView(withId(R.id.Freezer)).perform(scrollTo(),click());
        solo.enterText((EditText) solo.getView(R.id.edit_amount), "10");
        solo.enterText((EditText) solo.getView(R.id.edit_unit), "kg");
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
        assertEquals(Ingredient.Location.Freezer, newingre.getIngredientLocation());
        assertEquals(10, newingre.getIngredientAmount());
        assertEquals("kg", newingre.getIngredientUnit());

        // check whether attributes of a ingredient can be viewed by user
        solo.waitForText("frozenbroccoli", 1, 2000);
        solo.waitForText("food", 1, 2000);
        solo.waitForText("2022-12-3", 1, 2000);
        solo.waitForText("Freezer", 1, 2000);
        solo.waitForText("10", 1, 2000);
        solo.waitForText("kg", 1, 2000);

        // check whether an ingredient can be edit by a user and do the following changes to
        // certain ingredient
        solo.clickOnButton("Edit");
        solo.waitForText("frozenbroccoli", 1, 2000);
        onView(withId(R.id.edit_ingredient_desc)).perform(clearText(), typeText("broccoli"));
        onView(withId(R.id.edit_ingredient_category)).perform(clearText(), typeText("veggie"));
        onView(withId(R.id.edit_bestbeforedate_picker)).perform(scrollTo(),click());
        onView(isAssignableFrom(DatePicker.class)).perform(setDate(2022, 11, 8));
        onView(withId(R.id.Pantry)).perform(scrollTo(),click());
        onView(withId(R.id.edit_amount)).perform(scrollTo(),clearText(), typeText("12"));
        onView(withId(R.id.edit_unit)).perform(scrollTo(),clearText(), typeText("kg"));
        solo.clickOnButton("OK");
        solo.waitForText("broccoli", 1, 2000);
        Ingredient editedingre = (Ingredient) ingredientlist.getItemAtPosition(0); // Get item from first position

        // check whether the ingredient within the foodbook is edited, retrievd
        // attributes of the ingredient and verify
        assertEquals("broccoli", editedingre.getIngredientDescription());
        assertEquals("veggie", editedingre.getIngredientCategory());
        String edit_date = "Tue Nov 08 00:00:00 GMT 2022";
        LocalDateTime edit_localDate = LocalDateTime.parse(edit_date, formatter);
        long edit_timeInMilliseconds = edit_localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
        assertEquals(new Date(edit_timeInMilliseconds), editedingre.getIngredientBestBeforeDate());
        assertEquals(Ingredient.Location.Pantry, editedingre.getIngredientLocation());
        assertEquals(12, editedingre.getIngredientAmount());
        assertEquals("kg", editedingre.getIngredientUnit());

        // check whether attributes of a ingredient can be viewed by user
        solo.waitForText("broccoli", 1, 2000);
        solo.waitForText("food", 1, 2000);
        solo.waitForText("2022-11-8", 1, 2000);
        solo.waitForText("Pantry", 1, 2000);
        solo.waitForText("12", 1, 2000);
        solo.waitForText("kg", 1, 2000);

        // delete this ingredient and check for validity
        onView(withId(R.id.delete_button)).perform(click());
        assertFalse(solo.searchText("broccoli"));
        assertFalse(solo.searchText("food"));
        assertFalse(solo.searchText("2022-11-8"));
        assertFalse(solo.searchText("Pantry"));
        assertFalse(solo.searchText("12"));
        assertFalse(solo.searchText("kg"));
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class );
    }

    /**
     * Check for adding a list of ingredients and sort them based
     * on different sorting technique
     */

    @Test
    public void check_lists_and_sorting(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        // navigate to ingredient page and check whether intent is swapping correctly
        onView(withId(R.id.navigation_ingredient_storage))
                .perform(click());
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class );

        // try to add the first ingredient with description, category, bbd, location, amount and unit
        solo.clickOnView(solo.getView(R.id.add_ingredient));
        solo.enterText((EditText) solo.getView(R.id.edit_ingredient_desc), "frozenbroccoli");
        solo.enterText((EditText) solo.getView(R.id.edit_ingredient_category), "food");
        onView(withId(R.id.edit_bestbeforedate_picker)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(setDate(2022, 12, 3));
        onView(withId(R.id.Freezer)).perform(scrollTo(),click());
        solo.enterText((EditText) solo.getView(R.id.edit_amount), "10");
        solo.enterText((EditText) solo.getView(R.id.edit_unit), "kg");
        solo.clickOnButton("OK");
        // Details of the second ingredient
        solo.clickOnView(solo.getView(R.id.add_ingredient));
        solo.enterText((EditText) solo.getView(R.id.edit_ingredient_desc), "chickenthigh");
        solo.enterText((EditText) solo.getView(R.id.edit_ingredient_category), "meat");
        onView(withId(R.id.edit_bestbeforedate_picker)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(setDate(2022, 12, 15));
        onView(withId(R.id.Freezer)).perform(scrollTo(),click());
        solo.enterText((EditText) solo.getView(R.id.edit_amount), "20");
        solo.enterText((EditText) solo.getView(R.id.edit_unit), "kg");
        solo.clickOnButton("OK");
        // Details of the third ingredient
        solo.clickOnView(solo.getView(R.id.add_ingredient));
        solo.enterText((EditText) solo.getView(R.id.edit_ingredient_desc), "apple");
        solo.enterText((EditText) solo.getView(R.id.edit_ingredient_category), "fruits");
        onView(withId(R.id.edit_bestbeforedate_picker)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(setDate(2022, 11, 15));
        onView(withId(R.id.Pantry)).perform(scrollTo(),click());
        solo.enterText((EditText) solo.getView(R.id.edit_amount), "7");
        solo.enterText((EditText) solo.getView(R.id.edit_unit), "kg");
        solo.clickOnButton("OK");
        solo.waitForText("apple", 1, 2000);

        // check whether this ingredient is being added into the foodbook, retrieve
        // attribute of each added ingredient and verify
        // first ingredient
        IngredientActivity activity = (IngredientActivity)solo.getCurrentActivity();
        final ListView ingredientlist = activity.ingredientlistview; // Get the listview
        Ingredient first_ingre = (Ingredient) ingredientlist.getItemAtPosition(0); // Get item from first position
        assertEquals("frozenbroccoli", first_ingre.getIngredientDescription());
        assertEquals("food", first_ingre.getIngredientCategory());
        String date = "Sat Dec 03 00:00:00 GMT 2022";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        LocalDateTime localDate = LocalDateTime.parse(date, formatter);
        long timeInMilliseconds = localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
        assertEquals(new Date(timeInMilliseconds), first_ingre.getIngredientBestBeforeDate());
        assertEquals(Ingredient.Location.Freezer, first_ingre.getIngredientLocation());
        assertEquals(10, first_ingre.getIngredientAmount());
        assertEquals("kg", first_ingre.getIngredientUnit());
        // Second ingredient
        Ingredient second_ingre = (Ingredient) ingredientlist.getItemAtPosition(1); // Get item from Second position
        assertEquals("chickenthigh", second_ingre.getIngredientDescription());
        assertEquals("meat", second_ingre.getIngredientCategory());
        String sec_date = "Thu Dec 15 00:00:00 GMT 2022";
        DateTimeFormatter sec_formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        LocalDateTime sec_localDate = LocalDateTime.parse(sec_date, sec_formatter);
        long sec_timeInMilliseconds = sec_localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
        assertEquals(new Date(sec_timeInMilliseconds), second_ingre.getIngredientBestBeforeDate());
        assertEquals(Ingredient.Location.Freezer, second_ingre.getIngredientLocation());
        assertEquals(20, second_ingre.getIngredientAmount());
        assertEquals("kg", second_ingre.getIngredientUnit());
        // Third ingredient
        final ArrayList<Ingredient> list_ingre = activity.ingredientlist; // Get the ingredient list
        Ingredient third_ingre = list_ingre.get(2); // Get item from Third position
        assertEquals("apple", third_ingre.getIngredientDescription());
        assertEquals("fruits", third_ingre.getIngredientCategory());
        String third_date = "Tue Nov 15 00:00:00 GMT 2022";
        DateTimeFormatter third_formatter = DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH);
        LocalDateTime third_localDate = LocalDateTime.parse(third_date, third_formatter);
        long third_timeInMilliseconds = third_localDate.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli();
        assertEquals(new Date(third_timeInMilliseconds), third_ingre.getIngredientBestBeforeDate());
        assertEquals(Ingredient.Location.Pantry, third_ingre.getIngredientLocation());
        assertEquals(7, third_ingre.getIngredientAmount());
        assertEquals("kg", third_ingre.getIngredientUnit());

        // check whether this list of ingredients can be seen by the meal-planer
        // First ingredient
        solo.waitForText("frozenbroccoli", 1, 2000);
        solo.waitForText("food", 1, 2000);
        solo.waitForText("2022-12-3", 1, 2000);
        solo.waitForText("Freezer", 1, 2000);
        solo.waitForText("10", 1, 2000);
        solo.waitForText("kg", 1, 2000);
        // Second ingredient
        solo.waitForText("chickenthigh", 1, 2000);
        solo.waitForText("meat", 1, 2000);
        solo.waitForText("2022-12-15", 1, 2000);
        solo.waitForText("Freezer", 1, 2000);
        solo.waitForText("12", 1, 2000);
        solo.waitForText("kg", 1, 2000);
        // Third ingredient
        solo.waitForText("apple", 1, 2000);
        solo.waitForText("fruits", 1, 2000);
        solo.waitForText("2022-11-15", 1, 2000);
        solo.waitForText("Pantry", 1, 2000);
        solo.waitForText("7", 1, 2000);
        solo.waitForText("kg", 1, 2000);

        // start to apply sorting techniques by click on the spinner on the top right corner
        onView(withId(R.id.ingredient_sort_spinner)).perform(click());
        // perform sorting by description
        onData(allOf(is(instanceOf(String.class)), is("description"))).perform(click());
        // check whether such type is selected
        onView(withId(R.id.ingredient_sort_spinner)).check(matches(withSpinnerText(containsString("description"))));
        int index_first, index_second, index_third;
        index_first = list_ingre.indexOf(first_ingre);
        index_second = list_ingre.indexOf(second_ingre);
        index_third = list_ingre.indexOf(third_ingre);
        solo.waitForText("apple", 1, 2000);
        assertTrue(index_third < index_second);
        assertTrue(index_second < index_first);
        // perform sorting by category
        onView(withId(R.id.ingredient_sort_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("category"))).perform(click());
        // check whether such type is selected
        onView(withId(R.id.ingredient_sort_spinner)).check(matches(withSpinnerText(containsString("category"))));
        index_first = list_ingre.indexOf(first_ingre);
        index_second = list_ingre.indexOf(second_ingre);
        index_third = list_ingre.indexOf(third_ingre);
        solo.waitForText("apple", 1, 2000);
        assertTrue(index_first < index_third);
        assertTrue(index_third < index_second);
        // perform sorting by location
        onView(withId(R.id.ingredient_sort_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("location"))).perform(click());
        // check whether such type is selected
        onView(withId(R.id.ingredient_sort_spinner)).check(matches(withSpinnerText(containsString("location"))));
        index_first = list_ingre.indexOf(first_ingre);
        index_second = list_ingre.indexOf(second_ingre);
        index_third = list_ingre.indexOf(third_ingre);
        solo.waitForText("apple", 1, 2000);
        assertTrue(index_first < index_third);
        // perform sorting by bbd
        onView(withId(R.id.ingredient_sort_spinner)).perform(click());
        onData(allOf(is(instanceOf(String.class)), is("bbd"))).perform(click());
        // check whether such type is selected
        onView(withId(R.id.ingredient_sort_spinner)).check(matches(withSpinnerText(containsString("bbd"))));
        index_first = list_ingre.indexOf(first_ingre);
        index_second = list_ingre.indexOf(second_ingre);
        index_third = list_ingre.indexOf(third_ingre);
        solo.waitForText("apple", 1, 2000);
        assertTrue(index_third < index_first);
        assertTrue(index_first < index_second);


    }

    /**
     * This test aims to check and resolve conflict between firestore and espresso
     * if this test is passed, that means there is no conflict between them
     */
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


