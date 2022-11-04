/**
 * RecipeListActivity
 * @version 1.1
 * @author Muchen Li & Defrim Binakaj
 * @date Oct 30, 2022
 */
package com.example.projectsdjqm.recipe_list;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.projectsdjqm.MainActivity;
import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientActivity;
import com.example.projectsdjqm.ingredient_storage.IngredientFragment;
import com.example.projectsdjqm.ingredient_storage.IngredientList;
import com.example.projectsdjqm.meal_plan.MealPlanActivity;
import com.example.projectsdjqm.shopping_list.ShoppingListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class RecipeListActivity extends AppCompatActivity
        implements RecipeList.RecipeButtonListener,
        RecipeFragment.OnFragmentInteractionListener{

    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;
    final String TAG = "Recipes Activity";
    ListView recipeListView;
    RecipeList recipeAdapter;
    public ArrayList<Recipe> recipeList;
    Recipe selectedRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_main);
//        View view1 = LayoutInflater.from(this).inflate(R.layout.recipe_content, null);

        Log.d(TAG, "onCreate");
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Recipes");

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_recipe_list);

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

        recipeListView = findViewById(R.id.recipe_list);
        recipeList = new ArrayList<>();
        ArrayList<Ingredient> ingredientlist = new ArrayList<>();
        //ingredientlist.add(new Ingredient("egg",new Date(),Ingredient.Location.Pantry,3,2,"back"));
        //ingredientlist.add(new Ingredient("apple",new Date(2020,2,1),Ingredient.Location.Fridge,1,1,"here"));
        //ingredientlist.add(new Ingredient("ccc",new Date(2023,5,3),Ingredient.Location.Freezer,5,4,"ccc"));

        //Drawable icon = ContextCompat.getDrawable(this, R.drawable.ic_notifications_black_24dp);
        //Recipe testa = new Recipe("Orange Chicken", "30", 3,
        //        "category", "comments",icon,
        //        ingredientlist);
        //recipeList.add(testa);
        recipeAdapter = new RecipeList(this, recipeList);
        recipeAdapter.setRecipeButtonListener(this);
        recipeListView.setAdapter(recipeAdapter);

        db.collection("Recipes")
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

        final FloatingActionButton addButton = findViewById(R.id.add_recipe);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeFragment addRecipeFragment = new RecipeFragment();
                addRecipeFragment.show(getSupportFragmentManager(),"Add Recipe");
            }
        });
    }

    public void onEditRecipeClickListener(int position) {
        Recipe currentRecipe = recipeList.get(position);
        RecipeFragment addRecipeFragment = new RecipeFragment(currentRecipe);
        addRecipeFragment.show(getSupportFragmentManager(), "Edit Recipe");
    }

    // Delete button triggered
    public void onDeleteRecipeClickListener(int position) {
        final CollectionReference collectionReference = db.collection("Recipes");

        selectedRecipe = recipeList.get(position);
        recipeList.remove(position);
        recipeAdapter.notifyDataSetChanged();
        collectionReference
                .document(selectedRecipe.getTitle())
                .delete();
    }

    @Override
    public void onOkPressedAdd(Recipe recipe) {

        final CollectionReference collectionReference = db.collection("Recipes");
        final String recipeTitle = recipe.getTitle();
        final String recipePreparationTime = recipe.getPreparationTime();
        final int recipeServingNumber = recipe.getNumberofServings();
        final String recipeCategory = recipe.getRecipeCategory();
        final String recipeComments = recipe.getComments();
        //final Drawable recipePhoto = recipe.getPhotograph();
        final ArrayList<Ingredient> recipeIngredientList = recipe.getListofIngredients();

        HashMap<String, Object> data = new HashMap<>();

        data.put("Preparation Time", recipePreparationTime);
        data.put("Serving Number", recipeServingNumber);
        data.put("Category", recipeCategory);
        data.put("Comments", recipeComments);
        //data.put("Photo", recipePhoto);
        data.put("Ingredient List",recipeIngredientList);

        collectionReference
                .document(recipeTitle)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, recipeTitle + " data has been added successfully!");
                    }
                });

        recipeAdapter.add(recipe);
    };
    public void onOkPressedEdit(Recipe recipe,
                         String title,
                         String preparationTime,
                         int servingNumber,
                         String category,
                         String comments,
                         Drawable photo,
                         ArrayList<Ingredient> list) {
        recipe.setTitle(title);
        recipe.setPreparationTime(preparationTime);
        recipe.setRecipeCategory(category);
        recipe.setComments(comments);
        recipe.setNumberofServings(servingNumber);
        recipe.setListofIngredients(list);
        recipeAdapter.notifyDataSetChanged();
    };

}
