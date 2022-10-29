package com.example.projectsdjqm.ingredient_storage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectsdjqm.databinding.FragmentIngredientStorageBinding;

public class IngredientStorageFragment extends Fragment {

    private FragmentIngredientStorageBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        IngredientStorageViewModel ingredientStorageViewModel =
                new ViewModelProvider(this).get(IngredientStorageViewModel.class);

        binding = FragmentIngredientStorageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textIngredientStorage;
        ingredientStorageViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}