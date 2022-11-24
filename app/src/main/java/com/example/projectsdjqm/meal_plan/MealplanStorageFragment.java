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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class MealplanStorageFragment extends DialogFragment {
    FirebaseFirestore db;
    private DataPassListener mCallback;
    private ListView recipeListview;
    private ListView ingredientListview;
    RecipeList recipeAdapter;
    IngredientList ingredientAdapter;
    private String selectedItem;
    private ArrayList<Integer> rec_sel_list = new ArrayList<>();
    private ArrayList<Integer> ingre_sel_list = new ArrayList<>();
    View view;

    public interface DataPassListener {
        public void passData(String data);
        public void On_storage_pressed(ArrayList<Integer> rec_sel_list, ArrayList<Integer> ingre_sel_list);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MealplanStorageFragment.DataPassListener) {
            mCallback = (MealplanStorageFragment.DataPassListener) context;
        } else {
            throw new RuntimeException(context.toString() + "This is not the correct fragment!");
        }
    }

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

        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ArrayList<Recipe> recipeList = new ArrayList<>();

        collectionReference_ingre.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
//                ingredientlist.clear();
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
                    int unit = Integer.valueOf(doc.getData().get("Unit").toString());

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

        collectionReference_rec.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                {
                    String title = doc.getId();
                    String preptime = String.valueOf(doc.getData().get("Preparation Time"));
                    int numser = Integer.valueOf(doc.getData().get("Serving Number").toString());
                    String category = (String) doc.getData().get("Category");
                    String comm = (String) doc.getData().get("Comments");
                    ArrayList<Ingredient> ingredientlist = new ArrayList<>();
                    ingredientlist.add(new Ingredient("apple",new Date(2020,2,1),Ingredient.Location.Fridge,1,1,"here"));
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
