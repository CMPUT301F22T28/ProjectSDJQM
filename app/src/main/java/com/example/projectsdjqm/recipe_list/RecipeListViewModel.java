package com.example.projectsdjqm.recipe_list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecipeListViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RecipeListViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Recipe list fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}