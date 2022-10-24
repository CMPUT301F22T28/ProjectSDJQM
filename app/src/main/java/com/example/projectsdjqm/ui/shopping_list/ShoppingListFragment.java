package com.example.projectsdjqm.ui.shopping_list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectsdjqm.databinding.FragmentRecipeListBinding;
import com.example.projectsdjqm.databinding.FragmentShoppingListBinding;
import com.example.projectsdjqm.ui.meal_plan.MealPlanViewModel;

public class ShoppingListFragment extends Fragment {

    private FragmentShoppingListBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MealPlanViewModel mealPlanViewModel =
                new ViewModelProvider(this).get(MealPlanViewModel.class);

        binding = FragmentShoppingListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textShoppingList;
        mealPlanViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}