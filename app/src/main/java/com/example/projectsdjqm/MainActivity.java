/**
 * MainActivity
 * @version 1.1
 * @author Defrim Binakaj, Muchen Li
 * @date Oct 29, 2022
 */
package com.example.projectsdjqm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.projectsdjqm.ingredient_storage.IngredientActivity;

import com.example.projectsdjqm.meal_plan.MealPlanActivity;
import com.example.projectsdjqm.recipe_list.RecipeListActivity;
import com.example.projectsdjqm.shopping_list.ShoppingListActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * MainActivity
 * Home screen of the entire application
 */
public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    FirebaseFirestore db;
    final String TAG = "Main Activity";
    public ArrayList<String> allIngredients = new ArrayList<>();
    public ArrayList<String> allRecipes = new ArrayList<>();
    public ArrayList<String> allShoppingListEntries = new ArrayList<>();
    public ArrayList<String> allMealplans = new ArrayList<>();
    public ArrayList<String> allMealplanDate = new ArrayList<String>();
    public String tmrwMealPlanRecipes = "";
    public String tmrwMealPlanIngredients = "";


    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);

        TextView ingredientTextView = (TextView) findViewById(R.id.ingredientsText);
        TextView recipeTextView = (TextView) findViewById(R.id.recipesText);
        TextView shoppinglistTextView = (TextView) findViewById(R.id.shoppinglistText);
        TextView mealplanTextView = (TextView) findViewById(R.id.mealplanText);

        TextView tomorrowRecipe = (TextView) findViewById(R.id.nextdayRecipes);
        TextView tomorrowIngredients = (TextView) findViewById(R.id.nextdayIngredients);

        
        /**
        * the following nav bar switch statement used to transition btw activities
        * // non-deprecated https://stackoverflow.com/questions/68021770/setonnavigationitemselectedlistener-deprecated
        * // vid https://www.youtube.com/watch?v=lOTIedfP1OA
        */
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_ingredient_storage:
                        startActivity(new Intent(getApplicationContext(), IngredientActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_meal_plan:
                        startActivity(new Intent(getApplicationContext(), MealPlanActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_recipe_list:
                        startActivity(new Intent(getApplicationContext(), RecipeListActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_shopping_list:
                        startActivity(new Intent(getApplicationContext(), ShoppingListActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });





        // count of items for all:
        db = FirebaseFirestore.getInstance();
        /**
         * the following source helped with displaying all data of a collection in firestore
         * // https://firebase.google.com/docs/firestore/query-data/get-data
         */


        // ingredient count

        /**
         * source for dividers:
         * // https://stackoverflow.com/questions/5049852/android-drawing-separator-divider-line-in-layout
         */

        db.collection("Ingredients")
                // .whereEqualTo("capital", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d(TAG, document.getId() + " => " + document.getData());
                                allIngredients.add(document.getId().toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        // TO GET ALL ITEMS IN STRING:
                        /*
                        String dispIngredients = "";
                        for (int i = 0; i < allIngredients.size(); i++) {
                            dispIngredients += allIngredients.get(i) + " \n";
                        }
                        ingredientTextView.setText(dispIngredients);
                         */

                        ingredientTextView.setText("Ingredients - " + String.valueOf(allIngredients.size()));

                    }
                });





        // recipe count
        db.collection("Recipes")
                // .whereEqualTo("capital", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d(TAG, document.getId() + " => " + document.getData());
                                allRecipes.add(document.getId().toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        recipeTextView.setText("Recipes - " + String.valueOf(allRecipes.size()));

                    }
                });




        // shoppinglist count
        db.collection("ShoppingLists")
                // .whereEqualTo("capital", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d(TAG, document.getId() + " => " + document.getData());
                                allShoppingListEntries.add(document.getId().toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        shoppinglistTextView.setText("Shopping List - " + String.valueOf(allShoppingListEntries.size()));

                    }
                });


        // current date inits
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String stringCurrDate = dateFormat.format(currentDate);
        Log.d(TAG,  "stringCurrDate => " + stringCurrDate);



        // mealplan count + entries for tomorrows meal plan
        db.collection("MealPlans")
                // .whereEqualTo("capital", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                allMealplans.add(document.getId().toString());
                                // Log.d(TAG, document.getId() + " ! ");

                                if (document.getId().equals(stringCurrDate)) {
                                    Log.d(TAG, "CONDITIONAL ACHIEVED");
                                    allMealplanDate.add(document.getId());
                                }

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        mealplanTextView.setText("Mealplans - " + String.valueOf(allMealplans.size()));


                    }
                });




        // tomorrow's mealplan -------------------------------------------------------------------------

        // delay this portion, to ensure that allMealplanDate is appended to first
        // https://stackoverflow.com/questions/41664409/wait-for-5-seconds
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                // ---------------------------------------
                // mealplan recipes

                // Log.d(TAG, "ALL MEAL PLANS -------------: " + allMealplanDate);
                // allMealplanDate.add("2022-11-25");

                if (allMealplanDate.size() == 0) {
                    tomorrowRecipe.setText("None");
                }

                for (int j = 0; j < allMealplanDate.size(); j++) {
                    // Log.d(TAG, "RUNNING THROUGH ------------------------: " + allMealplanDate.get(j));
                    db.collection("MealPlans" + "/" + allMealplanDate.get(j) + "/" + "recipe List")
                            // .whereEqualTo(stringCurrDate, true)
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {

                                            // Log.d(TAG, document.getId());
                                            tmrwMealPlanRecipes += document.getId() + "\n";
                                        }
                                    } else {
                                        Log.d(TAG, "Error getting documents: ", task.getException());
                                    }

                                    if (tmrwMealPlanRecipes.equals("")) {
                                        tomorrowRecipe.setText("None");
                                    }
                                    else {
                                        tomorrowRecipe.setText(tmrwMealPlanRecipes);
                                    }



                                }
                            });
                }




                Handler handler2 = new Handler();
                handler2.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // ---------------------------------------
                        // mealplan ingredients
                        if (allMealplanDate.size() == 0) {
                            tomorrowIngredients.setText("None");
                        }

                        for (int j = 0; j < allMealplanDate.size(); j++) {
                            db.collection("MealPlans" + "/" + allMealplanDate.get(j) + "/" + "ingredient List")
                                    // .whereEqualTo(stringCurrDate, true)
                                    .get()
                                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                            if (task.isSuccessful()) {
                                                for (QueryDocumentSnapshot document : task.getResult()) {

                                                    // Log.d(TAG, document.getId());
                                                    tmrwMealPlanIngredients += document.getId() + "\n";
                                                }
                                            } else {
                                                Log.d(TAG, "Error getting documents: ", task.getException());
                                            }

                                            if (tmrwMealPlanIngredients.equals("")) {
                                                tomorrowIngredients.setText("None");
                                            }
                                            else {
                                                tomorrowIngredients.setText(tmrwMealPlanIngredients);
                                            }



                                        }
                                    });
                        }
                    }
                }, 250);




            }
        }, 1200);



















    }


}
