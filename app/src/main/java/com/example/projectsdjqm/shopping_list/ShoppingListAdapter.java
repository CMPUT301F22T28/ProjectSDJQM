package com.example.projectsdjqm.shopping_list;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientFragment;
import com.example.projectsdjqm.ingredient_storage.IngredientList;

import java.util.ArrayList;
import java.util.Date;

/**
 * ShoppingListAdapter:
 * Custom array adapter for shoppinglist
 */
public class ShoppingListAdapter extends ArrayAdapter<ShoppingList> {

    // attr init
    private ArrayList<ShoppingList> shoppingList;
    private Context context;

    // constructor
    public ShoppingListAdapter(Context context, ArrayList<ShoppingList> shoppingList) {
        super(context,0, shoppingList);
        this.context = context;
    }

    // view inflater for updating fields
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        ShoppingList shoppingList = getItem(position);
        Ingredient ingredient = shoppingList.getIngredient();
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.shopping_list_content, parent, false);

        TextView description = view.findViewById(R.id.shoppinglist_description);
        TextView category = view.findViewById(R.id.shoppinglist_category);
        TextView amount = view.findViewById(R.id.shoppinglist_amount);
        TextView unit = view.findViewById(R.id.shoppinglist_unit);
        CheckBox checkbox = view.findViewById(R.id.box);

        description.setText(ingredient.getIngredientDescription());

        category.setText(String.format("Category: %s", ingredient.getIngredientCategory()));
        amount.setText(String.format("Amount: %s", ingredient.getIngredientAmount()));
        unit.setText(String.format("Unit: %s", ingredient.getIngredientUnit()));

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (checkbox.isChecked()) {
//                    checkboxListener.onItemChecked(ingredient);
                    shoppingList.setPickedUp(true);
                    // As a meal planner, I want to note that I have picked up an ingredient on the
                    // shopping list. may need to change
                    new AlertDialog.Builder(getContext())
                            .setMessage("You have picked up sth ")
                            .setPositiveButton("Ok", null)
                            .show();
                } else {
                    shoppingList.setPickedUp(false);
                }
            }
        });

        return view;
    }
    
}
