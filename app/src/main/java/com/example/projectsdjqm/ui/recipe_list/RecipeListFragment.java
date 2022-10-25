package com.example.projectsdjqm.ui.recipe_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectsdjqm.databinding.FragmentMealPlanBinding;
import com.example.projectsdjqm.databinding.FragmentRecipeListBinding;
import com.example.projectsdjqm.ui.meal_plan.MealPlanViewModel;

public class RecipeListFragment extends Fragment {

    private FragmentRecipeListBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        RecipeListViewModel recipeListViewModel =
                new ViewModelProvider(this).get(RecipeListViewModel.class);

        binding = FragmentRecipeListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textRecipeList;
        recipeListViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}