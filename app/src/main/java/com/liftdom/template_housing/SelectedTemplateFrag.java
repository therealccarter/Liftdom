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

import android.widget.*;
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
    @BindView(R.id.activeTemplateCheckbox) CheckBox setAsActiveTemplate;
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

                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();
                        HousingDoWFrag housingDoWFrag = new HousingDoWFrag();
                        housingDoWFrag.dOWString = daysOfTheWeek;
                        housingDoWFrag.templateName = templateName;
                        fragmentTransaction.add(R.id.templateListedView,
                                housingDoWFrag);
                        fragmentTransaction.commitAllowingStateLoss();
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
                } else if(!isChecked){
                    activeTemplateRef.setValue(null);
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
