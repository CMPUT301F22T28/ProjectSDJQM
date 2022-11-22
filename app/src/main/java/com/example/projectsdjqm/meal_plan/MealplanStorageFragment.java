package com.example.projectsdjqm.meal_plan;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.projectsdjqm.R;

public class MealplanStorageFragment extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mealplan_storage_fragment);
    }

//    public interface OnFragmentInteractionListener {
//        void onFragmentInteraction(Uri uri);
//    }
//
//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        if(context instanceof MealplanStorageFragment.OnFragmentInteractionListener) {
//            listener = (MealplanStorageFragment.OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString() + "This is not the correct fragment!");
//        }
//    }
//
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        View view = LayoutInflater.from(getActivity()).inflate(R.layout.mealplan_storage_fragment,null);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//
//        return builder
//                .setView(view)
//                .setTitle("Choosing recipes and ingredients")
//                .setNegativeButton("Cancel", null)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {}
//                }).create();
//    }
}
