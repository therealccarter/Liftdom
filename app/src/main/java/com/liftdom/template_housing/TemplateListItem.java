package com.liftdom.template_housing;


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
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TemplateListItem extends Fragment {


    public TemplateListItem() {
        // Required empty public constructor
    }

    @BindView(R.id.templateName) TextView templateNameView;
    @BindView(R.id.templateListItemLinear) LinearLayout templateListItemLinear;

    String templateName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_template_list_item, container, false);

        ButterKnife.bind(this, view);

        templateNameView.setText(templateName);

        templateListItemLinear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                SelectedTemplateFrag selectedTemplateFrag = new SelectedTemplateFrag();
                selectedTemplateFrag.templateName = templateName;

                fragmentTransaction.replace(R.id.templateMenuFragContainer, selectedTemplateFrag,
                        "selectedTemplatesTag");

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
