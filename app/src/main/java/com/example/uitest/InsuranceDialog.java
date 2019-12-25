package com.example.uitest;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class InsuranceDialog extends DialogFragment {
    public interface onInsuranceDialog {
        void onPositiveButtonClicked(String[] list, ArrayList<String> selectedItemList);
        void onNegativeButtonClicked();
    }
    onInsuranceDialog mlistener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mlistener= (onInsuranceDialog) context;
        } catch (Exception e) {
            throw new ClassCastException(getActivity().toString()+"OnMultiChoiceListener must be implemented");
        }

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        final ArrayList<String> selectedItemList=new ArrayList<>();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        final String[] list = getActivity().getResources().getStringArray(R.array.insurance_company);

        builder.setTitle("Select Insurance Coverage")
                .setMultiChoiceItems(list, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(isChecked){
                            selectedItemList.add(list[which]);
                        }else {
                            selectedItemList.remove(list[which]);
                        }
                    }
                }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mlistener.onPositiveButtonClicked(list,selectedItemList);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mlistener.onNegativeButtonClicked();
            }
        });


        return builder.create();
    }
}
