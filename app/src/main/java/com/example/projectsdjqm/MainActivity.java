/**
 * MainActivity
 * @version 1.1
 * @author Defrim Binakaj & Muchen Li
 * @date Oct 29, 2022
 */
package com.example.projectsdjqm;

import android.content.Intent;
import android.os.Bundle;
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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


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


    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_main);

        TextView ingredientTextView = (TextView) findViewById(R.id.ingredientsText);
        TextView recipeTextView = (TextView) findViewById(R.id.recipesText);
        TextView shoppinglistTextView = (TextView) findViewById(R.id.shoppinglistText);
        TextView mealplanTextView = (TextView) findViewById(R.id.mealplanText);
        
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



        // mealplan count
        db.collection("MealPlans")
                // .whereEqualTo("capital", true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Log.d(TAG, document.getId() + " => " + document.getData());
                                allMealplans.add(document.getId().toString());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }

                        mealplanTextView.setText("Mealplans - " + String.valueOf(allMealplans.size()));

                    }
                });



    }


}
