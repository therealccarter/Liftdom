package com.liftdom.liftdom.utils.exercise_selector;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CheckBox;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FullBodyExListFrag extends Fragment {


    public FullBodyExListFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.stickyList3) StickyListHeadersListView stickyList;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_full_body_ex_list, container, false);

        ButterKnife.bind(this, view);

        ExSelectorStickyAdapter adapter = new ExSelectorStickyAdapter(getContext(), "full");

        stickyList.setAdapter(adapter);


        return view;
    }

    public void setItemsSelected(){
        //ArrayList<String> itemsSelected = new ArrayList<>();

        StickyListHeadersListView stickyList =
                (StickyListHeadersListView) view.findViewById(R.id.stickyList3);

        View v;
        CheckBox checkBox;
        TextView textView;

        for(int i = 0; i < stickyList.getCount(); i++){
            v = stickyList.getChildAt(i);
            checkBox = (CheckBox) v.findViewById(R.id.stickyCheckbox);
            textView = (TextView) v.findViewById(R.id.text);

            if(checkBox.isChecked()){
                ExSelectorSingleton.getInstance().fullBodyItems.add(textView.getText().toString());
            }
        }
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        //setItemsSelected();
    }

}
