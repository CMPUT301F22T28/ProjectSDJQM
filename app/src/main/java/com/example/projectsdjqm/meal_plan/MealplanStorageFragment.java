package com.example.projectsdjqm.meal_plan;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientList;
import com.example.projectsdjqm.recipe_list.Recipe;
import com.example.projectsdjqm.recipe_list.RecipeList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Date;

/**
 * MealplanStorageFragment
 * @version 3.5
 * @author Jianming Ma
 * @date Nov.23rd, 2022
 */
public class MealplanStorageFragment extends DialogFragment {
    // Initialization of variables
    FirebaseFirestore db;
    private DataPassListener mCallback;
    private ListView recipeListview;
    private ListView ingredientListview;
    RecipeList recipeAdapter;
    IngredientList ingredientAdapter;
    private ArrayList<Integer> rec_sel_list = new ArrayList<>();
    private ArrayList<Integer> ingre_sel_list = new ArrayList<>();
    View view;

    /**
     * Interface for add / edit listeners
     */
    public interface DataPassListener {
        /**
         * This method that is called when the ok button of mealplan storage
         * fragment is pressed
         * @param rec_sel_list arraylist of integers that contains the index of seleted recipes
         * @param ingre_sel_list arraylist of integers that contains the index of seleted ingredients
         */
        public void On_storage_pressed(ArrayList<Integer> rec_sel_list, ArrayList<Integer> ingre_sel_list);
    }

    /**
     * This is an override method onAttach
     * Called when a fragment is first attached to its context,
     * Create the fragment listener
     * @param context context
     * @throws RuntimeException
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MealplanStorageFragment.DataPassListener) {
            mCallback = (MealplanStorageFragment.DataPassListener) context;
        } else {
            throw new RuntimeException(context.toString() + "This is not the correct fragment!");
        }
    }

    /**
     * This is an override method onCreateView
     * layout inflater to update fields
     * @param inflater The LayoutInflater object that can be used to inflate any views in the fragment
     * @param container If non-null, this is the parent view that the fragment's UI should be attached to.
     * The fragment should not add the view itself, but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment
     * @return view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mealplan_storage_fragment, container, false);

        recipeListview = view.findViewById(R.id.mealplan_r_storage_list);
        ingredientListview = view.findViewById(R.id.mealplan_in_storage_list);
        recipeListview.setAdapter(recipeAdapter);
        ingredientListview.setAdapter(ingredientAdapter);
        ArrayList<String> ingredientList_str = new ArrayList<String>();
        ArrayList<String> recipeList_str = new ArrayList<String>();
        Log.d(TAG, "onCreateView");
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference_ingre = db.collection("Ingredients");
        CollectionReference collectionReference_rec = db.collection("Recipes");

        // retrieve data from firestore
        db.collection("Ingredients")
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

        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ArrayList<Recipe> recipeList = new ArrayList<>();

        collectionReference_ingre.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
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

                    ingredientList.add(new Ingredient(
                            description,
                            bestbeforedate,
                            location,
                            amount,
                            unit,
                            category));


                }

                for (Ingredient ingre : ingredientList) {
                    ingredientList_str.add(ingre.getIngredientDescription());
                }
                ArrayAdapter<String> ingredientAdapter =
                        new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, ingredientList_str);
                ingredientListview.setAdapter(ingredientAdapter);
            }
        });

        collectionReference_rec.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String title = doc.getId();
                    int preptime = Integer.valueOf(doc.getData().get("Preparation Time").toString());
                    int numser = Integer.valueOf(doc.getData().get("Serving Number").toString());
                    String category = (String) doc.getData().get("Category");
                    String comm = (String) doc.getData().get("Comments");
                    ArrayList<Ingredient> ingredientlist = new ArrayList<>();
                    //ingredientlist.add(new Ingredient("apple",new Date(2020,2,1),Ingredient.Location.Fridge,1,1,"here"));
                    Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_notifications_black_24dp);
                    // drawable photo;
                    // arrayList
                    recipeList.add(new Recipe(
                            title,
                            preptime,
                            numser,
                            category,
                            comm,
                            icon,
                            ingredientlist));
                }
                for (Recipe rec : recipeList) {
                    recipeList_str.add(rec.getTitle());
                }
                ArrayAdapter<String> recipeAdapter =
                        new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, recipeList_str);
                recipeListview.setAdapter(recipeAdapter);
            }
        });

        recipeListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                rec_sel_list.add(position);
            }

        });

        ingredientListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ingre_sel_list.add(position);
            }

        });

        Button addButton = view.findViewById(R.id.add_storage_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCallback.On_storage_pressed(rec_sel_list,ingre_sel_list);
            }
        });

        return view;
    }
}
