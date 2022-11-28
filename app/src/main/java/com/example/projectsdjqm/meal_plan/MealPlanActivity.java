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
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * MealPlanActivity
 * @version 1.2
 * @author Jianming Ma
 * @date Nov.23rd, 2022
 */
public class MealPlanActivity extends AppCompatActivity
        implements MealplanFragment.OnFragmentInteractionListener,
                    MealplanStorageFragment.DataPassListener,
                    MealplanList.recipeScaleListener,
                    MealplanScaleFragment.OnMealplanScaleFragmentListener{

    
    // initialization of variables
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;
    final String TAG = "Mealplan Activity";
    ListView mealplanListView;
    MealplanList mealplanAdapter;
    ArrayList<Mealplan> mealplanList;
    Recipe selectedMealplan;
    MealplanFragment addMealplanFragment = new MealplanFragment();
    MealplanStorageFragment mealplanStorageFragment = null;
    MealplanFragment add1MealplanFragment = null;
    MealplanScaleFragment editRecipeScale = null;
    private int recipe_position;
    private int mealplan_index;



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
        ArrayList<ArrayList<Recipe>> recipeList = new ArrayList<>();
        Drawable icon = ContextCompat.getDrawable(this, R.drawable.ic_notifications_black_24dp);

        mealplanAdapter = new MealplanList(this, mealplanList);
        mealplanAdapter.setScalelistener(this);
        mealplanListView.setAdapter(mealplanAdapter);

        final FloatingActionButton addButton = findViewById(R.id.add_meal_plan);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addMealplanFragment.show(getSupportFragmentManager(),"Add Mealplan");
            }
        });


        // Pull mealplanlist from database
        // Initialization of main collection
        final ArrayList<Recipe>[] recipelist = new ArrayList[]{new ArrayList<>()};
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

        // main collection reference of mealplan
        collectionReference.addSnapshotListener((queryDocumentSnapshots, error) -> {
            mealplanList.clear();
            for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
            {
                String mealplan_id = doc.getId();
                Timestamp ts = (Timestamp) doc.getData().get("Mealplan_date");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date mealplan_date = null;
                try{
                    mealplan_date = dateFormat.parse(mealplan_id);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ArrayList<Integer> recipeScale = (ArrayList<Integer>) doc.getData().get("Recipe Scale");
                // path of recipe list within mealplan collection
                String recipe_path = "MealPlans"+"/"+mealplan_id+"/"+"recipe List";
                CollectionReference collectionReference_mealplan_recipe = db.collection(recipe_path);
                db.collection(recipe_path)
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

                // add snap shot of recipe List subcollection
                collectionReference_mealplan_recipe.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                            FirebaseFirestoreException error) {
                        recipelist[0] = new ArrayList<Recipe>();
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                        {
                            String title = doc.getId();
                            int preptime = Integer.valueOf(doc.getData().get("preptime").toString());
                            int numser = Integer.valueOf(doc.getData().get("Serving Number").toString());
                            String category = (String) doc.getData().get("Category");
                            String comm = (String) doc.getData().get("Comments");
                            // add snap shot of Ingredient List of recipe list subcollection
                            recipelist[0].add(new Recipe(
                                    title,
                                    preptime,
                                    numser,
                                    category,
                                    comm,
                                    icon,
                                    ingredientList));
                        }
                        recipeList.add(recipelist[0]);
                    }
                });


                // path of ingredient list within mealplan collection
                String ingre_path = "MealPlans"+"/"+mealplan_id+"/"+"ingredient List";
                CollectionReference collectionReference_mealplan_ingredient = db.collection(ingre_path);
                db.collection(ingre_path)
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
                // add snap shot of ingredient List subcollection
                Date finalMealplan_date = mealplan_date;
                collectionReference_mealplan_ingredient.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                            FirebaseFirestoreException error) {
                        ArrayList<Ingredient> ingredientlist = new ArrayList<>();
                        ingredientlist.clear();
                        int count = 0;
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
                            String unit = (String) doc.getData().get("Unit");

                            ingredientlist.add(new Ingredient(
                                    description,
                                    bestbeforedate,
                                    location,
                                    amount,
                                    unit,
                                    category));
                        }
                        Mealplan test = new Mealplan(recipelist[0], ingredientlist, finalMealplan_date, recipeScale);
                        mealplanList.add(test);
                        mealplanAdapter.notifyDataSetChanged();

                    }
                });
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
        final ArrayList<Integer> recipeScale = mealplan.getRecipeScale();

        HashMap<String, Object> data = new HashMap<>();
        HashMap<String, Object> nestedData_rec = new HashMap<>();
        HashMap<String, Object> nestedData_ingre = new HashMap<>();

        // Store mealplan into database
        data.put("Mealplan_Date",mealplan_date);
        data.put("Recipe Scale",recipeScale);
        collectionReference
                .document(mealplan_date_str)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, mealplan_date + " data has been added successfully!");
                    }
                });

        // add recipelist as subcollection
        for (Recipe rec: recipeList) {
            final String recipeTitle = rec.getTitle();
            nestedData_rec.put("Title",rec.getTitle());
            nestedData_rec.put("preptime",rec.getPreparationTime());
            nestedData_rec.put("Serving Number",rec.getNumberofServings());
            nestedData_rec.put("Category",rec.getRecipeCategory());
            nestedData_rec.put("Comments",rec.getComments());
            nestedData_rec.put("Ingredient List",rec.getListofIngredients());
            collectionReference
                    .document(mealplan_date_str)
                    .collection("recipe List").document(recipeTitle)
                    .set(nestedData_rec)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, mealplan_date + " data has been added successfully!");
                        }
                    });
        }
        // add ingredient list as subcollection
        for (Ingredient ingre: IngredientList) {
            final String ingredientDesc = ingre.getIngredientDescription();
            nestedData_ingre.put("Amount",ingre.getIngredientAmount());
            nestedData_ingre.put("Best Before Date",ingre.getIngredientBestBeforeDate());
            nestedData_ingre.put("Category",ingre.getIngredientCategory());
            nestedData_ingre.put("Location",ingre.getIngredientLocation());
            nestedData_ingre.put("Unit",ingre.getIngredientUnit());
            collectionReference
                    .document(mealplan_date_str)
                    .collection("ingredient List").document(ingredientDesc)
                    .set(nestedData_ingre)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, mealplan_date + " data has been added successfully!");
                        }
                    });
        }
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

    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    @Override
    public void onRecipeScaleListPressed(int position, int mealplan_position) {
        recipe_position = position;
        mealplan_index = mealplan_position-1;
        editRecipeScale = new MealplanScaleFragment();
        editRecipeScale.show(getSupportFragmentManager(),"Edit scale");
    }

    @Override
    public void OnScaleOkpressedAdd(int recipeScale) {
        ArrayList<Integer> recipeScale_list = mealplanList.get(mealplan_index).getRecipeScale();
        final Date mealplan_date = mealplanList.get(mealplan_index).getMealplan_date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String mealplan_date_str = String.format(dateFormat.format(mealplan_date));

        recipeScale_list.set(recipe_position,recipeScale);
        final CollectionReference collectionReference = db.collection("MealPlans");
        HashMap<String, Object> data = new HashMap<>();
        data.put("Recipe Scale",recipeScale_list);
        mealplanAdapter.notifyDataSetChanged();
        collectionReference
                .document(mealplan_date_str)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, mealplan_date + " data has been added successfully!");
                    }
                });
    }
}
