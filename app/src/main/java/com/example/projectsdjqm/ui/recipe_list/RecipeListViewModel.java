package com.example.projectsdjqm.ui.recipe_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecipeListViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RecipeListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is recipe list fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}