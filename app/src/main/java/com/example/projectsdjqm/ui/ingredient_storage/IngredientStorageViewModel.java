package com.example.projectsdjqm.ui.ingredient_storage;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class IngredientStorageViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public IngredientStorageViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is ingredient storage fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}