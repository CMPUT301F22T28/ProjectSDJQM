package com.example.projectsdjqm;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.app.Activity;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientActivity;
import com.example.projectsdjqm.meal_plan.MealPlanActivity;
import com.example.projectsdjqm.meal_plan.Mealplan;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


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
     */
    @Test
    public void checkActivitySwitch(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.navigation_home))
                .perform(click());
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.navigation_ingredient_storage))
                .perform(click());
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class );
//        onView(withId(R.id.navigation_meal_plan))
//                .perform(click());
//        solo.assertCurrentActivity("Wrong Activity", MealPlanActivity.class);
//        onView(withId(R.id.navigation_recipe_list))
//                .perform(click());
//        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
//        onView(withId(R.id.navigation_shopping_list))
//                .perform(click());
    }
    /**
     * Check add an ingredient
     */

    @Test
    public void check_add_ingredient(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        onView(withId(R.id.navigation_ingredient_storage))
                .perform(click());
        solo.assertCurrentActivity("Wrong Activity", IngredientActivity.class );
        solo.clickOnView(solo.getView(R.id.add_ingredient));
        
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


