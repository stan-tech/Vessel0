package com.vessel.vesselapp.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.vessel.vesselapp.R;

public class DeleteDialog extends AppCompatDialogFragment {
    Delete_dialog_listener delete_dialog_listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        EditText name_et,phone_et,salary_et;

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.delete_prompt,null);


        dialog.setView(view).setTitle(R.string.Ajouter).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {



            }
        }).setPositiveButton(R.string.Ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                delete_dialog_listener.delete_row("phone");

            }
        });
        return  dialog.create();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);

        try {
            delete_dialog_listener = (DeleteDialog.Delete_dialog_listener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString());
        }

    }
    public interface Delete_dialog_listener{

        public void delete_row(String Column);
    }


}
