/**
 * IngredientActivity
 * @version 1.2
 * @author Muchen Li & Defrim Binakaj
 * @date Oct 30, 2022
 */
package com.example.projectsdjqm.ingredient_storage;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectsdjqm.MainActivity;
import com.example.projectsdjqm.R;
import com.example.projectsdjqm.meal_plan.MealPlanActivity;
import com.example.projectsdjqm.recipe_list.RecipeListActivity;
import com.example.projectsdjqm.shopping_list.ShoppingListActivity;
import com.google.android.gms.tasks.OnSuccessListener;
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
import java.util.Date;
import java.util.HashMap;

public class IngredientActivity extends AppCompatActivity implements
        IngredientFragment.OnFragmentInteractionListener,
        IngredientList.IngredientButtonListener {

    BottomNavigationView bottomNavigationView;

    FirebaseFirestore db;
    final String TAG = "Sample";

    ListView ingredientlistview;
    IngredientList ingredientAdapter;
    ArrayList<Ingredient> ingredientlist;
    Ingredient selectedIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_main);

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

        // floating button for add Ingredient
        FloatingActionButton addIngredientButton = findViewById(R.id.add_ingredient);
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IngredientFragment addIngredientFragment = new IngredientFragment();
                addIngredientFragment.show(getSupportFragmentManager(), "ADD_INGREDIENT");
            }
        });


        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                //ingredientlist.clear();
                for (QueryDocumentSnapshot doc : value) {
                    String desc = doc.getId();
                    String BBD = (String) doc.getData().get("Best Before Date");
                    String Amt = (String) doc.getData().get("Amount");
                    String Location = (String) doc.getData().get("Location");
                    String Unit = (String) doc.getData().get("Unit");
                    String Category = (String) doc.getData().get("Category");
                    //ingredientlist.add(new Ingredient(desc, BBD,Location,Amt,Unit,Category));
                }
                ingredientAdapter.notifyDataSetChanged(); 
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
                .document(String.valueOf(selectedIngredient))
                .delete();
    }

    @Override
    public void onOkPressedAdd(Ingredient newIngredient) {
        //db = FirebaseFirestore.getInstance();
        final CollectionReference collectionReference = db.collection("Ingredients");
        final String ingredientDesc = newIngredient.getIngredientDescription().toString();
        final String ingredientCate = newIngredient.getIngredientCategory().toString();
        final String ingredientBestBeforeDate = String.valueOf(newIngredient.getIngredientBestBeforeDate());
        final String ingredientAmt = String.valueOf(newIngredient.getIngredientAmount());
        final String ingredientUni = String.valueOf(newIngredient.getIngredientUnit());
        final String ingredientLoc = newIngredient.getIngredientLocation().toString();

        HashMap<String, String> data = new HashMap<>();

        data.put("Best Before Date", String.valueOf(ingredientBestBeforeDate));
        data.put("Amount", String.valueOf(ingredientAmt));
        data.put("Unit", String.valueOf(ingredientUni));
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

    @Override
    public void onOkPressedEdit(Ingredient ingredient,
                                String description,
                                Date bestbeforedate,
                                Ingredient.Location location,
                                int amount,
                                int unit,
                                String category) {
        ingredient.setIngredientDescription(description);
        ingredient.setIngredientBestBeforeDate(bestbeforedate);
        ingredient.setIngredientLocation(location);
        ingredient.setIngredientAmount(amount);
        ingredient.setIngredientUnit(unit);
        ingredient.setIngredientCategory(category);
    }

}
