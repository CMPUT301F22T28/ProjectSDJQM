package com.example.projectsdjqm.meal_plan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Date;

public class MealplanStorageFragment extends DialogFragment {
    private DataPassListener mCallback;
    private ListView recipeListview;
    private ListView ingredientListview;
    RecipeList recipeAdapter;
    IngredientList ingredientAdapter;
    private String selectedItem;
    View view;

    public interface DataPassListener {
        public void passData(String data);
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

        ArrayList<Ingredient> ingredientList = new ArrayList<>();
        Ingredient testc = new Ingredient("apple", new Date(2020, 2, 1), Ingredient.Location.Fridge, 1, 1, "here");
        ingredientList.add(testc);
        ArrayList<Recipe> recipeList = new ArrayList<>();
        Drawable icon = ContextCompat.getDrawable(getContext(), R.drawable.ic_notifications_black_24dp);
        Recipe testa = new Recipe("Orange Chicken", "30", 3,
                "category", "comments", icon,
                ingredientList);
        recipeList.add(testa);

        // test cases for mealplan storage
        // will be replaced by content within database
        ArrayList<String> recipeList_str = new ArrayList<String>();
        for (Recipe rec : recipeList) {
            recipeList_str.add(rec.getTitle());
        }
        ArrayAdapter<String> recipeAdapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, recipeList_str);
        recipeListview.setAdapter(recipeAdapter);

        ArrayList<String> ingredientList_str = new ArrayList<String>();
        for (Ingredient ingre : ingredientList) {
            ingredientList_str.add(ingre.getIngredientDescription());
        }
        ArrayAdapter<String> ingredientAdapter =
                new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, ingredientList_str);
        ingredientListview.setAdapter(ingredientAdapter);


        recipeListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = (String) recipeAdapter.getItem(position);
//                recipeList_str.add(selectedItem);
//                recipeListview.setAdapter(recipeAdapter);
//                MealplanFragment dialogFragment = new MealplanFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("TEXT", selectedItem);
//                dialogFragment.setArguments(bundle);
//                dialogFragment.show((MealplanStorageFragment.this).getSupportFragmentManager(),"Image Dialog");


            }

        });


        Button addButton = view.findViewById(R.id.add_storage_button);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


//        ActionBar actionBar = getSupportActionBar();
//
//        // showing the back button in action bar
//        actionBar.setDisplayHomeAsUpEnabled(true);
//
//
//

        return view;
    }
}
