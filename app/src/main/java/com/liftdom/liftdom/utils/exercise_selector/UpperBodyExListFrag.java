package com.liftdom.liftdom.utils.exercise_selector;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpperBodyExListFrag extends Fragment{


    public UpperBodyExListFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.stickyList1) StickyListHeadersListView stickyList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upper_body_ex_list, container, false);

        ButterKnife.bind(this, view);

        ExSelectorStickyAdapter adapter = new ExSelectorStickyAdapter(getContext(), "upper");

        stickyList.setAdapter(adapter);

        //for(int i = 0; i < adapter.getCount(); i++){
//
        //    ArrayList<View> viewArrayList = new ArrayList<>();
//
        //    View view1 = adapter.getView(i, null, stickyList);


            //CheckBox checkBox;
            //long id = stickyList.getItemIdAtPosition(i);
//
            //View v = (View) view.findViewById((int)id);
//
            //checkBox = (CheckBox) v.findViewById(R.id.stickyCheckbox);
            //final TextView textView = (TextView) v.findViewById(R.id.text);
//
            //checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            //    @Override
            //    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            //        ExSelectorSingleton.getInstance().upperBodyItems.add(textView.getText().toString());
            //    }
            //});

            //if(checkBox.isChecked()){
            //    ExSelectorSingleton.getInstance().upperBodyItems.add(textView.getText().toString());
            //}
        //}

        return view;
    }


    //public void setItemsSelected(){
    //    //ArrayList<String> itemsSelected = new ArrayList<>();
//
    //    StickyListHeadersListView stickyList =
    //            (StickyListHeadersListView) getView().findViewById(R.id.stickyList3);
//
    //    View v;
    //    CheckBox checkBox;
    //    TextView textView;
//
    //    for(int i = 0; i < stickyList.getCount(); i++){
    //        v = stickyList.getChildAt(i);
    //        checkBox = (CheckBox) v.findViewById(R.id.stickyCheckbox);
    //        textView = (TextView) v.findViewById(R.id.text);
//
    //        if(checkBox.isChecked()){
    //            ExSelectorSingleton.getInstance().upperBodyItems.add(textView.getText().toString());
    //        }
    //    }
    //}
//
    //@Override
    //public void onDestroyView(){
    //    super.onDestroyView();
    //    setItemsSelected();
    //}

}
