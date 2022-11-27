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

public class MealplanScaleFragment extends DialogFragment {
    private OnMealplanScaleFragmentListener listener;
    private EditText recipe_scale_text;

    public interface OnMealplanScaleFragmentListener {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof  OnMealplanScaleFragmentListener) {
            listener = (OnMealplanScaleFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString() + "This is not the correct fragment!");
        }
    }

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

                    }
                }).create();

    }
}

