package com.liftdom.user_profile.calendar_stuff;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.SimpleExNameFrag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FutureDateDialogSubFrag extends Fragment {


    public FutureDateDialogSubFrag() {
        // Required empty public constructor
    }

    public HashMap<String, List<String>> map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_future_date_dialog_sub, container, false);

        String delims = "[_]";

        for(int i = 0; i < map.size(); i++){
            for(Map.Entry<String, List<String>> mapEntry : map.entrySet()){
                if(!mapEntry.getKey().equals("0_key")){
                    String[] keyTokens = mapEntry.getKey().split(delims);
                    if(keyTokens[0].equals(String.valueOf(i + 1))){
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        SimpleExNameFrag exNameFrag = new SimpleExNameFrag();
                        exNameFrag.exInfoList = mapEntry.getValue();
                        fragmentTransaction.add(R.id.exInfoSubHolder, exNameFrag);
                        fragmentTransaction.commit();
                    }
                }
            }
        }


        return view;
    }

}