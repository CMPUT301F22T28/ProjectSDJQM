/**
 * RecipeListActivity
 * @version 1.2
 * @author Muchen Li & Defrim Binakaj @ Qingya Ye
 * @date Nov 3, 2022
 */
package com.example.projectsdjqm.recipe_list;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.projectsdjqm.MainActivity;
import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientActivity;
import com.example.projectsdjqm.meal_plan.MealPlanActivity;
import com.example.projectsdjqm.shopping_list.ShoppingListActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

/**
 * RecipeListActivity:
 * Main page for Recipe
 */
public class RecipeListActivity extends AppCompatActivity
        implements RecipeList.RecipeButtonListener,
        RecipeFragment.OnFragmentInteractionListener{

    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    final String TAG = "Recipes Activity";
    ListView recipeListView;
    RecipeList recipeAdapter;
    public ArrayList<Recipe> recipeList;
    Recipe selectedRecipe;
    Spinner spinnerForRecipe;
    String currentSortingType;
    Drawable d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_main);

        // firestore stuff
        Log.d(TAG, "onCreate");
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Recipes");
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // bottom nav
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

        // recipe list view init
        recipeListView = findViewById(R.id.recipe_list);
        recipeList = new ArrayList<>();
        ArrayList<Ingredient> ingredientlist = new ArrayList<>();

        // default icon if no image upload
        Drawable icon = ContextCompat.getDrawable(this, R.drawable.ic_notifications_black_24dp);

        recipeAdapter = new RecipeList(this, recipeList);
        recipeAdapter.setRecipeButtonListener(this);
        recipeListView.setAdapter(recipeAdapter);

        db.collection("Recipes")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                });

        final FloatingActionButton addButton = findViewById(R.id.add_recipe);
        addButton.setOnClickListener(view -> {
            RecipeFragment addRecipeFragment = new RecipeFragment();
            addRecipeFragment.show(getSupportFragmentManager(),"Add Recipe");
        });

        spinnerForRecipe = findViewById(R.id.spinner_for_recipe);
        spinnerForRecipe.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                String result = parent.getItemAtPosition(i).toString();
                currentSortingType = result;
                sortRecipeList(recipeList, result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // pull data from database
        collectionReference.addSnapshotListener((queryDocumentSnapshots, error) -> {
            recipeList.clear();
            for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
            {
                String title = doc.getId();
                int preptime = Integer.parseInt(Objects.requireNonNull(doc.getData().get("Preparation Time")).toString());
                int number = Integer.parseInt(Objects.requireNonNull(doc.getData().get("Serving Number")).toString());
                String category = (String) doc.getData().get("Category");
                String comm = (String) doc.getData().get("Comments");

                // use photokey (title of recipe) from firebase storage to load image to APP
                final String photokey = title.replace(" ","");
                StorageReference imageRef = storageReference.child("images/" + photokey);
                final long ONE_MEGABYTE = 1024 * 1024;

                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        d = new BitmapDrawable(getResources(),BitmapFactory.decodeByteArray(bytes, 0, bytes.length));

                        recipeList.add(new Recipe(
                                title,
                                preptime,
                                number,
                                category,
                                comm,
                                d,
                                ingredientlist));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        recipeList.add(new Recipe(
                                title,
                                preptime,
                                number,
                                category,
                                comm,
                                icon,
                                ingredientlist));
                    }
                });
                if (currentSortingType != null) {
                    if (!currentSortingType.equals("Sort")) {
                        sortRecipeList(recipeList,currentSortingType);
                    } else {
                        recipeAdapter.notifyDataSetChanged();
                    }
                } else {
                    recipeAdapter.notifyDataSetChanged();
                }
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
        
        // Create a storage reference from our app
        final String photokey = selectedRecipe.getTitle().replace(" ","");
        StorageReference imageRef = storageReference.child("images/" + photokey);
        // Delete the file
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.d(TAG,selectedRecipe.getTitle() + " has been deleted from storage");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
                Log.d(TAG, "Image not found from storage");
            }
        });
    }

    @Override
    public void onOkPressedAdd(Recipe recipe) {

        final CollectionReference collectionReference = db.collection("Recipes");

        final String recipeTitle = recipe.getTitle();
        final int recipePreparationTime = recipe.getPreparationTime();
        final int recipeServingNumber = recipe.getNumberofServings();
        final String recipeCategory = recipe.getRecipeCategory();
        final String recipeComments = recipe.getComments();
        final ArrayList<Ingredient> recipeIngredientList = recipe.getListofIngredients();

        HashMap<String, Object> data = new HashMap<>();

        data.put("Preparation Time", recipePreparationTime);
        data.put("Serving Number", recipeServingNumber);
        data.put("Category", recipeCategory);
        data.put("Comments", recipeComments);
        data.put("Ingredient List",recipeIngredientList);

        collectionReference
                .document(recipeTitle)
                .set(data)
                .addOnSuccessListener(aVoid -> Log.d(TAG, recipeTitle + " data has been added successfully!"));

        recipeAdapter.add(recipe);
    }
    public void onOkPressedEdit(Recipe recipe,
                         String title,
                         int preparationTime,
                         int servingNumber,
                         String category,
                         String comments,
                         Drawable photo,
                         ArrayList<Ingredient> list) {
        String oldTitle = recipe.getTitle();
        recipe.setTitle(title);
        recipe.setPreparationTime(preparationTime);
        recipe.setRecipeCategory(category);
        recipe.setComments(comments);
        recipe.setPhotograph(photo);
        recipe.setNumberofServings(servingNumber);
        recipe.setListofIngredients(list);

        final CollectionReference collectionReference = db.collection("Recipes");
        final int recipePrepTime = recipe.getPreparationTime();
        final int recipeSerNum = recipe.getNumberofServings();
        final String recipeCate = recipe.getRecipeCategory();
        final String recipeComm = recipe.getComments();
        final ArrayList<Ingredient> recipeIng = recipe.getListofIngredients();

        HashMap<String, Object> data = new HashMap<>();

        data.put("Preparation Time", recipePrepTime);
        data.put("Serving Number", recipeSerNum);
        data.put("Category", recipeCate);
        data.put("Comments", recipeComm);
        data.put("Ingredient List",recipeIng);

        if (title == oldTitle) {
            collectionReference
                    .document(title)
                    .update(data);
        } else {
            collectionReference
                    .document(oldTitle)
                    .delete();
            collectionReference
                    .document(title)
                    .set(data);
        }
        recipeAdapter.notifyDataSetChanged();
    };


    private void sortRecipeList(ArrayList<Recipe> list, String sorting_type) {
        switch (sorting_type) {
            case "title":
                Collections.sort(list, new Comparator<Recipe>() {
                    @Override
                    public int compare(Recipe recipe, Recipe recipe1) {
                        return recipe.getTitle()
                                .compareTo(recipe1.getTitle());
                    }
                });
                recipeList = list;
                recipeAdapter.notifyDataSetChanged();
                break;
            case "category":
                Collections.sort(list, new Comparator<Recipe>() {
                    @Override
                    public int compare(Recipe recipe, Recipe recipe1) {
                        return recipe.getRecipeCategory()
                                .compareTo(recipe1.getRecipeCategory());
                    }
                });
                recipeList = list;
                recipeAdapter.notifyDataSetChanged();
                break;
            case "preparation time":
                Collections.sort(list, new Comparator<Recipe>() {
                    @Override
                    public int compare(Recipe recipe, Recipe recipe1) {
                        int time = recipe.getPreparationTime();
                        int time1 = recipe1.getPreparationTime();
                        if (time < time1) {
                            return -1;
                        } else if (time == time1) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
                recipeList = list;
                recipeAdapter.notifyDataSetChanged();
                break;
            case "serving size":
                Collections.sort(list, new Comparator<Recipe>() {
                    @Override
                    public int compare(Recipe recipe, Recipe recipe1) {
                        int servingNumber = recipe.getNumberofServings();
                        int servingNumber1 = recipe1.getNumberofServings();
                        if (servingNumber < servingNumber1) {
                            return -1;
                        } else if (servingNumber == servingNumber1) {
                            return 0;
                        } else {
                            return 1;
                        }
                    }
                });
                recipeList = list;
                recipeAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}
