/**
 * MealPlanActivity
 * @version 1.1
 * @author Muchen Li & Defrim Binakaj
 * @date Oct 30, 2022
 */
package com.example.projectsdjqm.meal_plan;

import static android.system.Os.remove;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectsdjqm.MainActivity;
import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientActivity;
import com.example.projectsdjqm.recipe_list.Recipe;
import com.example.projectsdjqm.recipe_list.RecipeListActivity;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * MealPlanActivity:
 * Main page of the meal plan
 */
public class MealPlanActivity extends AppCompatActivity
        implements MealplanFragment.OnFragmentInteractionListener,
                    MealplanStorageFragment.DataPassListener{

    
    // initialization of variables
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;
    final String TAG = "Mealplan Activity";
    ListView mealplanListView;
    ArrayAdapter<Mealplan> mealplanAdapter;
    ArrayList<Mealplan> mealplanList;
    Recipe selectedMealplan;
    MealplanFragment addMealplanFragment = new MealplanFragment();
    MealplanStorageFragment mealplanStorageFragment = null;
    MealplanFragment add1MealplanFragment = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealplan_main);

        Log.d(TAG, "onCreate");
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("MealPlans");

        // bottom nav initialization
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
//        Ingredient testc = new Ingredient("apple",new Date(2020,2,1),Ingredient.Location.Fridge,1,1,"here");
//        ingredientList.add(testc);
        ArrayList<Recipe> recipeList = new ArrayList<>();
        Drawable icon = ContextCompat.getDrawable(this, R.drawable.ic_notifications_black_24dp);
//        Recipe testa = new Recipe("Orange Chicken", "30", 3,
//                "category", "comments",icon,
//                ingredientList);
//        recipeList.add(testa);
//        Mealplan testb = new Mealplan(recipeList, ingredientList, new Date(2022,11,30));
//        mealplanList.add(testb);

        mealplanAdapter = new MealplanList(this, mealplanList);
        mealplanListView.setAdapter(mealplanAdapter);

        final FloatingActionButton addButton = findViewById(R.id.add_meal_plan);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMealplanFragment.show(getSupportFragmentManager(),"Add Mealplan");
            }
        });
        db.collection("MealPlans")
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

        collectionReference.addSnapshotListener((queryDocumentSnapshots, error) -> {
            recipeList.clear();
            for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
            {
                String mealplan_date = doc.getId();

//                Mealplan test = new Mealplan(recipeList, ingredientList, new Date(2022,11,30));
//                mealplanList.add(test);

            }
        });
    }


    public void onOkPressedAdd(Mealplan mealplan) {
        if (mealplanStorageFragment != null) {
            mealplanStorageFragment.dismiss();
        }
        final CollectionReference collectionReference = db.collection("MealPlans");
        final ArrayList<Recipe> recipeList = mealplan.getRecipeList();
        final ArrayList<Ingredient> IngredientList = mealplan.getIngredientList();
        final Date mealplan_date = mealplan.getMealplan_date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String mealplan_date_str = String.format(dateFormat.format(mealplan_date));

        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, Object> nestedData = new HashMap<>();

        data.put("Mealplan_Ingredient List",IngredientList);
        data.put("Mealplan_Date",mealplan_date);
        for (Recipe rec: recipeList) {
            nestedData.put("Title",rec.getTitle());
            nestedData.put("preptime",rec.getPreparationTime());
            nestedData.put("Serving Number",rec.getNumberofServings());
            nestedData.put("Category",rec.getRecipeCategory());
            nestedData.put("Comments",rec.getComments());
        }


        data.put("Mealplan_Recipe List", nestedData);
        collectionReference
                .document(mealplan_date_str)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, mealplan_date + " data has been added successfully!");
                    }
                });

        mealplanAdapter.add(mealplan);
    }

    public void add_meal_plan_from_storage() {
        if (mealplanStorageFragment != null) {
            mealplanStorageFragment.dismiss();
        }
        mealplanStorageFragment = new MealplanStorageFragment();
        mealplanStorageFragment.show(getSupportFragmentManager(),"Mealplan Storage");


    }
    public void passData(String data) {

    }

    public void On_storage_pressed(ArrayList<Integer> rec_sel_list,ArrayList<Integer> ingre_sel_list) {
        Bundle bundle = new Bundle();
        bundle.putIntegerArrayList("rec_sel_list", rec_sel_list);
        bundle.putIntegerArrayList("ingre_sel_list", ingre_sel_list);


        if (addMealplanFragment != null) {
            addMealplanFragment.dismiss();
        }
        if (add1MealplanFragment != null) {
            add1MealplanFragment.dismiss();
        }

        add1MealplanFragment = new MealplanFragment();

        add1MealplanFragment.show(getSupportFragmentManager(),"Add1 Mealplan");
        // Set Fragmentclass Arguments
        add1MealplanFragment.setArguments(bundle);
    }


}
