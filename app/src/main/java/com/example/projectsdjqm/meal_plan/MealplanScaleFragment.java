package com.example.projectsdjqm.meal_plan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.projectsdjqm.R;

/**
 * MealplanScaleFragment
 * @version 3.5
 * @author Jianming Ma
 * @date Nov.23rd, 2022
 */
public class MealplanScaleFragment extends DialogFragment {
    private OnMealplanScaleFragmentListener listener;
    private EditText recipe_scale_text;

    /**
     * Interface for add / edit listeners
     */
    public interface OnMealplanScaleFragmentListener {
        /**
         * This method that is called when the ok button of mealplan Scale
         * fragment is pressed
         * @param recipeScale user changed recipe scale
         */
        void OnScaleOkpressedAdd(int recipeScale);
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
        if(context instanceof  OnMealplanScaleFragmentListener) {
            listener = (OnMealplanScaleFragmentListener) context;
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

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.mealplan_recipescale_fragment,null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        recipe_scale_text = view.findViewById(R.id.edit_recipe_scale);

        return builder
                .setView(view)
                .setTitle("Edit Recipe Scale")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int recipeScale = 0;
                        try{
                            recipeScale = Integer.parseInt(recipe_scale_text.getText().toString());
                        } catch(NumberFormatException ex){
                        }
                        listener.OnScaleOkpressedAdd(recipeScale);
                    }
                }).create();

    }
}

