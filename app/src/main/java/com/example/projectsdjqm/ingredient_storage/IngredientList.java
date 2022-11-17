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

/**
 * IngredientList:
 * Custom array adapter for ingredient
 */
public class IngredientList extends ArrayAdapter<Ingredient> {
    private IngredientButtonListener ingredientButtonListener;

    // Interface for ingredient button lister
    public interface IngredientButtonListener {
        void onEditIngredientClickListener(int position);
        void onDeleteIngredientClickListener(int position);
    }

    private final ArrayList<Ingredient> ingredientList;
    private final Context context;

    // Constructor for IngredientList
    public IngredientList(Context context, ArrayList<Ingredient> ingredientList) {
        super(context,0, ingredientList);
        this.context = context;
        this.ingredientList = ingredientList;
    }

    // Ingredient button listener
    public void setIngredientButtonListener(IngredientButtonListener ingredientButtonListener) {
        this.ingredientButtonListener = ingredientButtonListener;
    }

    // view manip
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
        TextView IngredientDescTextView = view.findViewById(R.id.ingredient_description);
        IngredientDescTextView.setText(ingredient.getIngredientDescription());

        
        // Get ingredient best before date
        TextView IngredientBestBeforeTextView = view.findViewById(R.id.ingredient_bestbeforedate);
        IngredientBestBeforeTextView.setText(getBestBeforeDate(ingredient));

        
        // Get ingredient location
        TextView IngredientLocationTextView = view.findViewById(R.id.ingredient_location);
        IngredientLocationTextView.setText(getLocation(ingredient));

        
        // Get ingredient amount
        TextView IngredientAmountTextView = view.findViewById(R.id.ingredient_amount);
        IngredientAmountTextView.setText(getAmount(ingredient));

        
        // Get ingredient unit
        TextView IngredientUnitTextView = view.findViewById(R.id.ingredient_unit);
        IngredientUnitTextView.setText(getUnit(ingredient));

        
        // Get ingredient category
        TextView IngredientCategoryTextView = view.findViewById(R.id.ingredient_category);
        IngredientCategoryTextView.setText(getCategory(ingredient));


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

    // Some cosmetics for the display:
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
        return String.format("Amount: %s", amount);
    }

    private String getUnit(Ingredient ingredient) {
        int unit = ingredient.getIngredientUnit();
        return String.format("Unit: %s", unit);
    }

    private String getCategory(Ingredient ingredient) {
        String category = ingredient.getIngredientCategory();
        return String.format("Category: %s", category);
    }

    public static class ViewHolder {
        Button editButton;
        Button deleteButton;
    }

    /**
     * this gets size of the list
     * @return
     */
    public int getCount(){
        return ingredientList.size();
    }

    /**
     * this adds a city object to the list
     *for the first phase it will be empty
     * @param ingredient
     */
    public void addIngredient(Ingredient ingredient){
        ingredientList.add(ingredient);
    }

    /**
     * this checks if list contains city object
     * @param ingredient
     * @return
     */
    public boolean hasIngredient(Ingredient ingredient) {
        return ingredientList.contains(ingredient);
    }

    /**
     * this method remove city from cities
     * @param ingredient
     */
    public void delete(Ingredient ingredient) {
        ingredientList.remove(ingredient);
    }

    /**
     * this gets size of the list
     * @return
     */
    public int countIngredients() {
        return ingredientList.size();
    }
}
