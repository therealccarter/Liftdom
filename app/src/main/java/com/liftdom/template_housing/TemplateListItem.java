package com.liftdom.template_housing;


import android.graphics.Color;
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
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.MainActivitySingleton;
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

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String templateName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_template_list_item, container, false);

        ButterKnife.bind(this, view);

        if(savedInstanceState != null){
            templateName = savedInstanceState.getString("template_name");
            templateNameView.setText(templateName);
        }



        if(templateNameView != null) {

            if(MainActivitySingleton.getInstance().userModelClass.getActiveTemplate() != null){
                if(MainActivitySingleton.getInstance().userModelClass.getActiveTemplate().equals(templateName)){
                    templateNameView.setTextColor(Color.parseColor("#D1B91D"));
                }
            }
        }

        templateNameView.setText(templateName);

        templateListItemLinear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                SelectedTemplateFrag selectedTemplateFrag = new SelectedTemplateFrag();
                selectedTemplateFrag.templateName = templateName;

                fragmentTransaction.replace(R.id.mainFragHolder, selectedTemplateFrag,
                        "selectedTemplatesTag");

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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
