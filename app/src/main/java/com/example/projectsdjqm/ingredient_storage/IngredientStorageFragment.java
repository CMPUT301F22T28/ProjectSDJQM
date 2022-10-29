/**
 * Ingredient Storage Page
 * @version 1.0
 * @author Muchen Li
 */
package com.example.projectsdjqm.ingredient_storage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.projectsdjqm.databinding.FragmentIngredientStorageBinding;

public class IngredientStorageFragment extends Fragment {

    private FragmentIngredientStorageBinding binding;
    private ListView ingredientListView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
       IngredientStorageViewModel ingredientStorageViewModel =
                new ViewModelProvider(this).get(IngredientStorageViewModel.class);

        binding = FragmentIngredientStorageBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //final ListView listView = binding.ingredientList;
        //ingredientStorageViewModel.getClass();

        ingredientListView = binding.ingredientList;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());


        return root;
     }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}