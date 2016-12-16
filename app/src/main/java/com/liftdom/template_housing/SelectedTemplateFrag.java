package com.liftdom.template_housing;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateEditorActivity;
import com.liftdom.workout_assistor.*;
import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
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
    @BindView(R.id.setActiveTemplate) Button setAsActiveTemplate;
    @BindView(R.id.templateListedView) LinearLayout templateListedViewHolder;

    int colorIncrement = 0;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selected_template, container, false);

        ButterKnife.bind(this, view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        selectedTemplateNameView.setText(templateName);
        selectedTemplateNameView.setTypeface(lobster);

        if(savedInstanceState != null){
            templateName = savedInstanceState.getString("template_name");
            selectedTemplateNameView.setText(templateName);
            selectedTemplateNameView.setTypeface(lobster);
        }

        final DatabaseReference specificTemplateRef = mRootRef.child("templates").child(uid).child(templateName);

        if(savedInstanceState == null){
            specificTemplateRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        String daysOfTheWeek = dataSnapshot1.getKey();

                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();
                        HousingDoWFrag housingDoWFrag = new HousingDoWFrag();
                        housingDoWFrag.dOWString = daysOfTheWeek;
                        fragmentTransaction.add(R.id.templateListedView,
                                housingDoWFrag);
                        fragmentTransaction.commitAllowingStateLoss();

                        DatabaseReference specificDaysRef = specificTemplateRef.child(daysOfTheWeek);

                        specificDaysRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                    String value = dataSnapshot2.getValue(String.class);
                                    if(isExerciseName(value)){

                                        FragmentManager fragmentManager = getChildFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager
                                                .beginTransaction();
                                        HousingExNameFrag housingExNameFrag = new HousingExNameFrag();
                                        housingExNameFrag.exNameString = value;
                                        fragmentTransaction.add(R.id.templateListedView, housingExNameFrag);

                                        fragmentTransaction.commitAllowingStateLoss();

                                    }else{

                                        FragmentManager fragmentManager = getChildFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager
                                                .beginTransaction();
                                        HousingSetSchemeFrag housingSetSchemeFrag = new HousingSetSchemeFrag();
                                        housingSetSchemeFrag.setSchemeString = value;
                                        fragmentTransaction.add(R.id.templateListedView, housingSetSchemeFrag);
                                        fragmentTransaction.commitAllowingStateLoss();

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
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

                DatabaseReference selectedTemplateRef = mRootRef.child("templates").child(uid).child(templateName);
                DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child("active_template");

                selectedTemplateRef.setValue(null);
                activeTemplateRef.setValue(null);

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.templateMenuFragContainer, new MyTemplatesFrag(), "myTemplatesTag");
                fragmentTransaction.commit();
            }
        });

        final DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child("active_template");

        activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String activeTemplate = dataSnapshot.getValue(String.class);
                if(activeTemplate != null && templateName != null) {
                    if (templateName.equals(activeTemplate)) {
                        setAsActiveTemplate.setTextColor(Color.parseColor("#000000"));
                        setAsActiveTemplate.setBackgroundColor(Color.parseColor("#D1B91D"));
                        setAsActiveTemplate.setText("Unselect As Active Template");
                        colorIncrement = 1;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        setAsActiveTemplate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){

                if (colorIncrement == 0) {

                    activeTemplateRef.setValue(templateName);

                    setAsActiveTemplate.setTextColor(Color.parseColor("#000000"));
                    setAsActiveTemplate.setBackgroundColor(Color.parseColor("#D1B91D"));
                    setAsActiveTemplate.setText("Unselect As Active Template");
                    colorIncrement = 1;

                } else if(colorIncrement == 1){

                    activeTemplateRef.setValue(null);

                    setAsActiveTemplate.setTextColor(Color.parseColor("#000000"));
                    setAsActiveTemplate.setBackgroundColor(Color.parseColor("#388e3c"));
                    setAsActiveTemplate.setText("Select As Active Template");
                    colorIncrement = 0;
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


    boolean isExerciseName(String input){
        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;

    }

}
