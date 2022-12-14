/**
 * RecipeListActivity:
 * Main page for Recipe
 * @version 1.2
 * @author Qingya Ye, Muchen Li, Defrim Binakaj
 * @date Nov. 3rd, 2022
 */
package com.example.projectsdjqm.recipe_list;

import static com.example.projectsdjqm.recipe_list.RecipeFragment.ingAdapter;
import static com.example.projectsdjqm.recipe_list.RecipeFragment.list;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.projectsdjqm.MainActivity;
import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientActivity;
import com.example.projectsdjqm.meal_plan.MealPlanActivity;
import com.example.projectsdjqm.shopping_list.ShoppingListActivity;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Objects;

public class RecipeListActivity extends AppCompatActivity
        implements RecipeList.RecipeButtonListener,
        RecipeFragment.OnFragmentInteractionListener,
AddIngredientFragment.OnAddIngreidentFragmentIteractionListener{

    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;

    private FirebaseStorage storage;
    private StorageReference storageReference;
    final String TAG = "Recipes Activity";
    public ListView recipeListView;
    public RecipeList recipeAdapter;
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
        View view2 = getLayoutInflater()
                        .inflate(R.layout.ingredient_in_recipe_fragment, null);

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

        spinnerForRecipe = findViewById(R.id.recipe_sort_spinner);
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
//                ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();

                String ingre_path = "Recipes"+"/"+title+"/"+"ingredient List";
                CollectionReference collectionReference_recipe_ingredient = db.collection(ingre_path);
                ArrayList<Ingredient> ingredientList = new ArrayList<>();

//                // add snap shot of ingredient List subcollection
                collectionReference_recipe_ingredient.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                            FirebaseFirestoreException error) {
                        ingredientList.clear();
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                        {
                            Log.d(TAG, String.valueOf(doc.getData().get("Category")));
                            String description = doc.getId();
                            int amount = Integer.valueOf(doc.getData().get("Amount").toString());
                            String category = (String) doc.getData().get("Category");

                            String unit = (String) doc.getData().get("Unit");

                            ingredientList.add(new Ingredient(
                                    description,
                                    null,
                                    null,
                                    amount,
                                    unit,
                                    category));
                        }
                    }
                });

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
                                ingredientList));
                        sortRecipeList(recipeList,"title");
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
                                ingredientList));
                        sortRecipeList(recipeList,"title");
                    }
                });
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
        for (Ingredient ingre: selectedRecipe.getListofIngredients()) {
            collectionReference
                    .document(selectedRecipe.getTitle())
                    .collection("ingredient List").document(ingre.getIngredientDescription())
                    .delete();
        }
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

    /**
     * This method is called when the ok button of ingredient adding fragment is pressed
     * on the recipe adding fragment
     * @param recipeTitle the title of a recipe that the candidate ingredient belongs to
     * @param ingredient the ingredient to be deleted
     */
    /**
     * This method is called when the ok button
     * @param ingredient a candidate ingredient to be added
     */
    @Override
    public void onAddIngredientOkPressed(Ingredient ingredient) {
        list.add(ingredient);
        ingAdapter.notifyDataSetChanged();
    }


    /**
     * This method is called when the ok button of ingredient removing AlertDialog is pressed
     * on the recipe adding fragment
     * @param recipeTitle the title of a recipe that the candidate ingredient belongs to
     * @param ingredient the ingredient to be deleted
     */
    @Override
    public void onRemoveIngredient(String recipeTitle, Ingredient ingredient) {
        list.remove(ingredient);
        ingAdapter.notifyDataSetChanged();
        CollectionReference collectionReference = db.collection("Recipes");

        try {
            collectionReference.document(recipeTitle)
                    .collection("ingredient List")
                    .document(ingredient.getIngredientDescription())
                    .delete();
        } catch (Exception e) {
            Log.d("removeIng","recipe has not been created");
        }
    }
    /**
     * This method is called when the ok button of recipe adding fragment is pressed
     * @param recipe Recipe instance that is sent by adding fragment, will be
     * added to database
     */
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
        HashMap<String, Object> nestedData_ingre = new HashMap<>();

        data.put("Preparation Time", recipePreparationTime);
        data.put("Serving Number", recipeServingNumber);
        data.put("Category", recipeCategory);
        data.put("Comments", recipeComments);

        collectionReference
                .document(recipeTitle)
                .set(data)
                .addOnSuccessListener(aVoid -> Log.d(TAG, recipeTitle + " data has been added successfully!"));

        for (Ingredient ingre: recipeIngredientList) {
            final String ingredientDesc = ingre.getIngredientDescription();
            nestedData_ingre.put("Amount",ingre.getIngredientAmount());
            nestedData_ingre.put("Best Before Date",ingre.getIngredientBestBeforeDate());
            nestedData_ingre.put("Category",ingre.getIngredientCategory());
            nestedData_ingre.put("Location",ingre.getIngredientLocation());
            nestedData_ingre.put("Unit",ingre.getIngredientUnit());
            collectionReference
                    .document(recipeTitle)
                    .collection("ingredient List").document(ingredientDesc)
                    .set(nestedData_ingre)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, " data has been added successfully!");
                        }
                    });
        }
        recipeAdapter.add(recipe);
    }

    /**
     * This method is called when the ok button of ingredient editing fragment is pressed
     * @param recipe the recipe to be edited
     * @param title new title
     * @param preparationTime new preparation time
     * @param servingNumber new serving number
     * @param category new category
     * @param comments new comments
     * @param photo new photo
     * @param list1 new ingredient list
     */
    public void onOkPressedEdit(Recipe recipe,
                         String title,
                         int preparationTime,
                         int servingNumber,
                         String category,
                         String comments,
                         Drawable photo,
                         ArrayList<Ingredient> list1) {
        String oldTitle = recipe.getTitle();
        recipe.setTitle(title);
        recipe.setPreparationTime(preparationTime);
        recipe.setRecipeCategory(category);
        recipe.setComments(comments);
        recipe.setPhotograph(photo);
        recipe.setNumberofServings(servingNumber);
        recipe.setListofIngredients(list1);

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

        if (Objects.equals(title, oldTitle)) {
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
        HashMap<String, Object> nestedData_ingre = new HashMap<>();
        for (Ingredient ingre: recipeIng) {
            collectionReference
                    .document(title)
                    .collection("ingredient List")
                    .document(ingre.getIngredientDescription())
                    .delete();
        }

        for (Ingredient ingre: recipeIng) {
            final String ingredientDesc = ingre.getIngredientDescription();
            nestedData_ingre.put("Amount",ingre.getIngredientAmount());
            nestedData_ingre.put("Best Before Date",ingre.getIngredientBestBeforeDate());
            nestedData_ingre.put("Category",ingre.getIngredientCategory());
            nestedData_ingre.put("Location",ingre.getIngredientLocation());
            nestedData_ingre.put("Unit",ingre.getIngredientUnit());
            collectionReference
                    .document(title)
                    .collection("ingredient List").document(ingredientDesc)
                    .set(nestedData_ingre)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, " data has been added successfully!");
                        }
                    });
        }
        ingAdapter.notifyDataSetChanged();
        recipeAdapter.notifyDataSetChanged();
    };

    /**
     * a method to sort the recipe list
     * @param list the list to be sorted
     * @param sorting_type sorting type that sort the list
     */
    private void sortRecipeList(ArrayList<Recipe> list, String sorting_type) {
        switch (sorting_type) {
            case "Title":
                Collections.sort(list, new Comparator<Recipe>() {
                    @Override
                    public int compare(Recipe recipe, Recipe recipe1) {
                        return recipe.getTitle()
                                .compareTo(recipe1.getTitle());
                    }
                });
                break;
            case "Category":
                Collections.sort(list, new Comparator<Recipe>() {
                    @Override
                    public int compare(Recipe recipe, Recipe recipe1) {
                        return recipe.getRecipeCategory()
                                .compareTo(recipe1.getRecipeCategory());
                    }
                });
                break;
            case "Preparation Time":
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
                break;
            case "Serving Size":
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
                break;
            default:
                break;
        }
        recipeList = list;
        recipeAdapter.notifyDataSetChanged();
    }
}
