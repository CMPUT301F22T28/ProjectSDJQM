/**
 * ShoppingListActivity
 *
 * @version 1.1
 * @author Muchen Li & Defrim Binakaj
 * @date Oct 30, 2022
 */
package com.example.projectsdjqm.shopping_list;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectsdjqm.MainActivity;
import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientActivity;
import com.example.projectsdjqm.ingredient_storage.IngredientList;
import com.example.projectsdjqm.meal_plan.MealPlanActivity;
import com.example.projectsdjqm.meal_plan.Mealplan;
import com.example.projectsdjqm.recipe_list.RecipeFragment;
import com.example.projectsdjqm.recipe_list.RecipeListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import android.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * ShoppingListActivity:
 * main page for shoppinglist
 */
public class ShoppingListActivity extends AppCompatActivity {

    // attr init
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;
    final String TAG = "Shopping List Activity";
    Spinner spinner;
    //    ArrayList<Ingredient> checkedIngredientList;
    ListView shoppingListView;
    ShoppingListAdapter shoppingListAdapter;
    ArrayList<ShoppingList> shoppingCartList;
    ArrayList<Ingredient> shoppingCartIngredient;
    ArrayList<Ingredient> mealPlanIngredientList;
    String currentSortingType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list_main);

        // database variables initialized
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("ShoppingLists");
        CollectionReference mealReference = db.collection("MealPlans");
        CollectionReference ingredientReference = db.collection("Ingredients");


        // bottom nav
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_shopping_list);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_ingredient_storage:
                        startActivity(new Intent(getApplicationContext(), IngredientActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_meal_plan:
                        startActivity(new Intent(getApplicationContext(), MealPlanActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_recipe_list:
                        startActivity(new Intent(getApplicationContext(), RecipeListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.navigation_shopping_list:
                        startActivity(new Intent(getApplicationContext(), ShoppingListActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });

        shoppingListView = findViewById(R.id.shopping_list);

        shoppingCartList = new ArrayList<>();
        shoppingListAdapter = new ShoppingListAdapter(this, shoppingCartList);
        shoppingListView.setAdapter(shoppingListAdapter);


        boolean pickup = false;

        // Getting information from shopping list
        // Here we are retrieving information from the Shopping List
        db.collection("ShoppingLists")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        shoppingListAdapter.notifyDataSetChanged();

        // In order to obtain information from the firestore database, need to get collection meal plan
        // Pull the date document and the ingredient list collection

        // Once the ingredient list is collected then we want to create another collection that pulls from
        // ingredient database and checks if that ingredient already exists in the storage, if it does then
        // we will remove it from the ingredient list collection

        // Creating reference to ingredients list

        final FloatingActionButton addToStorageButton = findViewById(R.id.add_to_storage);
        //add checked items to ingredient storage if add button is clicked
        addToStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < shoppingCartList.size(); i++) {
                    if (shoppingCartList.get(i).getPickedUp()) {
                        // add ingredient from checkedIngredientList to ingredient storage

                        // remove thi shoppingItem from shoppingCartList
                        shoppingCartList.remove(i);
                    }
                }
                new AlertDialog.Builder(ShoppingListActivity.this)
                        .setMessage("Checked Items from shopping list have been added into your" +
                                " storage! Please complete details: location, actual amount, and " +
                                "unit!")
                        .setPositiveButton("Ok", null)
                        .show();
            }
        });


        // pull data from database for shopping list
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                shoppingCartList.clear();
                for (QueryDocumentSnapshot doc : value) {
                    Log.d(TAG, String.valueOf(doc.getData().get("Category")));

                    String unit = (String) doc.getData().get("Unit");


                    // pulling information from meal plan database that can be put into shopping list
                    mealReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                            ArrayList<Ingredient> mealPlanIngredientList = new ArrayList<>();

                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                String ingredient_path = "MealPlans/" + doc.getId() + "/ingredient_list";
                                CollectionReference collectionReferenceMealIngredientList = db.collection(ingredient_path); // connecting to ingredient list


                                collectionReferenceMealIngredientList.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                    @Override
                                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots1, @Nullable FirebaseFirestoreException error) {
                                        Log.d(TAG, "Meal Plan" + doc.getId());

                                        // Need to grab information for meal plan list here so that it can be compared with the ingredient list
                                        for (QueryDocumentSnapshot mealPlanDoc : queryDocumentSnapshots1) {
                                            String mealPlanIngredientDescription = mealPlanDoc.getId();
                                            int mealPlanIngredientAmount = (int) mealPlanDoc.getData().get("Amount");
                                            String mealPlanIngredientCategory = (String) mealPlanDoc.getData().get("Category");
                                            Timestamp mealPlanIngredientBBD = (Timestamp) mealPlanDoc.getData().get("Best Before Date:");
                                            Date mealIngredientBestBefore = mealPlanIngredientBBD.toDate();
                                            String mealPlanIngredientStorage = (String) mealPlanDoc.getData().get("Location");
                                            String mealPlanIngredientUnit = (String) mealPlanDoc.getData().get("Unit");
                                            mealPlanIngredientList.add(new Ingredient(
                                                    mealPlanIngredientDescription,
                                                    mealIngredientBestBefore,
                                                    null,
                                                    mealPlanIngredientAmount,
                                                    mealPlanIngredientUnit,
                                                    mealPlanIngredientCategory));
                                        } // Ignoring location since getting weird error as of now

                                        /*
                                        // Pulling information from ingredient list to compare with the meal plan ingredient list
                                        ingredientReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                                                ArrayList<Ingredient> ingredientsStored = new ArrayList<>();

                                                for (QueryDocumentSnapshot ingredientDoc : queryDocumentSnapshots) {
                                                    String ingredientDescription = ingredientDoc.getId();
                                                    int ingredientAmount = (int) ingredientDoc.getData().get("Amount");
                                                    String ingredientCategory = (String) ingredientDoc.getData().get("Category");
                                                    String ingredientLocation = String.valueOf(ingredientDoc.getData().get("Location"));
                                                    Ingredient.Location loc;
                                                    switch (ingredientLocation) {
                                                        case "Fridge":
                                                            loc = Ingredient.Location.Fridge;
                                                            break;
                                                        case "Freezer":
                                                            loc = Ingredient.Location.Freezer;
                                                            break;
                                                        default:
                                                            loc = Ingredient.Location.Pantry;
                                                    }
                                                    Timestamp ingredientBBD = (Timestamp) ingredientDoc.getData().get("Best Before Date:");
                                                    Date ingredientBestBefore = ingredientBBD.toDate();

                                                    String ingredientUnit = (String) ingredientDoc.getData().get("Unit");
                                                    ingredientsStored.add(new Ingredient(
                                                            ingredientDescription,
                                                            ingredientBestBefore,
                                                            null,
                                                            ingredientAmount,
                                                            ingredientUnit,
                                                            ingredientCategory));
                                                } // Ignoring location since getting weird error as of now
                                                // Compare ingredient with mealPlan Ingredients here
                                                for (Ingredient ingredient : mealPlanIngredientList) {
                                                    int index = 0;
                                                    if (ingredient.getIngredientDescription().equals(ingredientsStored)) {
                                                        mealPlanIngredientList.remove(index);
                                                    }
                                                    index++;
                                                }

                                            }

                                        });
                                        */

                                    }
                                });
                            }
                        }
                    });
                }

                shoppingListAdapter.notifyDataSetChanged();
            }
        });


        // Sorting below
        spinner = findViewById(R.id.shopping_list_sort_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String result = parent.getItemAtPosition(i).toString();
                currentSortingType = result;
                sortShoppingList(shoppingCartList, result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void sortShoppingList(ArrayList<ShoppingList> list, String sorting_type) {
        switch (sorting_type) {
            case "Description":
                Collections.sort(list, new Comparator<ShoppingList>() {
                    @Override
                    public int compare(ShoppingList shoppinglist, ShoppingList shoppinglist1) {
                        return shoppinglist.getIngredient().getIngredientDescription()
                                .compareTo(shoppinglist1.getIngredient().getIngredientDescription());
                    }
                });
                break;
            case "Category":
                Collections.sort(list, new Comparator<ShoppingList>() {
                    @Override
                    public int compare(ShoppingList shoppinglist, ShoppingList shoppinglist1) {
                        return shoppinglist.getIngredient().getIngredientCategory()
                                .compareTo(shoppinglist1.getIngredient().getIngredientCategory());
                    }
                });
                break;
            default:
                break;
        }
        shoppingCartList = list;
        shoppingListAdapter.notifyDataSetChanged();
    }

}
