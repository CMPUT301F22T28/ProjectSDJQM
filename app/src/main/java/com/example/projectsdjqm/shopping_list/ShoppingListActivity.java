/**
 * ShoppingListActivity
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
import com.example.projectsdjqm.recipe_list.RecipeFragment;
import com.example.projectsdjqm.recipe_list.RecipeListActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


import android.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;


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
    String currentSortingType;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list_main);
        db = FirebaseFirestore.getInstance();
        CollectionReference shoppinglistcollectionReference = db.collection("ShoppingLists");
        CollectionReference mealplancollectionReference = db.collection("MealPlans");
        CollectionReference ingredientcollectionReference = db.collection("Ingredients");
        CollectionReference recipecollectionReference = db.collection("Recipes");



        // bottom nav
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_shopping_list);
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

        shoppingListView = findViewById(R.id.shopping_list);

        shoppingCartList = new ArrayList<>();
        shoppingListAdapter = new ShoppingListAdapter(this, shoppingCartList);
        shoppingListView.setAdapter(shoppingListAdapter);
        boolean pickup = false;
        calendar = Calendar.getInstance();


        db.collection("ShoppingLists")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        //Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });
        shoppingListAdapter.notifyDataSetChanged();

        final FloatingActionButton addToStorageButton = findViewById(R.id.add_to_storage);
        //add checked items to ingredient storage if add button is clicked
        addToStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0; i < shoppingCartList.size(); i++) {
                    if (shoppingCartList.get(i).getPickedUp()) {

                        // add ingredient from checkedIngredientList to ingredient storage
                        Ingredient newIngredient = shoppingCartList.get(i).getIngredient();
                        CollectionReference addIngCollection = db.collection("Ingredients");
                        final String ingredientDesc = newIngredient.getIngredientDescription();
                        final String ingredientCate = newIngredient.getIngredientCategory();
                        final int ingredientAmt = newIngredient.getIngredientAmount();
                        final String ingredientUni = newIngredient.getIngredientUnit();
                        // use system date as pickup date and store it in new ingredient
                        Date pickDate = calendar.getTime();
                        newIngredient.setIngredientBestBeforeDate(pickDate);
                        final Date ingredientBestBeforeDate = newIngredient.getIngredientBestBeforeDate();
                        // by default, picked ingredient will be add to pantry
                        Ingredient.Location tempLoc = Ingredient.Location.Pantry;
                        newIngredient.setIngredientLocation(tempLoc);
                        final Ingredient.Location ingredientLoc = newIngredient.getIngredientLocation();

                        HashMap<String, Object> data = new HashMap<>();
                        data.put("Best Before Date", ingredientBestBeforeDate);
                        data.put("Amount", ingredientAmt);
                        data.put("Unit", ingredientUni);
                        data.put("Category", ingredientCate);
                        data.put("Location", ingredientLoc);
                        addIngCollection
                                .document(ingredientDesc)
                                .set(data)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d(TAG, ingredientDesc + "data has been added successfully!");
                                    }
                                });

                        // remove thi shoppingItem from shoppingCartList
                        shoppingCartList.remove(i);
                        shoppinglistcollectionReference
                                .document(ingredientDesc)
                                .delete();
                    }
                    shoppingListAdapter.notifyDataSetChanged();
                }
                new AlertDialog.Builder(ShoppingListActivity.this)
                        .setMessage("Checked Items from shopping list have been added into your" +
                                " storage! Please return to ingredient page to complete details: " +
                                "Location, Actual amount, and Unit!")
                        .setPositiveButton("Ok", null)
                        .show();
            }
        });

        //grab items from firebase shopping list
        shoppinglistcollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                shoppingCartList.clear();

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    //Log.d(TAG, String.valueOf(doc.getData().get("Category")));
                    String description = doc.getId();
                    int amount = Integer.valueOf(doc.getData().get("Amount").toString());
//                    Timestamp bbd = (Timestamp) doc.getData().get("Best Before Date");
//                    Date bestbeforedate = bbd.toDate();
                    String category = (String) doc.getData().get("Category");
//                    String location_str = String.valueOf(doc.getData().get("Location"));
//                    Ingredient.Location location;
//                    switch (location_str) {
//                        case "Fridge":
//                            location = Ingredient.Location.Fridge;
//                            break;
//                        case "Freezer":
//                            location = Ingredient.Location.Freezer;
//                            break;
//                        default:
//                            location = Ingredient.Location.Pantry;
//                    }
                    String unit = (String) doc.getData().get("Unit");

                    Ingredient addingredient = new Ingredient(description,
                            null,
                            null,
                            amount,
                            unit,
                            category);
                    shoppingCartList.add(new ShoppingList(addingredient, pickup));
                }
                shoppingListAdapter.notifyDataSetChanged();
                }
        });

        //grab items based on meal plan and ingredient storage
        //shopping list should be generated if
        //    1. meal plan exist but
        //    2. ingredient missing from storage
        mealplancollectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                //shoppingCartList.clear();

                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String recipe_path = "MealPlans"+"/"+doc.getId()+"/"+"recipe List";
                    CollectionReference collectionReference_mealplan_recipe = db.collection(recipe_path);

                    collectionReference_mealplan_recipe.
                            addSnapshotListener(new EventListener<QuerySnapshot>()
                            {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                                        FirebaseFirestoreException error) {
                                    Log.d(TAG, "meal plan" + doc.getId());
                                    for (QueryDocumentSnapshot recipedoc : queryDocumentSnapshots) {
                                        Log.d(TAG, "meal plan" + recipedoc.getId());
                                    }
                                }
                            });

                }
                //shoppingListAdapter.notifyDataSetChanged();
            }
        });

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
