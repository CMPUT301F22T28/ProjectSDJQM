/**
 * MainActivity of the program that binds to the navigation view
 * @version 1.0
 * @author Muchen Li
 */
package com.example.projectsdjqm;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.projectsdjqm.home.HomeFragment;
import com.example.projectsdjqm.ingredient_storage.IngredientActivity;
import com.example.projectsdjqm.ingredient_storage.IngredientFragment;
import com.example.projectsdjqm.meal_plan.MealPlanFragment;
import com.example.projectsdjqm.recipe_list.Recipe;
import com.example.projectsdjqm.recipe_list.RecipeListFragment;
import com.example.projectsdjqm.shopping_list.ShoppingListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.projectsdjqm.databinding.ActivityMainBinding;


// nav bar help:
// https://www.youtube.com/watch?v=Bb8SgfI4Cm4


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.navView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.navigation_home:
                    replaceFragment(new HomeFragment());
                    break;

                case R.id.navigation_ingredient_storage:
                    replaceFragment(new IngredientFragment());
                    break;

                case R.id.navigation_recipe_list:
                    replaceFragment(new RecipeListFragment());
                    break;

                case R.id.navigation_meal_plan:
                    replaceFragment(new MealPlanFragment());
                    break;

                case R.id.navigation_shopping_list:
                    replaceFragment(new ShoppingListFragment());
                    break;

            }

            return true;
        });


    }


    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();


    }


}