/**
 * IngredientActivity
 * @version 1.1
 * @author Muchen Li & Defrim Binakaj
 * @date Oct 30, 2022
 */
package com.example.projectsdjqm.ingredient_storage;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectsdjqm.MainActivity;
import com.example.projectsdjqm.R;
import com.example.projectsdjqm.meal_plan.MealPlanActivity;
import com.example.projectsdjqm.recipe_list.RecipeListActivity;
import com.example.projectsdjqm.shopping_list.ShoppingListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

public class IngredientActivity extends AppCompatActivity implements
        IngredientFragment.OnFragmentInteractionListener,
        IngredientList.IngredientButtonListener {




    BottomNavigationView bottomNavigationView;

    FirebaseFirestore db;

    Spinner spinner;
    ListView ingredientlistview;
    IngredientList ingredientAdapter;
    ArrayList<Ingredient> ingredientlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_main);


        // bottomnav stuff
        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_ingredient_storage);

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




        // firestore stuff
        // must complete adding dependencies, etc before next line:
        db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Ingredients");


        ingredientlistview = findViewById(R.id.ingredient_list);

        ingredientlist = new ArrayList<>();
        ingredientlist.add(new Ingredient("egg",new Date(),Ingredient.Location.Pantry,3,2,"back"));
        ingredientlist.add(new Ingredient("apple",new Date(2020,2,1),Ingredient.Location.Fridge,1,1,"here"));
        ingredientlist.add(new Ingredient("ccc",new Date(2023,5,3),Ingredient.Location.Freezer,5,4,"ccc"));
        ingredientAdapter = new IngredientList(this, ingredientlist);
        ingredientAdapter.setIngredientButtonListener(this);
        ingredientlistview.setAdapter(ingredientAdapter);

        // floating button for add Ingredient
        final FloatingActionButton addIngredientButton = findViewById(R.id.add_ingredient);
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IngredientFragment addIngredientFragment = new IngredientFragment();
                addIngredientFragment.show(getSupportFragmentManager(), "ADD_INGREDIENT");
            }
        });
/*
        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ingredientlist.clear();
                for (QueryDocumentSnapshot doc : value) {
                    String desc = doc.getId();
                    String BBD = (String) String.valueOf(doc.getData().get("Best Before Date"));
                    String Amt = (String) String.valueOf(doc.getData().get("Amount"));
                    String Location = (String) String.valueOf(doc.getData().get("Location"));
                    String Unit = (String) String.valueOf(doc.getData().get("Unit"));
                    String Category = (String) doc.getData().get("Category");
                }
                ingredientAdapter.notifyDataSetChanged();
            }
        });
*/

        // sort the ingredient list when a sorting method is selected
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String result = parent.getItemAtPosition(i).toString();
//                Toast.makeText(IngredientActivity.this, result,Toast.LENGTH_SHORT).show();
                sortIngredientList(ingredientlist, result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }

    // Edit button triggered
    public void onEditIngredientClickListener(int position) {
        Ingredient currentIngredient = ingredientlist.get(position);
        IngredientFragment addIngredientFragment = new IngredientFragment(currentIngredient);
        addIngredientFragment.show(getSupportFragmentManager(), "EDIT_INGREDIENT");
    }

    // Delete button triggered
    public void onDeleteIngredientClickListener(int position) {
        ingredientlist.remove(position);
        ingredientAdapter.notifyDataSetChanged();
    }

    @Override
    public void onOkPressedAdd(Ingredient newIngredient) {
        ingredientAdapter.add(newIngredient);
    }

    @Override
    public void onOkPressedEdit(Ingredient ingredient, String description, Date bestbeforedate, Ingredient.Location location, int amount, int unit, String category) {
        ingredient.setIngredientDescription(description);
        ingredient.setIngredientBestBeforeDate(bestbeforedate);
        ingredient.setIngredientLocation(location);
        ingredient.setIngredientAmount(amount);
        ingredient.setIngredientUnit(unit);
        ingredient.setIngredientCategory(category);
        ingredientAdapter.notifyDataSetChanged();

    }

    /*
     sort list by a certain type: description, category,
     best before date, location
     */
    public void sortIngredientList(ArrayList<Ingredient> list, String sorting_type) {
        IngredientList adapter;
        switch (sorting_type) {
            case "description":
                Collections.sort(list, new Comparator<Ingredient>() {
                    @Override
                    public int compare(Ingredient ingredient, Ingredient ingredient1) {
                        return ingredient.getIngredientDescription()
                                .compareTo(ingredient1.getIngredientDescription());
                    }
                });
                adapter = new IngredientList(this, list);
                ingredientlistview.setAdapter(adapter);
                break;
            case "category":
                Collections.sort(list, new Comparator<Ingredient>() {
                    @Override
                    public int compare(Ingredient ingredient, Ingredient ingredient1) {
                        return ingredient.getIngredientCategory()
                                .compareTo(ingredient1.getIngredientCategory());
                    }
                });
                adapter = new IngredientList(this, list);
                ingredientlistview.setAdapter(adapter);
                break;
            case "location":
                Collections.sort(list, new Comparator<Ingredient>() {
                    @Override
                    public int compare(Ingredient ingredient, Ingredient ingredient1) {
                        return ingredient.getIngredientLocation().toString()
                                .compareTo(ingredient1.getIngredientLocation().toString());
                    }
                });
                adapter = new IngredientList(this, list);
                ingredientlistview.setAdapter(adapter);
                break;
            case "bbd":
                Collections.sort(list, new Comparator<Ingredient>() {
                    @Override
                    public int compare(Ingredient ingredient, Ingredient ingredient1) {
                        long time = ingredient.getIngredientBestBeforeDate().getTime();
                        long time1 = ingredient1.getIngredientBestBeforeDate().getTime();
                        if (time <= time1) {
                            return -1;
                        } else if (time == time1) {
                            return 0;
                        } else {
                                return 1;
                        }
                    }
                });
                adapter = new IngredientList(this, list);
                ingredientlistview.setAdapter(adapter);
                break;
            default:
                break;
        }
    }

}
