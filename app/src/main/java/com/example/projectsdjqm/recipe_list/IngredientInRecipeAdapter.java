// IngredientAdapter
/**
 * IngredientList
 * Custom array adapter for Ingredient
 *
 * @version 1.1
 * @update fixed some minor bugs
 * @author Muchen Li
 * @date Oct 30, 2022
 *
 * @version 1.0
 * @author Muchen Li
 * @date Oct 27, 2022
 */
package com.example.projectsdjqm.recipe_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/* Create custom array adapter for Ingredient */
public class IngredientInRecipeAdapter extends ArrayAdapter<Ingredient> {
//    private IngredientButtonListener ingredientButtonListener;
//
//    /* Interface for ingredient button lister*/
//    public interface IngredientButtonListener {
//        void onEditIngredientClickListener(int position);
//        void onDeleteIngredientClickListener(int position);
//    }

    private final ArrayList<Ingredient> ingredientList;
    private final Context context;

    /* Constructor for IngredientList */
    public IngredientInRecipeAdapter(Context context, ArrayList<Ingredient> ingredientList) {
        super(context,0, ingredientList);
        this.context = context;
        this.ingredientList = ingredientList;
    }

    /* Ingredient button listener */
//    public void setIngredientButtonListener(IngredientButtonListener ingredientButtonListener) {
//        this.ingredientButtonListener = ingredientButtonListener;
//    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @Nullable ViewGroup parent) {
//        ViewHolder viewHolder;
        View view1 = LayoutInflater.from(context).inflate(R.layout.ingredient_in_recipe_fragment,null);
//        if (view == null) {
//            LayoutInflater inflater = LayoutInflater.from(context);
//            view = inflater.inflate(R.layout.ingredient_in_recipe_content, parent, false);

//            viewHolder = new ViewHolder();
//            viewHolder.editButton = (Button) view.findViewById(R.id.edit_button);
//            viewHolder.deleteButton = (Button) view.findViewById(R.id.delete_button);
//
//            view.setTag(viewHolder);
//        }
//        else {
//            viewHolder = (ViewHolder) view.getTag();
//        }

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


        // Edit and Delete buttons
//        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                if (ingredientButtonListener != null) {
//                    ingredientButtonListener.onEditIngredientClickListener(position);
//                }
//            }
//        });
//
//        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (ingredientButtonListener != null) {
//                    ingredientButtonListener.onDeleteIngredientClickListener(position);
//                }
//            }
//        });
        return view1;
    }

    /**
     * Some cosmetics for the display
     * */

    private  String getDescription(Ingredient ingredient) {
        String description = ingredient.getIngredientDescription();
        return String.format("%s, ",description);
    }
    private String getAmount(Ingredient ingredient) {
        int amount = ingredient.getIngredientAmount();
        return String.format("%s x ", amount);
    }

    private String getUnit(Ingredient ingredient) {
        int unit = ingredient.getIngredientUnit();
        return String.format("unit cost: $%s, ", unit);
    }

    private String getCategory(Ingredient ingredient) {
        String category = ingredient.getIngredientCategory();
        return String.format("category: %s", category);
    }

//    public static class ViewHolder {
//        Button editButton;
//        Button deleteButton;
//    }
}
