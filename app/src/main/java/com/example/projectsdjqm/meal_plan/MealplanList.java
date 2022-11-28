package com.example.projectsdjqm.meal_plan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.projectsdjqm.MainActivity;
import com.example.projectsdjqm.R;
import com.example.projectsdjqm.ingredient_storage.Ingredient;
import com.example.projectsdjqm.ingredient_storage.IngredientList;
import com.example.projectsdjqm.recipe_list.Recipe;
import com.example.projectsdjqm.recipe_list.RecipeList;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MealplanList aims to create an arrayadapter that matches
 * our desired layout of Mealplan class
 * @version 3.5
 * @author Jianming Ma
 * @date Nov.23rd, 2022
 */
public class MealplanList extends ArrayAdapter<Mealplan> {
    // attr init
    private final ArrayList<Mealplan> mealplanList;

    /**
     * Interface for edit / delete listeners
     */
    public interface recipeScaleListener {
        /**
         * This method that is called when the listview of recipe scale list
         * is pressed
         * @param position index of clicked element within scale list
         * @param mealplan_position index of clicked element within mealplan list
         */
        void onRecipeScaleListPressed(int position, int mealplan_position);
    }

    private final Context context;
    private recipeScaleListener scalelistener;
    private Integer seletedItem;
    private Integer mealplan_position;


    /**
     * This is a constructor to create MealplanList object.
     * packagename.classname#MealplanList
     * @param context Context
     * @param mealplanList  data to be bounded with a ListView
     */
    public MealplanList(Context context, ArrayList<Mealplan> mealplanList) {
        super(context, 0, mealplanList);
        this.mealplanList = mealplanList;
        this.context = context;
    }

    /**
     * This is the setter of listener
     * @param scalelistener scalelistener
     */
    public void setScalelistener(recipeScaleListener scalelistener) {
        this.scalelistener = scalelistener;
    }

    /**
     * This is an override method GetView
     * @param position Position of the data displayed by the view in the data set
     * @param convertView The old view to reuse, if possible
     * @param parent The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.mealplan_content, parent, false);
        }
//        will implement the meal plan content depending on the recipe list here
        Mealplan mealplan = mealplanList.get(position);
        mealplan_position = position;

        // Set mealplan Date
        TextView mealplan_date = view.findViewById(R.id.mealplan_date);
        mealplan_date.setText(get_mealplan_Date(mealplan));

        // Set recipe list of one day's meal plan
        ListView recipeListview = view.findViewById(R.id.mealplan_r_list);
        ArrayList<Recipe> recipeList = mealplan.getRecipeList();
        ArrayList<String> recipeList_str = new ArrayList<String>();
        for (Recipe rec : recipeList) {
            recipeList_str.add(rec.getTitle());
        }
        ArrayAdapter<String> recipeAdapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, recipeList_str);
        recipeListview.setAdapter(recipeAdapter);
        setListViewHeightBasedOnChildren(recipeListview);

        // Set ingredient list of one day's meal plan
        ListView ingredientListview = view.findViewById(R.id.mealplan_in_list);
        ArrayList<Ingredient> ingredientList = mealplan.getIngredientList();
        ArrayList<String> ingredientList_str = new ArrayList<String>();
        for (Ingredient ingre : ingredientList) {
            ingredientList_str.add(ingre.getIngredientDescription());
        }
        ArrayAdapter<String> ingredientAdapter =
                new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, ingredientList_str);
        ingredientListview.setAdapter(ingredientAdapter);
        setListViewHeightBasedOnChildren(ingredientListview);

        // Set recipe scale list
        ListView recipe_scale_view = view.findViewById(R.id.recipe_scale);
        ArrayList<Integer> recipe_scale_list = mealplan.getRecipeScale();
        ArrayAdapter<Integer> scaleAdapter =
                new ArrayAdapter<Integer>(context, android.R.layout.simple_list_item_1, recipe_scale_list);
        recipe_scale_view.setAdapter(scaleAdapter);
        recipe_scale_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (scalelistener != null) {
                    scalelistener.onRecipeScaleListPressed(position,mealplan_position);
                }
            }
        });
        setListViewHeightBasedOnChildren(recipe_scale_view);
        return view;
    }

    /**
     * This is a method to return string type of mealplan date
     * @param mealplan certain mealplan instance
     * @return string format of mealplan date
     */
    private String get_mealplan_Date(Mealplan mealplan) {
        Date date = mealplan.getMealplan_date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return String.format(dateFormat.format(date));
    }

    /**
     * This method that is called after adapter is set in order to adjust listview layout
     * @param listView listview of certain arraylist
     */
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
}
