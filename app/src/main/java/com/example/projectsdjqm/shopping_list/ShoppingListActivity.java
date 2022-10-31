package com.example.projectsdjqm.shopping_list;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.projectsdjqm.MainActivity;
import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientActivity;
import com.example.projectsdjqm.ingredient_storage.IngredientFragment;
import com.example.projectsdjqm.ingredient_storage.IngredientList;
import com.example.projectsdjqm.recipe_list.RecipeListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class ShoppingListActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
//    ArrayList<Ingredient> checkedIngredientList;
    ListView shoppingListView;
    ShoppingListAdapter shoppingListAdapter;
    ArrayList<ShoppingList> shoppingCartList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shopping_list_main);

//        checkedIngredientList = new ArrayList<>();
        // bottomnav stuff
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
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
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
//        ingredientAdapter.setIngredientButtonListener(this);
        shoppingListView.setAdapter(shoppingListAdapter);

        final FloatingActionButton addToStorageButton = findViewById(R.id.add_to_storage);

        /*
           add checked items to ingredient storage if add button is clicked
         */
        addToStorageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i=0; i < shoppingCartList.size(); i++) {
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
    }
}