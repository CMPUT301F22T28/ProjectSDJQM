package com.example.projectsdjqm.recipe_list;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;

import java.util.ArrayList;
import java.util.Date;

public class RecipeFragment extends DialogFragment {
    public interface OnFragmentInteractionListener {
        void onOkPressedAdd(Recipe recipe);
        void onOkPressedEdit(Recipe recipe,
                             String title,
                             String preparationTime,
                             int servingNumber,
                             String comments,
                             String category,
                             Drawable photo,
                             ArrayList<Ingredient> list);
    }

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
    private Recipe recipe;
    private boolean isEdit = false;

    private OnFragmentInteractionListener listener;

    public RecipeFragment(Recipe recipe) {
        super();
        this.recipe = recipe;
        this.isEdit = true;
    }
    public RecipeFragment() {
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
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.recipe_add_fragment, null);
        recipeTitle = view.findViewById(R.id.edit_recipe_title);
        recipePreparationTime = view.findViewById(R.id.edit_recipe_preparation_time);
        recipeServingNumber = view.findViewById(R.id.edit_recipe_servings);
        recipeCategory = view.findViewById(R.id.edit_recipe_category);
        recipeComments = view.findViewById(R.id.edit_recipe_comments);
        photoSelectButton = view.findViewById(R.id.photo_select_button);
        ingredientSelectButton = view.findViewById(R.id.ingredient_select_button);
        photo = view.findViewById(R.id.recipe_image);
        ingredientText = view.findViewById(R.id.recipe_ingredient);
// click edit, current recipe details show in fragment
        if (isEdit) {
            recipeTitle.setText(recipe.getTitle());
            recipePreparationTime.setText(recipe.getPreparationTime());
            recipeServingNumber.setText(String.valueOf(recipe.getNumberofServings()));
            recipeCategory.setText(recipe.getRecipeCategory());
            recipeComments.setText(recipe.getComments());
            photo.setImageDrawable(recipe.getPhotograph());
            ArrayList<Ingredient> list = recipe.getListofIngredients();
            String listText = "";
            for (int i=0; i<list.size(); i++) {
                listText += list.get(i).getIngredientDescription();
                listText += ",\n";
            }
        }
        photoSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Test")
                        .setPositiveButton("Ok", null)
                        .show();
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        new ActivityResultCallback<ActivityResult>() {
                            @Override
                            public void onActivityResult(ActivityResult result) {
                                if (result.getResultCode() == 2) {
                                    if (result.getData().getClipData() != null) {
                                        Uri uri = result.getData().getClipData().getItemAt(1).getUri();
                                        photo.setImageURI(uri);
                                    }
                                }
                            }
                });
            }
        });
        ingredientSelectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setMessage("Test")
                        .setPositiveButton("Ok", null)
                        .show();
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog alertDialog = builder
                .setView(view)
                .setTitle("Add Recipe")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", null)
                .create();
        alertDialog.show();

        Button okButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        okButton.setOnClickListener(new CustomListener(alertDialog));

        return alertDialog;
    }

    class CustomListener implements View.OnClickListener {
        private final Dialog dialog;

        public CustomListener(Dialog dialog) {
            this.dialog = dialog;
        }

        @Override
        public void onClick(View v) {
            boolean isValid = true;

            String title = recipeTitle.getText().toString();
            String preparationTime = recipePreparationTime.getText().toString();
            String servingNumber = recipeServingNumber.getText().toString();
            String category = recipeCategory.getText().toString();
            String comments = recipeComments.getText().toString();
            Drawable photograph = photo.getDrawable();
            // need to change --------------------------
            Ingredient i = new Ingredient("a",new Date(),Ingredient.Location.Pantry,2,1,"category");
            ArrayList<Ingredient> list = new ArrayList<>();
            list.add(i);
            // need to change --------------------------

            // check title
            if (title.length() < 1) {
                isValid = false;
                recipeTitle.setError("Enter a title");
            }

            // check category
            if (category.length() < 1) {
                isValid = false;
                recipeCategory.setError("Enter a category");
            }

            //check preparation time
            int time = 0;
            try {
                if (!preparationTime.isEmpty()) {
                    time = Integer.parseInt(preparationTime);

                    if (time < 1) {
                        isValid = false;
                        recipePreparationTime.setError("Enter a positive number");
                    }
                } else {
                    isValid = false;
                    recipePreparationTime.setError("Enter a positive number");
                }
            } catch (NumberFormatException ex) {
                isValid = false;
                recipePreparationTime.setError("Enter a positive number");
                Log.d("NumberFormatLog", "error on numberformat is " + ex.getMessage());
                ex.printStackTrace();
            }
            int serving = 0;
            try {
                if (!servingNumber.isEmpty()) {
                    serving = Integer.parseInt(servingNumber);
                    if (serving < 1) {
                        isValid = false;
                        recipeServingNumber.setError("Enter a positive number");
                    }
                } else {
                    isValid = false;
                    recipeServingNumber.setError("Enter a positive number");
                }
            } catch (NumberFormatException ex) {
                isValid = false;
                recipeServingNumber.setError("Enter a positive number");
                Log.d("NumberFormatLog", "error on numberformat is " + ex.getMessage());
                ex.printStackTrace();
            }

            if (isValid) {
                if (!isEdit) {
                    listener.onOkPressedAdd(new Recipe(
                            title,
                            preparationTime,
                            serving,
                            category,
                            comments,
                            photograph,
                            list
                    ));
                } else {
                    listener.onOkPressedEdit(
                            recipe,
                            title,
                            preparationTime,
                            serving,
                            category,
                            comments,
                            photograph,
                            list
                    );
                }
                dialog.dismiss();
            }
        }
    }

}
