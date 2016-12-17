package com.liftdom.template_housing;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class HousingDoWFrag extends Fragment {


    public HousingDoWFrag() {
        // Required empty public constructor
    }

    String dOWString = "error";
    String templateName = "error";


    @BindView(R.id.doWName) TextView doWStringView;
    @BindView(R.id.exAndSetLLHolder) LinearLayout exAndSetHolder;




    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_housing_do_w, container, false);

        ButterKnife.bind(this, view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        if(savedInstanceState != null){
            dOWString = savedInstanceState.getString("doW_name");
            doWStringView.setText(dOWString);
        }

        doWStringView.setTypeface(lobster);

        final DatabaseReference specificTemplateRef = mRootRef.child("templates").child(uid).child(templateName);

        doWStringView.setText(dOWString);

        DatabaseReference specificDaysRef = specificTemplateRef.child(dOWString);

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
                        fragmentTransaction.add(R.id.exAndSetLLHolder, housingExNameFrag);

                        fragmentTransaction.commitAllowingStateLoss();

                    }else{

                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();
                        HousingSetSchemeFrag housingSetSchemeFrag = new HousingSetSchemeFrag();
                        housingSetSchemeFrag.setSchemeString = value;
                        fragmentTransaction.add(R.id.exAndSetLLHolder, housingSetSchemeFrag);
                        fragmentTransaction.commitAllowingStateLoss();

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("doW_name", dOWString);

        super.onSaveInstanceState(savedInstanceState);
    }

}
