/**
 * IngredientActivity
 * @version 1.0
 * @author Muchen Li
 */
package com.example.projectsdjqm.ingredient_storage;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectsdjqm.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class IngredientActivity extends AppCompatActivity implements
        IngredientFragment.OnFragmentInteractionListener,
        IngredientList.IngredientButtonListener {

    ListView ingredientlistview;
    IngredientList ingredientAdapter;
    ArrayList<Ingredient> ingredientlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_main);

        ingredientlistview = findViewById(R.id.ingredient_list);

        ingredientlist = new ArrayList<>();
        ingredientAdapter = new IngredientList(this, ingredientlist);
        ingredientAdapter.setIngredientButtonListener(this);
        ingredientlistview.setAdapter(ingredientAdapter);

        // floating button for add Ingredient
        final FloatingActionButton addIngredientButton = findViewById(R.id.add_ingredient);
        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IngredientFragment addIngredientFragment = new IngredientFragment();
                addIngredientFragment.show(getSupportFragmentManager(), "ADD_INGREDIENT");
            }
        });
    }

    // Edit button triggered
    public void onEditIngredientClickListener(int position) {
        Ingredient currentIngredient = ingredientlist.get(position);
        IngredientFragment addIngredientFragment = new IngredientFragment(currentIngredient);
        addIngredientFragment.show(getSupportFragmentManager(), "EDIT_INGREDIENT");
    }

    // Delete button triggered
    public void onDeleteIngredientClickListener(int position) {
        ingredientlist.remove(position);
        ingredientAdapter.notifyDataSetChanged();
    }

    @Override
    public void onOkPressedAdd(Ingredient newIngredient) {
        ingredientAdapter.add(newIngredient);
    }

    @Override
    public void onOkPressedEdit(Ingredient ingredient, String description, Date bestbeforedate, Ingredient.Location location, int amount, int unit, String category) {
        ingredient.setIngredientDescription(description);
        ingredient.setIngredientBestBeforeDate(bestbeforedate);
        ingredient.setIngredientLocation(location);
        ingredient.setIngredientAmount(amount);
        ingredient.setIngredientUnit(unit);
        ingredient.setIngredientCategory(category);
    }

}
