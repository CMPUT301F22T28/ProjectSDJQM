package com.example.projectsdjqm;

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
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class showActivityTest {

    private Solo solo;
    private Solo solo1;

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);
    public ActivityTestRule<showActivity> rule1 =
            new ActivityTestRule<>(showActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        solo1 = new Solo(InstrumentationRegistry.getInstrumentation(),rule1.getActivity());
    }


    /**
     * Gets the Activity
     * @throws Exception
     */
    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
        Activity activity1 = rule1.getActivity();
    }

    @Test
    public void checkactivityswitch(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("ADD CITY"); //Click ADD CITY Button
        //Get view for EditText and enter a city name
        solo.enterText((EditText) solo.getView(R.id.editText_name), "Edmonton");
        solo.clickOnView(solo.getView(R.id.button_confirm));
        assertTrue(solo.waitForText("Edmonton", 1, 2000));
        solo.clickInList(1); //click on listview
        assertTrue(solo1.waitForText("Edmonton", 1, 2000));
        solo1.assertCurrentActivity("Wrong Activity", showActivity.class);
        //True if there is no text: Edmonton on the screen
//        assertFalse(solo.searchText("Edmonton"));
    }
    /**
     * Check item taken from the listview
     */
    @Test
    public void checkconsistency(){
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("ADD CITY"); //Click ADD CITY Button
        //Get view for EditText and enter a city name
        solo.enterText((EditText) solo.getView(R.id.editText_name), "Edmonton");
        solo.clickOnView(solo.getView(R.id.button_confirm));
        assertTrue(solo.waitForText("Edmonton", 1, 2000));
        solo.clickInList(1);
        assertTrue(solo1.waitForText("Edmonton", 1, 2000));
        solo1.assertCurrentActivity("Wrong Activity", showActivity.class);
        showActivity activity = (showActivity) solo1.getCurrentActivity();
        TextView textview = activity.textView; // Get the textview
        String city = (String) textview.getText();
        assertEquals("Edmonton", city);
    }

    @Test
    public void checkbackbutton(){
        // Asserts that the current activity is the MainActivity. Otherwise, show “Wrong Activity”
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("ADD CITY"); //Click ADD CITY Button
        //Get view for EditText and enter a city name
        solo.enterText((EditText) solo.getView(R.id.editText_name), "Edmonton");
        solo.clickOnView(solo.getView(R.id.button_confirm));
        assertTrue(solo.waitForText("Edmonton", 1, 2000));
        solo.clickInList(1); //click on listview
        solo1.clickOnButton("BACK");
        solo1.assertCurrentActivity("Wrong Activity", showActivity.class);
    }

    /**
     * Closes the activity after each test
     * @throws Exception
     */
    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }



}


