/**
 * IngredientActivity
 * @version 1.0
 * @author Muchen Li
 */
package com.example.projectsdjqm.ingredient_storage;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectsdjqm.MainActivity;
import com.example.projectsdjqm.R;
import com.example.projectsdjqm.meal_plan.MealPlanFragment;
import com.example.projectsdjqm.recipe_list.Recipe;
import com.example.projectsdjqm.recipe_list.RecipeListActivity;
import com.example.projectsdjqm.shopping_list.ShoppingListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

public class IngredientActivity extends AppCompatActivity implements
        IngredientFragment.OnFragmentInteractionListener,
        IngredientList.IngredientButtonListener {




    BottomNavigationView bottomNavigationView;

    FirebaseFirestore db;


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
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_recipe_list:
                        startActivity(new Intent(getApplicationContext(), RecipeListActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.navigation_shopping_list:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });




        // firestore stuff
        // must complete adding dependencies, etc before next line:
        // db = FirebaseFirestore.getInstance();






        ingredientlistview = findViewById(R.id.ingredient_list);

        ingredientlist = new ArrayList<>();
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
    }

}
