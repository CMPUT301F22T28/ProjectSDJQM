package com.example.projectsdjqm.ingredient_storage;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.projectsdjqm.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;


public class ingredient_main extends AppCompatActivity implements
        ingredientFragment.OnFragmentInteractionListener,
        IngredientList.FoodButtonListener {

    ListView ingredientlistview;
    IngredientList ingredientAdapter;
    ArrayList<ingredient> ingredientlist;
    TextView totalcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_main);

        ingredientlistview = findViewById(R.id.ingredient_list);
        // Calculate the total cost at bottom of the screen
        totalcount = findViewById(R.id.total_cost);

        ingredientlist = new ArrayList<>();
        ingredientAdapter = new IngredientList(this, ingredientlist);
        ingredientAdapter.setFoodButtonListener(this);
        ingredientlistview.setAdapter(ingredientAdapter);

        // floating button for add food
        final FloatingActionButton addFoodButton = findViewById(R.id.add_ingredient);
        addFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientFragment addFoodFragment = new ingredientFragment();
                addFoodFragment.show(getSupportFragmentManager(), "ADD_INGREDIENT");
            }
        });
        total_cost();
    }

    // Edit button triggered
    public void onEditFoodClickListener(int position) {
        ingredient currentFood = ingredientlist.get(position);
        ingredientFragment addFoodFragment = new ingredientFragment(currentFood);
        addFoodFragment.show(getSupportFragmentManager(), "EDIT_FOOD");
    }

    // Delete button triggered
    public void onDeleteFoodClickListener(int position) {
        ingredientlist.remove(position);
        ingredientAdapter.notifyDataSetChanged();
        total_cost();
    }

    // Add a new food
    @Override
    public void onOkPressedAdd(ingredient newFood) {
        ingredientAdapter.add(newFood);
        total_cost();
    }

    // Edit a food
    @Override
    public void onOkPressedEdit(ingredient food,
                                String description,
                                Date bestbeforedate,
                                ingredient.Location location,
                                int amount,
                                int unit,
                                String category) {
        food.setDescription(description);
        food.setBestBeforeDate(bestbeforedate);
        food.setLocation(location);
        food.setAmount(amount);
        food.setUnit(unit);
        food.setIngredientCategory(category);

        ingredientAdapter.notifyDataSetChanged();
        total_cost();
    }

    // Calculate the total cost of the whole list
    private void total_cost() {
        int cost = 0;

        for (ingredient food : ingredientlist) {
            cost += food.getAmount() * food.getUnit();
        }

        totalcount.setText(String.format("Total cost($): %d", cost));
    }
}