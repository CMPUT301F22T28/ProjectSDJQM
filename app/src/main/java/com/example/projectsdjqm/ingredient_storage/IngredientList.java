/**
 *  Classname: FoodList
 *
 *  Version information: version 1
 *
 *  Date: 2022/09/23
 *
 *  Copyright notice: All rights reserved, used by permission of Muchen Li.
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
public class IngredientList extends ArrayAdapter<ingredient> {
    private FoodButtonListener foodButtonListener;

    /* Interface for food button lister*/
    public interface FoodButtonListener {
        void onEditFoodClickListener(int position);
        void onDeleteFoodClickListener(int position);
    }

    private final ArrayList<ingredient> ingredientList;
    private final Context context;

    /* Constructor for FoodList */
    public IngredientList(Context context, ArrayList<ingredient> ingredientList) {
        super(context,0, ingredientList);
        this.context = context;
        this.ingredientList = ingredientList;
    }

    /* Food button listener */
    public void setFoodButtonListener(FoodButtonListener foodButtonListener) {
        this.foodButtonListener = foodButtonListener;
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

        // Get Food at this position
        ingredient ingredient = ingredientList.get(position);

        // Get ingredient description
        TextView IngredientDescTextView = view.findViewById(R.id.ingredient_description);
        IngredientDescTextView.setText(ingredient.getDescription());

        // Get ingredient best before date
        TextView IngredientBestBeforeTextView = view.findViewById(R.id.ingredient_bestbeforedate);
        IngredientBestBeforeTextView.setText(getBestBeforeDate(ingredient));

        // Get ingredient location
        TextView IngredientLocationTestView = view.findViewById(R.id.ingredient_location);
        IngredientLocationTestView.setText(getLocation(ingredient));

        // Get ingredient amount
        TextView IngredientAmountTestView = view.findViewById(R.id.ingredient_amount);
        IngredientAmountTestView.setText(getCount(ingredient));

        // Get ingredient unit
        TextView IngredientUnitTestView = view.findViewById(R.id.ingredient_unit);
        IngredientUnitTestView.setText(getUnitCost(ingredient));

        // Get ingredient category
        TextView IngredientCategoryTestView = view.findViewById(R.id.ingredient_category);
        IngredientCategoryTestView.setText(ingredient.getIngredientCategory());

        // Edit and Delete buttons
        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (foodButtonListener != null) {
                    foodButtonListener.onEditFoodClickListener(position);
                }
            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodButtonListener != null) {
                    foodButtonListener.onDeleteFoodClickListener(position);
                }
            }
        });
        return view;
    }

    /**
     * Some cosmetics for the display
     * */
    private String getBestBeforeDate(ingredient ingredient) {
        Date date = ingredient.getBestBeforeDate();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return String.format("Best Before Date: %s", dateFormat.format(date));
    }

    private String getLocation(ingredient ingredient) {
        ingredient.Location location = ingredient.getLocation();
        return String.format("Location: %s", location);
    }

    private String getCount(ingredient ingredient) {
        int amount = ingredient.getAmount();
        return String.format("Amount: %s", amount);
    }

    private String getUnitCost(ingredient ingredient) {
        int unit = ingredient.getUnit();
        return String.format("Unit: %s", unit);
    }

    public static class ViewHolder {
        Button editButton;
        Button deleteButton;
    }

}
