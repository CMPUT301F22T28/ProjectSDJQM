/**
 *  IngredientList
 *  @version 1.0
 *  @author Muchen Li
 */
package com.example.projectsdjqm.ingredient_storage;

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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/* Create custom array adapter for Food */
public class IngredientList extends ArrayAdapter<Ingredient> {
    private IngredientButtonListener ingredientButtonListener;

    /* Interface for food button lister*/
    public interface IngredientButtonListener {
        void onEditIngredientClickListener(int position);
        void onDeleteIngredientClickListener(int position);
    }

    private final ArrayList<Ingredient> ingredientList;
    private final Context context;

    /* Constructor for FoodList */
    public IngredientList(Context context, ArrayList<Ingredient> ingredientList) {
        super(context,0, ingredientList);
        this.context = context;
        this.ingredientList = ingredientList;
    }

    /* Food button listener */
    public void setIngredientButtonListener(IngredientButtonListener ingredientButtonListener) {
        this.ingredientButtonListener = ingredientButtonListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @Nullable ViewGroup parent) {
        ViewHolder viewHolder;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.ingredient_content, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.editButton = (Button) view.findViewById(R.id.edit_button);
            viewHolder.deleteButton = (Button) view.findViewById(R.id.delete_button);

            view.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) view.getTag();
        }

        // Get ingredient at this position
        Ingredient ingredient = ingredientList.get(position);

        // Get ingredient description
        TextView FoodDescTextView = view.findViewById(R.id.ingredient_description);
        FoodDescTextView.setText(ingredient.getIngredientDescription());

        // Get ingredient best before date
        TextView IngredientBestBeforeTextView = view.findViewById(R.id.ingredient_bestbeforedate);
        IngredientBestBeforeTextView.setText(getBestBeforeDate(ingredient));

        // Get ingredient location
        TextView FoodLocationTextView = view.findViewById(R.id.ingredient_location);
        FoodLocationTextView.setText(getLocation(ingredient));

        // Get ingredient amount
        TextView FoodCountTextView = view.findViewById(R.id.ingredient_count);
        FoodCountTextView.setText(getAmount(ingredient));

        // Get ingredient unit
        TextView FoodUnitCostTextView = view.findViewById(R.id.ingredient_unitcost);
        FoodUnitCostTextView.setText(getUnit(ingredient));

        // Get ingredient category
        TextView FoodCategoryTextView = view.findViewById(R.id.ingredient_category);
        FoodDescTextView.setText(getCategory(ingredient));

        // Edit and Delete buttons
        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (ingredientButtonListener != null) {
                    ingredientButtonListener.onEditIngredientClickListener(position);
                }
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ingredientButtonListener != null) {
                    ingredientButtonListener.onDeleteIngredientClickListener(position);
                }
            }
        });
        return view;
    }

    /**
     * Some cosmetics for the display
     * */
    private String getBestBeforeDate(Ingredient ingredient) {
        Date date = ingredient.getIngredientBestBeforeDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("Best Before Date: %s", dateFormat.format(date));
    }

    private String getLocation(Ingredient ingredient) {
        Ingredient.Location location = ingredient.getIngredientLocation();
        return String.format("Location: %s", location);
    }

    private String getAmount(Ingredient ingredient) {
        int amount = ingredient.getIngredientAmount();
        return String.format("Count: %s", amount);
    }

    private String getUnit(Ingredient ingredient) {
        int unit = ingredient.getIngredientUnit();
        return String.format("Unit Cost($): %s", unit);
    }

    private String getCategory(Ingredient ingredient) {
        String category = ingredient.getIngredientCategory();
        return String.format("Category: %s", category);
    }

    public static class ViewHolder {
        Button editButton;
        Button deleteButton;
    }
}
