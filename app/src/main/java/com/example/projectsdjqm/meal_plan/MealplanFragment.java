package com.example.projectsdjqm.meal_plan;

import static android.content.Intent.getIntent;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.recipe_list.Recipe;
import com.example.projectsdjqm.recipe_list.RecipeFragment;
import com.example.projectsdjqm.recipe_list.RecipeList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MealplanFragment extends DialogFragment {
    public interface OnFragmentInteractionListener {
        void onOkPressedAdd(Mealplan mealplan);
    }
    public interface FragmentAListener{
        void onInputASent(CharSequence input);
    }

    // attr init
    private Mealplan mealplan;
    private MealplanFragment.OnFragmentInteractionListener listener;
    private DatePicker mealplan_date_view;
    private ListView recipeListview;
    private ListView ingredientListview;
    private String selectedItem;


    public MealplanFragment(Mealplan mealplan) {
        super();
        this.mealplan = mealplan;
    }
    public MealplanFragment() {
        super();

    }

    // fragment interaction listener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof MealplanFragment.OnFragmentInteractionListener) {
            listener = (MealplanFragment.OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString() + "This is not the correct fragment!");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String strtext = getArguments().getString("edttext");
        return inflater.inflate(R.layout.mealplan_add_fragment , container, false);
    }

    // layout inflater to update fields
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.mealplan_add_fragment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        mealplan_date_view = view.findViewById(R.id.mealplan_date_picker);
        recipeListview = view.findViewById(R.id.mealplan_r_add_list);
        ingredientListview = view.findViewById(R.id.mealplan_in_add_list);



//        selectedItem = getArguments().getString("edttext");

        Button recipe_add_button = (Button) view.findViewById(R.id.mealplan_r_add_button);
        recipe_add_button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

//                MealplanStorageFragment fragment = new MealplanStorageFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.fragmentContainerView, fragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
                Intent intent = new Intent();
                intent.setClass(getActivity(), MealplanStorageFragment.class);
                getActivity().startActivity(intent);
            }
        });

        return builder
                .setView(view)
                .setTitle("Adding Meal Plan")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Calendar calendar = new GregorianCalendar(
                                mealplan_date_view.getYear(),
                                mealplan_date_view.getMonth(),
                                mealplan_date_view.getDayOfMonth());
                        Date mealplan_date = new Date(calendar.getTimeInMillis());
                        ArrayList<Ingredient> ingredientList = new ArrayList<>();
                        ArrayList<Recipe> recipeList = new ArrayList<>();


                        listener.onOkPressedAdd(new Mealplan(recipeList,ingredientList,mealplan_date));
                    }
                }).create();


    }

}
