package com.example.projectsdjqm.meal_plan;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MealPlanViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MealPlanViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is meal plan fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}