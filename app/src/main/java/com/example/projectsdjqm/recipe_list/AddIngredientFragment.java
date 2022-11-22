package com.example.projectsdjqm.recipe_list;

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
import com.example.projectsdjqm.ingredient_storage.Ingredient;

public class AddIngredientFragment extends DialogFragment {

    private OnAddIngreidentFragmentIteractionListener listener;


    public interface OnAddIngreidentFragmentIteractionListener {
        void onAddIngredientOkPressed(Ingredient ingredient);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnAddIngreidentFragmentIteractionListener) {
            listener = (OnAddIngreidentFragmentIteractionListener) context;
        } else  {
            throw new RuntimeException(context.toString() + "This is not the correct fragment!");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.ingredient_in_recipe_content, null);

        EditText description = view.findViewById(R.id.description_l);
        EditText category = view.findViewById(R.id.category_l);
        EditText unitCost = view.findViewById(R.id.unit_cost_l);
        EditText amount = view.findViewById(R.id.count_l);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Add ingredient")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String descriptionInput = description.getText().toString();
                        String categoryInput = category.getText().toString();
                        int unitCostInput = Integer.parseInt(unitCost.getText().toString());
                        int amountInput = Integer.parseInt(amount.getText().toString());
                        Ingredient ingredient = new Ingredient(
                                descriptionInput,
                                null,
                                null,
                                amountInput,
                                unitCostInput,
                                categoryInput
                                );
                        listener.onAddIngredientOkPressed(ingredient);

                    }
                }).create();
    }
}
