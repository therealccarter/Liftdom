package com.liftdom.template_housing;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.database.DatabaseReference;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateEditorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PremadeTemplatesFrag extends Fragment {

    @BindView(R.id.smolovButton) Button smolovButton;


    public PremadeTemplatesFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_premade_templates, container, false);

        ButterKnife.bind(this, view);

        smolovButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // set title
                builder.setTitle("Select Smolov?");

                // set dialog message
                builder
                        .setMessage("Would you like to begin Smolov?")
                        .setCancelable(false)
                        .setPositiveButton("Select Smolov",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                //DatabaseReference setSmolov =

                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = builder.create();

                // show it
                alertDialog.show();
            }
        });

        return view;
    }

}
