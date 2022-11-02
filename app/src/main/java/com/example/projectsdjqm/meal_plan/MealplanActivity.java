package com.example.projectsdjqm.meal_plan;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projectsdjqm.MainActivity;
import com.example.projectsdjqm.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;

public class MealplanActivity extends AppCompatActivity implements MealPlanFragment.OnFragmentIteractionListener{

    ListView mealplanlistview;
    MealplanList mealplanAdapter;
    ArrayList<Mealplan> mealplanlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealplan_activity);

        mealplanlistview = findViewById(R.id.meal_plan_list);

        mealplanlist = new ArrayList<>();
        mealplanAdapter = new MealplanList(this, mealplanlist);
        mealplanlistview.setAdapter(mealplanAdapter);

        // floating button for add meal plan
        final FloatingActionButton addmealplanButton = findViewById(R.id.add_mealplan_button);
        addmealplanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MealPlanFragment addmealplanFragment = new MealPlanFragment();
                MealPlanFragment.show(getSupportFragmentManager(), "ADD_INGREDIENT");
            }
        });

//        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_view);
//        navigation.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()) {
//                    case R.id.navigation_home:
//                        Intent a = new Intent(IngredientActivity.this, MainActivity.class);
//                        startActivity(a);
//                        break;
//                }
//                return false;
//            }
//        });

    }







}
