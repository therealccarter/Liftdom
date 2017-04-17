package com.liftdom.template_housing;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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
    String otherTitle = "error";
    String otherSub = "error";

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

        if(!dOWString.equals("error") && !templateName.equals("error")){

            doWStringView.setText(titleFormatter(dOWString));

            DatabaseReference specificDaysRef = specificTemplateRef.child(dOWString);

            specificDaysRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                        String value = dataSnapshot2.getValue(String.class);
                        if(isExerciseName(value)){

                            try {
                                FragmentManager fragmentManager = getChildFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager
                                        .beginTransaction();
                                HousingExNameFrag housingExNameFrag = new HousingExNameFrag();
                                housingExNameFrag.exNameString = value;
                                fragmentTransaction.add(R.id.exAndSetLLHolder, housingExNameFrag);
                                fragmentTransaction.commitAllowingStateLoss();
                            } catch (IllegalStateException e){
                                Log.i("info", "illegal state");
                            }
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
        }else{
            doWStringView.setVisibility(View.GONE);

            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            HousingExNameFrag housingExNameFrag = new HousingExNameFrag();
            housingExNameFrag.exNameString = otherTitle;
            fragmentTransaction.add(R.id.exAndSetLLHolder, housingExNameFrag);

            HousingSetSchemeFrag housingSetSchemeFrag = new HousingSetSchemeFrag();
            housingSetSchemeFrag.setSchemeString = otherSub;
            if(!otherTitle.equals("1rm")){
                housingSetSchemeFrag.differentType = true;
            }
            fragmentTransaction.add(R.id.exAndSetLLHolder, housingSetSchemeFrag);
            fragmentTransaction.commitAllowingStateLoss();
        }


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

    String titleFormatter(String unformatted){
        String formatted = unformatted.replaceAll("_", "/");

        return formatted;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("doW_name", dOWString);

        super.onSaveInstanceState(savedInstanceState);
    }

}
