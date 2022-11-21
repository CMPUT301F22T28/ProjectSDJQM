package com.example.projectsdjqm.meal_plan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.recipe_list.Recipe;
import com.example.projectsdjqm.recipe_list.RecipeFragment;

import java.util.ArrayList;

public class MealplanFragment extends DialogFragment {
    public interface OnFragmentInteractionListener {
        void onOkPressedAdd(Mealplan mealplan);
    }

    // attr init
    private EditText recipeTitle;
    // preparation time should change to a time selector? can discuss and decide in project part 4
    private EditText recipePreparationTime;
    private EditText recipeServingNumber;
    private EditText recipeCategory;
    private EditText recipeComments;
    private Button photoSelectButton;
    private Button ingredientSelectButton;
    private ImageView photo;
    private TextView ingredientText;
    private Mealplan mealplan;
    private boolean isEdit = false;
    private MealplanFragment.OnFragmentInteractionListener listener;

    public MealplanFragment(Mealplan mealplan) {
        super();
        this.mealplan = mealplan;
        this.isEdit = true;
    }
    public MealplanFragment() {
        super();

    }

    // fragment interaction listener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MealplanFragment.OnFragmentInteractionListener) {
            listener = (MealplanFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "This is not the correct fragment!");
        }
    }

    // layout inflater to update fields
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.mealplan_add_fragment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder
                .setView(view)
                .setTitle("Adding Meal Plan")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();

    }
}
