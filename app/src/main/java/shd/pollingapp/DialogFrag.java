package shd.pollingapp;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class DialogFrag extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.add_poll_dialog, null);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(view)
                // Add action buttons
                .setPositiveButton("Create", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        Intent intent = new Intent(getActivity(), AddQuestions.class);
                        EditText name= (EditText) view.findViewById(R.id.newpollname);
                        String qname = name.getText().toString();
                        System.out.println("Dia :"+qname);

                        if(getActivity().getClass().toString().equals("class shd.pollingapp.HrPoll")) {
                            intent.putExtra("name", qname);
                            System.out.println(qname+"   "+getActivity().getClass().toString());

                        }
                        else if(getActivity().getClass().toString().equals("class shd.pollingapp.AddQuestions"))
                        {
                            intent.putExtra("question", qname);
                            System.out.println(qname+"   "+getActivity().getClass().toString());
                        }
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
        return builder.create();
    }
}