/**
 * IngredientInRecipeAdapter:
 * This class defines a custom array adapter for Ingredient.
 * @author Qingya Ye
 * @version 1.0
 * @date Nov. 22nd, 2022
 */
package com.example.projectsdjqm.recipe_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import java.util.ArrayList;


public class IngredientInRecipeAdapter extends ArrayAdapter<Ingredient> {
    private final ArrayList<Ingredient> ingredientList;
    private final Context context;

    /* Constructor for IngredientList */
    /**
     * This is a constructor to create IngredientInRecipeAdapter object.
     * packagename.classname#IngredientInRecipeAdapter
     * @param context context
     * @param ingredientList data to be bound with an ListView
     */
    public IngredientInRecipeAdapter(Context context, ArrayList<Ingredient> ingredientList) {
        super(context,0, ingredientList);
        this.context = context;
        this.ingredientList = ingredientList;
    }

    /* Ingredient button listener */
    /**
     * This is an override method GetView
     * @param position Position of the data displayed by the view in the data set
     * @param view The old view to reuse, if possible
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @Nullable ViewGroup parent) {
        View view1 = LayoutInflater.from(context).inflate(R.layout.ingredient_in_recipe_fragment,null);

        // Get ingredient at this position
        Ingredient ingredient = ingredientList.get(position);

        // Get ingredient description
        TextView IngredientDescTextView = view1.findViewById(R.id.ing_description);
        IngredientDescTextView.setText(getDescription(ingredient));

        // Get ingredient amount
        TextView IngredientAmountTextView = view1.findViewById(R.id.ing_amount);
        IngredientAmountTextView.setText(getAmount(ingredient));

        // Get ingredient unit
        TextView IngredientUnitTextView = view1.findViewById(R.id.ing_unitost);
        IngredientUnitTextView.setText(getUnit(ingredient));

        // Get ingredient category

        TextView IngredientCategoryTextView = view1.findViewById(R.id.ing_category);
        IngredientCategoryTextView.setText(getCategory(ingredient));

        return view1;
    }

    /**
     * Some cosmetics for the display
     **/
    private  String getDescription(Ingredient ingredient) {
        String description = ingredient.getIngredientDescription();
        return String.format("%s, ",description);
    }
    private String getAmount(Ingredient ingredient) {
        int amount = ingredient.getIngredientAmount();
        return String.format("%s x ", amount);
    }

    private String getUnit(Ingredient ingredient) {
        String unit = ingredient.getIngredientUnit();
        return String.format("unit: %s, ", unit);
    }

    private String getCategory(Ingredient ingredient) {
        String category = ingredient.getIngredientCategory();
        return String.format("category: %s", category);
    }

}
