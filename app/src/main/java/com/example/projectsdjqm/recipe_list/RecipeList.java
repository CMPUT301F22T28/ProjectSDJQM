package com.example.projectsdjqm.recipe_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientList;

import java.util.ArrayList;

public class RecipeList extends ArrayAdapter<Recipe> {
    private RecipeButtonListener recipeButtonListener;

    public interface RecipeButtonListener {
        void onEditRecipeClickListener(int position);
        void onDeleteRecipeClickListener(int position);
    }

    private final ArrayList<Recipe> recipeList;
    private final Context context;

    public RecipeList(@NonNull Context context, ArrayList<Recipe> recipeList) {
        super(context, 0, recipeList);
        this.context = context;
        this.recipeList = recipeList;
    }
    /* Ingredient button listener */
    public void setRecipeButtonListener(RecipeButtonListener recipeButtonListener) {
        this.recipeButtonListener = recipeButtonListener;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable android.view.View view, @Nullable ViewGroup parent) {

        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.recipe_content, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.editButton = (Button) view.findViewById(R.id.recipe_edit);
            viewHolder.deleteButton = (Button) view.findViewById(R.id.recipe_delete);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // Get recipe at this position
        Recipe recipe = recipeList.get(position);

        TextView titleTextView = view.findViewById(R.id.recipe_title);
        TextView preparationTimeTextView = view.findViewById(R.id.recipe_preparation_time);
        TextView servingNumberTextView = view.findViewById(R.id.recipe_serving_number);
        TextView categoryTextView = view.findViewById(R.id.recipe_category);
        TextView commentsTextView = view.findViewById(R.id.recipe_comments);
        ImageView photographImageView = view.findViewById(R.id.recipe_photograph);
        TextView listOfIngredientsTextView = view.findViewById(R.id.recipe_ingredient_list);

        titleTextView.setText(recipe.getTitle());
        preparationTimeTextView.setText(String.format("Preparation Time: %s",recipe.getPreparationTime()));
        servingNumberTextView.setText(String.format("Serving Number: %s", recipe.getNumberofServings()));
        categoryTextView.setText(String.format("Category: %s", recipe.getRecipeCategory()));
        commentsTextView.setText(String.format("Comments:\n%s", recipe.getComments()));
        photographImageView.setImageDrawable(recipe.getPhotograph());
        ArrayList<Ingredient> list = recipe.getListofIngredients();
        String text = "\n";
        String amount = "";
        for (int i=0; i<list.size(); i++) {
            String temp = list.get(i).getIngredientDescription().toString();
//            String temp_amount = list.get(i).get
            text += temp;
            text += " ,\n";
        }
        listOfIngredientsTextView.setText(String.format("Needed Ingredients: %s", text));

        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (recipeButtonListener != null) {
                    recipeButtonListener.onEditRecipeClickListener(position);
                }
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (recipeButtonListener != null) {
                    recipeButtonListener.onDeleteRecipeClickListener(position);
                }
            }
        });

        return view;
    }

    public static class ViewHolder {
        Button editButton;
        Button deleteButton;
    }

}
