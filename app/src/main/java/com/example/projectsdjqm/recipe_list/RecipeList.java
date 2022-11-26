/**
 * RecipeList
 * @version 1
 * @author Qingya Ye
 */
package com.example.projectsdjqm.recipe_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;

import java.util.ArrayList;

// setListViewHeightBasedOnChildren reference: https://blog.csdn.net/qq_40543575/article/details/
// 90293306?spm=1001.2101.3001.6650.2&utm_medium=distribute.pc_relevant.none-task-blog-
// 2%7Edefault%7ECTRLIST%7ERate-2-90293306-blog-122808771.pc_relevant_multi_platform_
// whitelistv3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7ECTRLIST%
// 7ERate-2-90293306-blog-122808771.pc_relevant_multi_platform_whitelistv3&utm_relevant_index=3

/**
 * RecipeList:
 * Custom array adapter for the recipe list
 * @author Qingya Ye
 * @version 1.1
 */
public class RecipeList extends ArrayAdapter<Recipe> {
    private RecipeButtonListener recipeButtonListener;

    /**
     * Interface for edit / delete listeners
     */
    public interface RecipeButtonListener {
        void onEditRecipeClickListener(int position);
        void onDeleteRecipeClickListener(int position);
    }

    // final attr
    private final ArrayList<Recipe> recipeList;
    private final Context context;

    /**
     * This is a constructor to create RecipeList object.
     * packagename.classname#RecipeList
     * @param context Context
     * @param recipeList  data to be bounded with a ListView
     */
    public RecipeList(@NonNull Context context, ArrayList<Recipe> recipeList) {
        super(context, 0, recipeList);
        this.context = context;
        this.recipeList = recipeList;
    }

    // Ingredient button listener
    public void setRecipeButtonListener(RecipeButtonListener recipeButtonListener) {
        this.recipeButtonListener = recipeButtonListener;
    }

    /**
     * This is an override method GetView
     * @param position Position of the data displayed by the view in the data set
     * @param view The old view to reuse, if possible
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position
     */
    @NonNull
    @Override
    public View getView(int position,
                        @Nullable android.view.View view,
                        @Nullable ViewGroup parent) {

        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.recipe_content, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.editButton = view.findViewById(R.id.recipe_edit);
            viewHolder.deleteButton = view.findViewById(R.id.recipe_delete);

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
        ArrayList<Ingredient> ingredientList = recipe.getListofIngredients();
//        ArrayList<Ingredient> ingredientList = new ArrayList<Ingredient>();
//        ingredientList.add(new Ingredient("aaple",null,null,2,2,"category"));
        ListView ingredientListview = view.findViewById(R.id.ingredient_list_onRecipe);
        IngredientInRecipeAdapter adapter = new IngredientInRecipeAdapter(getContext(),ingredientList);
        ingredientListview.setAdapter(adapter);
        setListViewHeightBasedOnChildren(ingredientListview);

        titleTextView.setText(recipe.getTitle());
        preparationTimeTextView.setText(
                String.format("Preparation Time: %s minutes",recipe.getPreparationTime()));
        servingNumberTextView.setText(
                String.format("Serving Size: %s", recipe.getNumberofServings()));
        categoryTextView.setText(String.format("Category: %s", recipe.getRecipeCategory()));
        commentsTextView.setText(String.format("Comments:\n%s", recipe.getComments()));
        photographImageView.setImageDrawable(recipe.getPhotograph());

        listOfIngredientsTextView.setText(String.format("Ingredients: "));

        viewHolder.editButton.setOnClickListener(view1 -> {
            if (recipeButtonListener != null) {
                recipeButtonListener.onEditRecipeClickListener(position);
            }
        });

        viewHolder.deleteButton.setOnClickListener(view12 -> {
            if (recipeButtonListener != null) {
                recipeButtonListener.onDeleteRecipeClickListener(position);
            }
        });

        return view;
    }

    public static class ViewHolder {
        Button editButton;
        Button deleteButton;
    }

    /**
     * This is a method that calculate and set the width and height of a ListView
     * @param listView the candidate listview to be used in calculation
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // get the corresponding adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            // calculate width and height of sub items
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
