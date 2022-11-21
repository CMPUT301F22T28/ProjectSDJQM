/**
 * RecipeFragment
 * @version 1
 * @author Qingya Ye
 */
package com.example.projectsdjqm.recipe_list;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
                             int preparationTime,
                             int servingNumber,
                             String comments,
                             String category,
                             Drawable photo,
                             ArrayList<Ingredient> list);
    }

    private EditText recipeTitle;
    // preparation time should change to a time selector? can discuss and decide in project part 4
//    private EditText recipePreparationTime;
    private EditText recipeServingNumber;
    private EditText recipeCategory;
    private EditText recipeComments;
    private TimePicker timePicker;
    private ImageView photo;
    private TextView ingredientText;
    private Recipe recipe;
    private boolean isEdit = false;
    private final int requestCodeForTakePhoto = 1;
    private final int requestCodeForChoosePhoto = 2;

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
            throw new RuntimeException(context + "This is not the correct fragment!");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = getLayoutInflater()
                .inflate(R.layout.recipe_add_fragment, null);
        recipeTitle = view.findViewById(R.id.edit_recipe_title);
//        recipePreparationTime = view.findViewById(R.id.edit_recipe_preparation_time);
        recipeServingNumber = view.findViewById(R.id.edit_recipe_servings);
        recipeCategory = view.findViewById(R.id.edit_recipe_category);
        recipeComments = view.findViewById(R.id.edit_recipe_comments);
        Button takePhotoButton = view.findViewById(R.id.take_photo);
        Button choosePhotoButton = view.findViewById(R.id.choose_from_album);
        Button ingredientSelectButton = view.findViewById(R.id.ingredient_select_button);
        photo = view.findViewById(R.id.recipe_image);
        ingredientText = view.findViewById(R.id.recipe_ingredient);
        timePicker = view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
// click edit, current recipe details show in fragment
        if (isEdit) {
            recipeTitle.setText(recipe.getTitle());
            recipeServingNumber.setText(String.valueOf(recipe.getNumberofServings()));
            recipeCategory.setText(recipe.getRecipeCategory());
            recipeComments.setText(recipe.getComments());
            photo.setImageDrawable(recipe.getPhotograph());
            ArrayList<Ingredient> list = recipe.getListofIngredients();

            int totalTime = recipe.getPreparationTime();
            int hours = totalTime / 60;
            int minutes = totalTime % 60;
            timePicker.setHour(hours);
            timePicker.setMinute(minutes);

            StringBuilder listText = new StringBuilder();
            for (int i=0; i<list.size(); i++) {
                listText.append(list.get(i).getIngredientDescription());
                listText.append(",\n");
            }
        }
        takePhotoButton.setOnClickListener(view12 -> {
            // have permission and start taking photo
            if (ContextCompat.checkSelfPermission(
                    getContext(),
                    Manifest.permission.CAMERA) == PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                            getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE) == PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(
                            getContext(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED){
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,requestCodeForTakePhoto);
            }
            // request for permissions if do not have
            else {
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        requestCodeForTakePhoto);
            }
        });

        choosePhotoButton.setOnClickListener(view1 -> {
            // request for permissions if do not have
            if (ContextCompat.checkSelfPermission(
                            getContext(),
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        requestCodeForChoosePhoto);
            } else {
                // have permission and open album to choose a photo
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, requestCodeForChoosePhoto);
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
//            String preparationTime = recipePreparationTime.getText().toString();
            int preparationTime_h = timePicker.getHour();
            int preparationTime_m = timePicker.getMinute();
            int preparationTime = preparationTime_h * 60 + preparationTime_m;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,

                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // take photo
        if (requestCode == requestCodeForTakePhoto) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,requestCodeForTakePhoto);
            }
        }
        // choose a photo
        else if (requestCode == requestCodeForChoosePhoto) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, requestCodeForChoosePhoto);
            }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == requestCodeForTakePhoto && resultCode == Activity.RESULT_OK) {
            assert data != null;
            Bitmap b =(Bitmap) data.getExtras().get("data");
//            Drawable drawable = new BitmapDrawable(getResources (), b);
//            photo.setImageDrawable(drawable);
            photo.setImageBitmap(b);

        }else if (requestCode == requestCodeForChoosePhoto && resultCode == Activity.RESULT_OK) {
            assert data != null;
            Uri uri = data.getData();
            photo.setImageURI(uri);
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
