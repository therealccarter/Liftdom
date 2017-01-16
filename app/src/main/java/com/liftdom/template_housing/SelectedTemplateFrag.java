package com.liftdom.template_housing;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateEditorActivity;
import com.liftdom.workout_assistor.*;
import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedTemplateFrag extends Fragment {


    public SelectedTemplateFrag() {
        // Required empty public constructor
    }

    String templateName;

    @BindView(R.id.selectedTemplateTitle) TextView selectedTemplateNameView;
    @BindView(R.id.editThisTemplate) Button editTemplate;
    @BindView(R.id.deleteThisTemplate) Button deleteTemplate;
    @BindView(R.id.activeTemplateCheckbox) CheckBox setAsActiveTemplate;
    @BindView(R.id.templateListedView) LinearLayout templateListedViewHolder;

    int colorIncrement = 0;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    ArrayList<String> daysArrayList = new ArrayList<>();

    String[] daysArray = new String[7];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selected_template, container, false);

        ButterKnife.bind(this, view);


        for(int i = 0; i < 7; i++){
            daysArray[i] = "false";
        }

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");


        selectedTemplateNameView.setText(templateName);
        selectedTemplateNameView.setTypeface(lobster);

        if(savedInstanceState != null){
            templateName = savedInstanceState.getString("template_name");
            selectedTemplateNameView.setText(templateName);
            selectedTemplateNameView.setTypeface(lobster);
        }

        if(savedInstanceState == null){
            final DatabaseReference specificTemplateRef = mRootRef.child("templates").child(uid).child(templateName);
            specificTemplateRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        String daysOfTheWeek = dataSnapshot1.getKey();

                        if(!daysOfTheWeek.equals("algorithm") || !daysOfTheWeek.equals("algorithmExercises")){

                            String delims = "[_]";

                            String[] dayArray = daysOfTheWeek.split(delims);

                            if(dayArray[0].equals("Monday")){
                                daysArray[0] = daysOfTheWeek;
                            } else if(dayArray[0].equals("Tuesday")){
                                daysArray[1] = daysOfTheWeek;
                            }else if(dayArray[0].equals("Wednesday")){
                                daysArray[2] = daysOfTheWeek;
                            }else if(dayArray[0].equals("Thursday")){
                                daysArray[3] = daysOfTheWeek;
                            }else if(dayArray[0].equals("Friday")){
                                daysArray[4] = daysOfTheWeek;
                            }else if(dayArray[0].equals("Saturday")){
                                daysArray[5] = daysOfTheWeek;
                            }else if(dayArray[0].equals("Sunday")){
                                daysArray[6] = daysOfTheWeek;
                            }

                        }
                    }

                    for(int i = 0; i < 7; i++){
                        if(!daysArray[i].equals("false")){
                            FragmentManager fragmentManager = getChildFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager
                                    .beginTransaction();
                            HousingDoWFrag housingDoWFrag = new HousingDoWFrag();
                            housingDoWFrag.dOWString = daysArray[i];
                            housingDoWFrag.templateName = templateName;
                            fragmentTransaction.add(R.id.templateListedView,
                                    housingDoWFrag);
                            if(!getActivity().isFinishing()){
                                fragmentTransaction.commitAllowingStateLoss();
                            }
                        }
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }


        editTemplate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                String isEdit = "yes";
                Intent intent = new Intent(v.getContext(), TemplateEditorActivity.class);
                intent.putExtra("isEdit", isEdit );
                intent.putExtra("templateName", templateName);

                startActivity(intent);
            }
        });

        deleteTemplate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());

                // set title
                alertDialogBuilder.setTitle("Delete template:");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Are you sure you want to delete this template?")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                DatabaseReference selectedTemplateRef = mRootRef.child("templates").child(uid).child(templateName);
                                DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child("active_template");

                                selectedTemplateRef.setValue(null);
                                activeTemplateRef.setValue(null);

                                //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                //fragmentTransaction.replace(R.id.templateMenuFragContainer, new MyTemplatesFrag(),
                                        //"myTemplatesTag");
                                //fragmentTransaction.commit();

                                Intent intent = new Intent(getContext(), TemplateHousingActivity.class);
                                startActivity(intent);

                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });

        final DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child("active_template");

        activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String activeTemplate = dataSnapshot.getValue(String.class);
                if(activeTemplate != null && templateName != null) {
                    if (templateName.equals(activeTemplate)) {
                        setAsActiveTemplate.setChecked(true);
                    }
                }
            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        setAsActiveTemplate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    activeTemplateRef.setValue(templateName);
                    CharSequence toastText = "(+) Set as Active Template";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getContext(), toastText, duration);
                    toast.show();
                } else if(!isChecked){
                    activeTemplateRef.setValue(null);

                    CharSequence toastText = "(-) Unselected as Active Template";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getContext(), toastText, duration);
                    toast.show();
                }
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("template_name", templateName);

        super.onSaveInstanceState(savedInstanceState);
    }




}
