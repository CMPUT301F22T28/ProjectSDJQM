package com.example.projectsdjqm.recipe_list;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;

public class AddIngredientFragment extends DialogFragment {

    private EditText description;
    private EditText category;
    private EditText unitCost;
    private EditText amount;
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
            throw new RuntimeException(context + "This is not the correct fragment!");
        }
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater().inflate(R.layout.ingredient_in_recipe_content, null);

        description = view.findViewById(R.id.description_l);
        category = view.findViewById(R.id.category_l);
        unitCost = view.findViewById(R.id.unit_cost_l);
        amount = view.findViewById(R.id.count_l);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = builder
                .setView(view)
                .setTitle("Add Ingredient")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", null)
                .create();
        alertDialog.show();

        Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        okButton.setOnClickListener(new AddIngredientFragment.CustomListener(alertDialog));

        return alertDialog;
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;

        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            String descriptionInput = description.getText().toString();
            String categoryInput = category.getText().toString();
            String unitCostInputStr = unitCost.getText().toString();
            String amountInputStr = amount.getText().toString();
            boolean isValid = true;

            // check description input
            if (descriptionInput.length() < 1) {
                isValid = false;
                description.setError("Enter a name");
            } else if (descriptionInput.length() > Ingredient.MAX_LENGTH_NAME) {
                description.setError("Name must be less than 30 characters");
                isValid = false;
            }

            // check category input
            if (categoryInput.length() < 1) {
                isValid = false;
                category.setError("Enter a name");
            }

            // check unit cost input
            int unitCostInput = 0;
            try {
                if (!unitCostInputStr.isEmpty()) {
                    unitCostInput = Integer.parseInt(unitCostInputStr);

                    if (unitCostInput < 1) {
                        isValid = false;
                        unitCost.setError("Enter a positive number");
                    }
                } else {
                    isValid = false;
                    unitCost.setError("Enter a positive number");
                }
            } catch (NumberFormatException ex) {
                isValid = false;
                unitCost.setError("Enter a positive number");
                Log.d("NumberFormatLog", "error on numberformat is " + ex.getMessage());
                ex.printStackTrace();
            }

            // check amount input
            int amountInput = 0;
            try {
                if (!amountInputStr.isEmpty()) {
                    amountInput = Integer.parseInt(amountInputStr);

                    if (amountInput < 1) {
                        isValid = false;
                        amount.setError("Enter a positive number");
                    }
                } else {
                    isValid = false;
                    amount.setError("Enter a positive number");
                }
            } catch (NumberFormatException ex) {
                isValid = false;
                amount.setError("Enter a positive number");
                Log.d("NumberFormatLog", "error on numberformat is " + ex.getMessage());
                ex.printStackTrace();
            }
            if (isValid) {
                Ingredient ingredient = new Ingredient(
                        descriptionInput,
                        null,
                        null,
                        amountInput,
                        unitCostInput,
                        categoryInput
                );
                listener.onAddIngredientOkPressed(ingredient);
                dialog.dismiss();
            }
        }
    }
}
