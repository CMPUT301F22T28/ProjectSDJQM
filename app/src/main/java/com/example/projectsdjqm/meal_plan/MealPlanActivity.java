/**
 * MealPlanActivity
 * @version 1.1
 * @author Muchen Li & Defrim Binakaj
 * @date Oct 30, 2022
 */
package com.example.projectsdjqm.meal_plan;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.projectsdjqm.MainActivity;
import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientActivity;
import com.example.projectsdjqm.recipe_list.Recipe;
import com.example.projectsdjqm.recipe_list.RecipeListActivity;
import com.example.projectsdjqm.shopping_list.ShoppingListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;

/**
 * MealPlanActivity:
 * Main page of the meal plan
 */
public class MealPlanActivity extends AppCompatActivity
        implements MealplanFragment.OnFragmentInteractionListener,
                    MealplanStorageFragment.DataPassListener{

    
    // bottom nav
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;
    final String TAG = "Recipes Activity";
    ListView mealplanListView;
    ArrayAdapter<Mealplan> mealplanAdapter;
    ArrayList<Mealplan> mealplanList;
    Recipe selectedMealplan;
    MealplanFragment addMealplanFragment = new MealplanFragment();
    MealplanStorageFragment mealplanStorageFragment = new MealplanStorageFragment();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealplan_main);


        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_meal_plan);


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

        mealplanListView = findViewById(R.id.meal_plan_list);
        mealplanList = new ArrayList<Mealplan>();

        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        Ingredient testc = new Ingredient("apple",new Date(2020,2,1),Ingredient.Location.Fridge,1,1,"here");
        ingredientList.add(testc);
        ArrayList<Recipe> recipeList = new ArrayList<>();
        Drawable icon = ContextCompat.getDrawable(this, R.drawable.ic_notifications_black_24dp);
        Recipe testa = new Recipe("Orange Chicken", "30", 3,
                "category", "comments",icon,
                ingredientList);
        recipeList.add(testa);
        Mealplan testb = new Mealplan(recipeList, ingredientList, new Date(2022,11,30));
        mealplanList.add(testb);

        mealplanAdapter = new MealplanList(this, mealplanList);
        mealplanListView.setAdapter(mealplanAdapter);

        final FloatingActionButton addButton = findViewById(R.id.add_meal_plan);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addMealplanFragment.show(getSupportFragmentManager(),"Add Mealplan");
            }
        });

    }


    public void onOkPressedAdd(Mealplan mealplan) {
        mealplanAdapter.add(mealplan);
        mealplanListView.setAdapter(mealplanAdapter);
    }

    public void add_meal_plan_from_storage() {
        mealplanStorageFragment.show(getSupportFragmentManager(),"Mealplan Storage");

    }


    public void passData(String data) {

    }


}
