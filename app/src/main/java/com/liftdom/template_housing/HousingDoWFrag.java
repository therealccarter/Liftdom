package com.liftdom.template_housing;


import android.content.Intent;
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
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.SetsLevelSSFrag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    HashMap<String, List<String>> map;
    boolean isTemplateImperial;
    boolean isCurrentUserImperial;

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

        doWStringView.setText(titleFormatter(dOWString));
        doWStringView.setTypeface(lobster);

        if(map != null){
            for(Map.Entry<String, List<String>> entry : map.entrySet()){ // map is null
                if(!entry.getKey().equals("0_key")){
                    List<String> list = entry.getValue();
                    boolean isFirstEx = true;
                    boolean isFirstSetSchemes = true;
                    for(String string : list){
                        if(!string.isEmpty()){
                            if(isExerciseName(string) && isFirstEx){
                                // add exname frag
                                isFirstEx = false;
                                FragmentManager fragmentManager = getChildFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager
                                        .beginTransaction();
                                HousingExNameFrag exNameFrag = new HousingExNameFrag();
                                exNameFrag.exNameString = string;
                                fragmentTransaction.add(R.id.exAndSetLLHolder, exNameFrag);
                                fragmentTransaction.commit();
                            }else if(isExerciseName(string) && !isFirstEx) {
                                // add exname frag
                                FragmentManager fragmentManager = getChildFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager
                                        .beginTransaction();
                                ExNameSupersetFrag exNameFrag = new ExNameSupersetFrag();
                                exNameFrag.exNameString = string;
                                fragmentTransaction.add(R.id.exAndSetLLHolder, exNameFrag);
                                fragmentTransaction.commit();
                                isFirstSetSchemes = false;
                            }else if(!isExerciseName(string) && isFirstSetSchemes){
                                FragmentManager fragmentManager = getChildFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager
                                        .beginTransaction();
                                HousingSetSchemeFrag setSchemeFrag = new HousingSetSchemeFrag();
                                setSchemeFrag.isCurrentUserImperial = isCurrentUserImperial;
                                setSchemeFrag.isTemplateImperial = isTemplateImperial;
                                setSchemeFrag.setSchemeString = string;
                                fragmentTransaction.add(R.id.exAndSetLLHolder, setSchemeFrag);
                                fragmentTransaction.commit();
                            }else if(!isExerciseName(string) && !isFirstSetSchemes){
                                FragmentManager fragmentManager = getChildFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager
                                        .beginTransaction();
                                SetSchemeSupersetFrag setSchemeFrag = new SetSchemeSupersetFrag();
                                setSchemeFrag.isCurrentUserImperial = isCurrentUserImperial;
                                setSchemeFrag.isTemplateImperial = isTemplateImperial;
                                setSchemeFrag.setSchemeString = string;
                                fragmentTransaction.add(R.id.exAndSetLLHolder, setSchemeFrag);
                                fragmentTransaction.commit();
                            }
                        }
                    }
                }
            }
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

        if(formatted.length() != 0) {
            int index = formatted.length() - 1;
            char c = formatted.charAt(index);
            if(String.valueOf(c).equals("/")){
                formatted = formatted.substring(0, index);
            }
        }


        return formatted;
    }


}
