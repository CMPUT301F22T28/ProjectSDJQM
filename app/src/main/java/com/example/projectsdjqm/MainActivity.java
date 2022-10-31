/**
 * MainActivity
 *
 * @version 1.1
 * @author Defrim Binakaj
 * @date Oct 29, 2022
 *
 * @version 1.0
 * @author Muchen Li
 * @date Oct 27, 2022
 */
package com.example.projectsdjqm;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.projectsdjqm.home.HomeFragment;

import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientActivity;
import com.example.projectsdjqm.ingredient_storage.IngredientFragment;
import com.example.projectsdjqm.meal_plan.MealPlanFragment;
import com.example.projectsdjqm.recipe_list.Recipe;

import com.example.projectsdjqm.recipe_list.RecipeListActivity;
import com.example.projectsdjqm.recipe_list.RecipeListFragment;
import com.example.projectsdjqm.shopping_list.ShoppingListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.projectsdjqm.databinding.ActivityMainBinding;
import com.google.android.material.navigation.NavigationBarView;

/**
 * nav bar help:
 * // non-deprecated https://stackoverflow.com/questions/68021770/setonnavigationitemselectedlistener-deprecated
 * // vid https://www.youtube.com/watch?v=lOTIedfP1OA
 */


public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;


    @Override
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
    }


}