package com.liftdom.template_housing;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import com.liftdom.liftdom.R;

import java.io.File;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTemplatesFrag extends Fragment {


    public MyTemplatesFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_templates, container, false);

        /**
        ArrayList<String> templateListWithNum = new ArrayList<>();
        ArrayList<String> templateNamesListSansNum = new ArrayList<>();

        File myDir = getContext().getFilesDir();
        File[] templateFiles = myDir.listFiles();

        int length = templateFiles.length;


        for(int i = 0; i < length; i++){
            templateListWithNum.add(templateFiles[i].getName());
        }

        for(String withNum : templateListWithNum){
            String sansNum = lastCharRemover(withNum);
            if(!templateNamesListSansNum.contains(sansNum) && !sansNum.equals("instant-ru") && !s3Check(sansNum)){
                templateNamesListSansNum.add(sansNum);
            }

        }

        for(String template : templateNamesListSansNum){

            TemplateListItemFrag templateListItem = new TemplateListItemFrag();

            templateListItem.templateName = template;

            FragmentManager fragmentManager1 = getActivity().getSupportFragmentManager();
            android.support.v4.app.FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
            fragmentTransaction1.add(R.id.myTemplatesList, templateListItem);
            fragmentTransaction1.addToBackStack(null);
            fragmentTransaction1.commit();

        }**/

        //TODO: Retrieve user's list of templates


        return view;
    }

}
