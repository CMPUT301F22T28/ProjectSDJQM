package com.example.projectsdjqm.meal_plan;

import static android.content.ContentValues.TAG;
import static android.content.Intent.getIntent;

import static androidx.fragment.app.FragmentKt.setFragmentResultListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.recipe_list.Recipe;
import com.example.projectsdjqm.recipe_list.RecipeFragment;
import com.example.projectsdjqm.recipe_list.RecipeList;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * MealplanFragment:
 * mealplan adding fragment that used to add a new
 * meaplan to the mealplan list
 * @author Jianming Ma
 * @date Nov.23rd, 2022
 */

public class MealplanFragment extends DialogFragment {
    /**
     * Interface for add / edit listeners
     */
    public interface OnFragmentInteractionListener {
        /**
         * This method that is called when the ok button of mealplan adding
         * fragment is pressed
         * @param mealplan mealplan instance that is sent by the adding fragment
         */
        void onOkPressedAdd(Mealplan mealplan);

        /**
         * This method is called when the ADD button of mealplan adding fragment
         * (either recipelist or ingredientlist) is pressed
         * aims to show mealplan storage fragment instance
         */
        void add_meal_plan_from_storage();
    }


    // attr init
    private Mealplan mealplan;
    private MealplanFragment.OnFragmentInteractionListener listener;
    private DatePicker mealplan_date_view;
    private ListView recipeListview;
    private ListView ingredientListview;
    private String selectedItem_recipe;
    private String selectedItem_ingre;
    private ArrayList<String> recipeList_str;
    private ArrayList<String> ingredientList_str;
    private ArrayList<Integer> recipeList_int = new ArrayList<Integer>();
    private ArrayList<Integer> ingredientList_int = new ArrayList<Integer>();
    ArrayAdapter<String> recipeAdapter;
    ArrayAdapter<String> ingredientAdapter;
    FirebaseFirestore db;


    /**
     * This is a constructor to create MealplanFragment object.
     * packagename.classname#MealplanFragment
     * @param mealplan current mealplan to be edited
     */
    public MealplanFragment(Mealplan mealplan) {
        super();
        this.mealplan = mealplan;
    }
    public MealplanFragment() {
        super();
    }

    /**
     * This is an override method onAttach
     * Called when a fragment is first attached to its context,
     * Create the fragment iteraction listener
     * @param context context
     * @throws RuntimeException
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MealplanFragment.OnFragmentInteractionListener) {
            listener = (MealplanFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "This is not the correct fragment!");
        }
    }


    /**
     * This is an override method onCreateDialog
     * layout inflater to update fields
     * @param savedInstanceState The last saved instance state of the Fragment,
     * or null if this is a freshly created Fragment
     * @return Dialog Return a new Dialog instance to be displayed by the Fragment
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.mealplan_add_fragment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


        // initialize listviews and datepicker
        mealplan_date_view = view.findViewById(R.id.mealplan_date_picker);
        recipeListview = view.findViewById(R.id.mealplan_r_add_list);
        ingredientListview = view.findViewById(R.id.mealplan_in_add_list);

        // initialize arraylists
        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        ArrayList<Recipe> recipeList = new ArrayList<>();
        ingredientList_str = new ArrayList<>();
        recipeList_str = new ArrayList<>();

        // firestore initialization
        Log.d(TAG, "onCreateView");
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference_ingre = db.collection("Ingredients");
        CollectionReference collectionReference_rec = db.collection("Recipes");

        // set database path of ingredients and recipes
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

        // retrieve the user selected recipes from bundle object
        // handle NULL POINTER EXCEPTION
        Bundle bundle = getArguments();
        if (bundle != null) {
            // update the meal plan adding fragement for recipe
            if (bundle.getStringArrayList("rec_sel_list") != null) {
                recipeList_int = bundle.getIntegerArrayList("rec_sel_list");
                collectionReference_rec.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                            FirebaseFirestoreException error) {
                        for(QueryDocumentSnapshot doc: queryDocumentSnapshots)
                        {
                            String title = doc.getId();
                            int preptime = Integer.parseInt(String.valueOf(doc.getData().get("Preparation Time")));
                            int numser = Integer.valueOf(doc.getData().get("Serving Number").toString());
                            String category = (String) doc.getData().get("Category");
                            String comm = (String) doc.getData().get("Comments");
                            ArrayList<Ingredient> ingredientlist = new ArrayList<>();
                            ingredientlist.add(new Ingredient("apple",new Date(2020,2,1),Ingredient.Location.Fridge,1,"kg","here"));
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
                        ArrayList<String> recipeList_str_container = new ArrayList<>();
                        for (Integer num : recipeList_int){
                            recipeList_str_container.add(recipeList_str.get(num));
                        }
                        recipeList_str = recipeList_str_container;
                        recipeAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, recipeList_str);
                        recipeListview.setAdapter(recipeAdapter);
                    }
                });
            }
            // retrieve the user selected recipes from bundle object
            // update the meal plan adding fragement for recipe
            if (bundle.getIntegerArrayList("ingre_sel_list") != null) {
                ingredientList_int = bundle.getIntegerArrayList("ingre_sel_list");
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
                        ArrayList<String> ingredientList_str_container = new ArrayList<>();
                        for (Integer num : ingredientList_int){
                            ingredientList_str_container.add(ingredientList_str.get(num));
                        }
                        ingredientList_str = ingredientList_str_container;

                        ingredientAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, ingredientList_str);
                        ingredientListview.setAdapter(ingredientAdapter);
                    }
                });
            }
        } else {
            ingredientList_str.add("EMPTY!");
            recipeList_str.add("EMPTY!");
        }

        // ADD button to add recipes from database recipe storage
        Button recipe_add_button = (Button) view.findViewById(R.id.mealplan_r_add_button);
        recipe_add_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.add_meal_plan_from_storage();
            }
        });
        // ADD button to add ingredients from database ingredients storage
        Button ingredient_add_button = (Button) view.findViewById(R.id.mealplan_in_add_button);
        ingredient_add_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                listener.add_meal_plan_from_storage();
            }
        });

        // click on the recipe list view and use the delete button to delete selected recipe
        recipeListview.setAdapter(recipeAdapter);
        recipeListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem_recipe = (String) recipeAdapter.getItem(position);
            }

        });
        Button recipe_delete_button = (Button) view.findViewById(R.id.mealplan_r_delete_button);
        recipe_delete_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                recipeAdapter.remove(selectedItem_recipe);
            }
        });

        // click on the ingredient list view and use the delete button to delete selected ingredient
        ingredientListview.setAdapter(ingredientAdapter);
        ingredientListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem_ingre = (String) ingredientAdapter.getItem(position);
            }

        });
        Button ingredient_delete_button = (Button) view.findViewById(R.id.mealplan_in_delete_button);
        ingredient_delete_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ingredientAdapter.remove(selectedItem_ingre);
            }
        });

        // builder of meal plan adding fragment
        return builder
                .setView(view)
                .setTitle("Adding Meal Plan")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Calendar calendar = new GregorianCalendar(
                                mealplan_date_view.getYear(),
                                mealplan_date_view.getMonth(),
                                mealplan_date_view.getDayOfMonth());
                        Date mealplan_date = new Date(calendar.getTimeInMillis());

                        ArrayList<Recipe> recipeList_container = new ArrayList<>();
                        ArrayList<Integer> recipeScale_container = new ArrayList<>();
                        for (Integer num : recipeList_int){
                            recipeList_container.add(recipeList.get(num));
                            recipeScale_container.add(1);
                        }
                        ArrayList<Ingredient> ingredientList_container = new ArrayList<>();

                        for (Integer num : ingredientList_int){
                            ingredientList_container.add(ingredientList.get(num));

                        }
                        listener.onOkPressedAdd(new Mealplan(recipeList_container,ingredientList_container,mealplan_date,recipeScale_container));
                    }
                }).create();


    }


}
