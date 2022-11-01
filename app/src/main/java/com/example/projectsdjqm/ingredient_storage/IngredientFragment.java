/**
 * IngredientFragment
 * @version 1
 * @author Muchen Li
 */
package com.example.projectsdjqm.ingredient_storage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.projectsdjqm.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class IngredientFragment extends DialogFragment {
    public interface OnFragmentInteractionListener {
        void onOkPressedAdd(Ingredient newIngredient);
        void onOkPressedEdit(Ingredient ingredient,
                             String description,
                             Date bestbeforedate,
                             Ingredient.Location location,
                             int amount,
                             int unit,
                             String category);
    }

    private EditText ingredientDescription;
    private DatePicker ingredientBestBeforeDate;
    private RadioGroup ingredientRadioGroup;
    private RadioButton ingredientLocationPantry;
    private RadioButton ingredientLocationFreezer;
    private RadioButton ingredientLocationFridge;
    private EditText ingredientAmount;
    private EditText ingredientUnit;
    private EditText ingredientCategory;

    private Ingredient ingredient;
    private Boolean isEdit = false;

    private OnFragmentInteractionListener listener;

    /* Constructor for editing */
    public IngredientFragment(Ingredient ingredient) {
        super();
        this.ingredient = ingredient;
        this.isEdit = true;
    }

    /* Default constructor */
    public IngredientFragment() {
        super();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "This is not the correct fragment!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.ingredient_add_fragment, null);

        ingredientDescription = view.findViewById(R.id.edit_ingredient_desc);
        ingredientCategory = view.findViewById(R.id.edit_ingredient_category);
        ingredientAmount = view.findViewById(R.id.edit_amount);
        ingredientUnit = view.findViewById(R.id.edit_unit);
        ingredientRadioGroup = view.findViewById(R.id.location_radio_group);
        ingredientLocationPantry = view.findViewById(R.id.Pantry);
        ingredientLocationFreezer = view.findViewById(R.id.Freezer);
        ingredientLocationFridge = view.findViewById(R.id.Fridge);
        ingredientBestBeforeDate = view.findViewById(R.id.edit_bestbeforedate_picker);

        if (isEdit) {
            ingredientDescription.setText(ingredient.getIngredientDescription());
            ingredientCategory.setText(ingredient.getIngredientCategory());
            ingredientAmount.setText(Integer.toString(ingredient.getIngredientAmount()));
            ingredientUnit.setText(Integer.toString(ingredient.getIngredientUnit()));

            switch (ingredient.getIngredientLocation()) {
                case Freezer:
                    ingredientRadioGroup.check(R.id.Freezer);
                case Fridge:
                    ingredientRadioGroup.check(R.id.Fridge);
                default:
                    ingredientRadioGroup.check(R.id.Pantry);
            }

            Date bestbeforedate = ingredient.getIngredientBestBeforeDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(bestbeforedate);
            ingredientBestBeforeDate.updateDate(
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DATE));
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = builder
                .setView(view)
                .setTitle("Add Ingredient")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", null)
                .create();
        alertDialog.show();

        Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        okButton.setOnClickListener(new CustomListener(alertDialog));

        return alertDialog;
    }

    /* Custom listener class to prevent dialog from closing if input is invalid */
    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;

        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            boolean isValid = true;

            String IngredientDescInput = ingredientDescription.getText().toString();
            String IngredientCategoryInput = ingredientCategory.getText().toString();
            String IngredientAmountInput = ingredientAmount.getText().toString();
            String IngredientUnitInput = ingredientUnit.getText().toString();

            Ingredient.Location location;
            int IngredientLocationInput = ingredientRadioGroup.getCheckedRadioButtonId();

            if (IngredientLocationInput == R.id.Pantry) {
                location = Ingredient.Location.Pantry;
            } else if (IngredientLocationInput == R.id.Freezer) {
                location = Ingredient.Location.Freezer;
            } else {
                location = Ingredient.Location.Fridge;
            }

            Calendar calendar = new GregorianCalendar(
                    ingredientBestBeforeDate.getYear(),
                    ingredientBestBeforeDate.getMonth(),
                    ingredientBestBeforeDate.getDayOfMonth());
            Date IngredientBestBeforeDateInput = new Date(calendar.getTimeInMillis());

            // check the description length
            if (IngredientDescInput.length() < 1) {
                isValid = false;
                ingredientDescription.setError("Enter a name");
            } else if (IngredientDescInput.length() > Ingredient.MAX_LENGTH_NAME) {
                ingredientDescription.setError("Name must be less than 30 characters");
                isValid = false;
            }

            // check amount input
            int amount = 0;
            try {
                if (!IngredientAmountInput.isEmpty()) {
                    amount = Integer.parseInt(IngredientAmountInput);

                    if (amount < 1) {
                        isValid = false;
                        ingredientAmount.setError("Enter a positive number");
                    }
                } else {
                    isValid = false;
                    ingredientAmount.setError("Enter a positive number");
                }
            } catch (NumberFormatException ex) {
                isValid = false;
                ingredientAmount.setError("Enter a positive number");
                Log.d("NumberFormatLog", "error on numberformat is " + ex.getMessage());
                ex.printStackTrace();
            }

            // check unit  input
            int unit = 0;
            try {
                if (!IngredientUnitInput.isEmpty()) {
                    unit = Integer.parseInt(IngredientUnitInput);

                    if (unit < 1) {
                        isValid = false;
                        ingredientUnit.setError("Enter a positive number");
                    }
                } else {
                    isValid = false;
                    ingredientUnit.setError("Enter a positive number");
                }
            } catch (NumberFormatException ex) {
                isValid = false;
                ingredientUnit.setError("Enter a positive number");
                ex.printStackTrace();
            }

            if (isValid) {
                if (isEdit) {
                    listener.onOkPressedEdit(
                            ingredient,
                            IngredientDescInput,
                            IngredientBestBeforeDateInput,
                            location,
                            amount,
                            unit,
                            IngredientCategoryInput);
                } else {
                    listener.onOkPressedAdd(new Ingredient(
                            IngredientDescInput,
                            IngredientBestBeforeDateInput,
                            location,
                            amount,
                            unit,
                            IngredientCategoryInput
                    ));
                }

                dialog.dismiss();
            }
        }
    }
}
