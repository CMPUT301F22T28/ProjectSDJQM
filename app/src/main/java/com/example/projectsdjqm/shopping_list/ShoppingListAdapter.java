/**
 * ShoppingList
 * @version 2.1
 * @author  Muchen Li, Qingya Ye
 * @date Nov 25, 2022
 * shoppinglist class
 */
package com.example.projectsdjqm.shopping_list;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
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
import java.util.ArrayList;

/**
 * ShoppingListAdapter:
 * Custom array adapter for shoppinglist
 */
public class ShoppingListAdapter extends ArrayAdapter<ShoppingList> {
    private final String TAG = "Picked up";
    private Context context;

    /**
     * This is a constructor to create ShoppingListAdapter
     * @param context
     * @param shoppingList
     */
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
                    new AlertDialog.Builder(getContext())
                            .setMessage("You have picked up "+(shoppingList.getIngredient().getIngredientDescription()+"."))
                            .setPositiveButton("Ok",null)
                            .show();
                            Log.d(TAG,"picked up "+shoppingList.getIngredient().getIngredientDescription()+" "+shoppingList.getPickedUp());
                } else {
                    shoppingList.setPickedUp(false);
                    Log.d(TAG,"picked up "+shoppingList.getIngredient().getIngredientDescription()+" "+shoppingList.getPickedUp());
                }
            }
        });
        return view;
    }
    
}
