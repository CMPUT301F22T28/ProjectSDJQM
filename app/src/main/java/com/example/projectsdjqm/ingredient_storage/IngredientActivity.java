/**
 * IngredientActivity
 * @version 1.2
 * @author Muchen Li & Defrim Binakaj & Qingya Ye
 * @date Nov 3, 2022
 */
package com.example.projectsdjqm.ingredient_storage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.Timestamp;
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
import java.util.HashMap;

/**
 * IngredientActivity:
 * The main page of the ingredient storage feature.
 * Grants access to other types of ingredient storage functions
 */
public class IngredientActivity extends AppCompatActivity implements
        IngredientFragment.OnFragmentInteractionListener,
        IngredientList.IngredientButtonListener {

    // all init
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;
    final String TAG = "Ingredient Activity";
    Spinner spinner;
    public ListView ingredientlistview;
    public IngredientList ingredientAdapter;
    public ArrayList<Ingredient> ingredientlist;
    Ingredient selectedIngredient;
    String currentSortingType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_main);

        Log.d(TAG, "onCreate");
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Ingredients");


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


        ingredientlistview = findViewById(R.id.ingredient_list);

        ingredientlist = new ArrayList<>();
        ingredientAdapter = new IngredientList(this, ingredientlist);
        ingredientAdapter.setIngredientButtonListener(this);
        ingredientlistview.setAdapter(ingredientAdapter);

        db.collection("Ingredients")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        // floating button for add Ingredient
        FloatingActionButton addIngredientButton = findViewById(R.id.add_ingredient);
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IngredientFragment addIngredientFragment = new IngredientFragment();
                addIngredientFragment.show(getSupportFragmentManager(), "ADD_INGREDIENT");
            }
        });

        // sort the ingredient list when a sorting method is selected
        spinner = findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String result = parent.getItemAtPosition(i).toString();
                currentSortingType = result;
//                Toast.makeText(IngredientActivity.this, result,Toast.LENGTH_SHORT).show();
                sortIngredientList(ingredientlist, result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                ingredientlist.clear();
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    Log.d(TAG, String.valueOf(doc.getData().get("Category")));
                    String description = doc.getId();
                    int amount = Integer.valueOf(doc.getData().get("Amount").toString());
                    Timestamp bbd = (Timestamp) doc.getData().get("Best Before Date");
                    Date bestbeforedate = bbd.toDate();
                    String category = (String) doc.getData().get("Category");
                    String location_str = String.valueOf(doc.getData().get("Location"));
                    Ingredient.Location location;
                    switch (location_str) {
                        case "Fridge":
                            location = Ingredient.Location.Fridge;
                            break;
                        case "Freezer":
                            location = Ingredient.Location.Freezer;
                            break;
                        default:
                            location = Ingredient.Location.Pantry;
                    }
                    int unit = Integer.valueOf(doc.getData().get("Unit").toString());

                    ingredientlist.add(new Ingredient(
                            description,
                            bestbeforedate,
                            location,
                            amount,
                            unit,
                            category));
                }
                if (currentSortingType != null) {
                    if (!currentSortingType.equals("Sort")) {
                        sortIngredientList(ingredientlist,currentSortingType);
                    } else {
                        ingredientAdapter.notifyDataSetChanged();
                    }
                } else {
                    ingredientAdapter.notifyDataSetChanged();
                }
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
        final CollectionReference collectionReference = db.collection("Ingredients");

        selectedIngredient = ingredientlist.get(position);
        ingredientlist.remove(position);
        ingredientAdapter.notifyDataSetChanged();
        collectionReference
                .document(selectedIngredient.getIngredientDescription())
                .delete();
    }

    // perform add click
    @Override
    public void onOkPressedAdd(Ingredient newIngredient) {

        final CollectionReference collectionReference = db.collection("Ingredients");
        final String ingredientDesc = newIngredient.getIngredientDescription();
        final String ingredientCate = newIngredient.getIngredientCategory();
        final Date ingredientBestBeforeDate = newIngredient.getIngredientBestBeforeDate();
        final int ingredientAmt = newIngredient.getIngredientAmount();
        final int ingredientUni = newIngredient.getIngredientUnit();
        final Ingredient.Location ingredientLoc = newIngredient.getIngredientLocation();

        HashMap<String, Object> data = new HashMap<>();

        data.put("Best Before Date", ingredientBestBeforeDate);
        data.put("Amount", ingredientAmt);
        data.put("Unit", ingredientUni);
        data.put("Category", ingredientCate);
        data.put("Location", ingredientLoc);

        collectionReference
                .document(ingredientDesc)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, ingredientDesc + "data has been added successfully!");
                    }
                });

        ingredientAdapter.add(newIngredient);

    }

    // perform edit click
    @Override
    public void onOkPressedEdit(Ingredient ingredient,
                                String description,
                                Date bestbeforedate,
                                Ingredient.Location location,
                                int amount,
                                int unit,
                                String category) {

        String oldDescription = ingredient.getIngredientDescription();
        ingredient.setIngredientDescription(description);
        ingredient.setIngredientBestBeforeDate(bestbeforedate);
        ingredient.setIngredientLocation(location);
        ingredient.setIngredientAmount(amount);
        ingredient.setIngredientUnit(unit);
        ingredient.setIngredientCategory(category);
        final CollectionReference collectionReference = db.collection("Ingredients");
        final String ingredientCate = ingredient.getIngredientCategory();
        final Date ingredientBestBeforeDate = ingredient.getIngredientBestBeforeDate();
        final int ingredientAmt = ingredient.getIngredientAmount();
        final int ingredientUni = ingredient.getIngredientUnit();
        final Ingredient.Location ingredientLoc = ingredient.getIngredientLocation();

        HashMap<String, Object> data = new HashMap<>();

        data.put("Best Before Date", ingredientBestBeforeDate);
        data.put("Amount", ingredientAmt);
        data.put("Unit", ingredientUni);
        data.put("Category", ingredientCate);
        data.put("Location", ingredientLoc);

        if (description == oldDescription) {
            collectionReference
                    .document(description)
                    .update(data);
        } else {
            collectionReference
                    .document(oldDescription)
                    .delete();
            collectionReference
                    .document(description)
                    .set(data);
        }
        ingredientAdapter.notifyDataSetChanged();

    }

    // sort list by a certain type: description, category, best before date, location
    public void sortIngredientList(ArrayList<Ingredient> list, String sorting_type) {
        switch (sorting_type) {
            case "Description":
                Collections.sort(list, new Comparator<Ingredient>() {
                    @Override
                    public int compare(Ingredient ingredient, Ingredient ingredient1) {
                        return ingredient.getIngredientDescription()
                                .compareTo(ingredient1.getIngredientDescription());
                    }
                });
                break;
            case "Category":
                Collections.sort(list, new Comparator<Ingredient>() {
                    @Override
                    public int compare(Ingredient ingredient, Ingredient ingredient1) {
                        return ingredient.getIngredientCategory()
                                .compareTo(ingredient1.getIngredientCategory());
                    }
                });
                break;
            case "Location":
                Collections.sort(list, new Comparator<Ingredient>() {
                    @Override
                    public int compare(Ingredient ingredient, Ingredient ingredient1) {
                        return ingredient.getIngredientLocation().toString()
                                .compareTo(ingredient1.getIngredientLocation().toString());
                    }
                });
                break;
            case "Best Before Date":
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
                break;
            default:
                break;
        }
        ingredientlist = list;
        ingredientAdapter.notifyDataSetChanged();
    }

}