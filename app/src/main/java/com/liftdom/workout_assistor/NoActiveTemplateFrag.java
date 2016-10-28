package com.liftdom.workout_assistor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.template_housing.TemplateHousingActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoActiveTemplateFrag extends Fragment {


    public NoActiveTemplateFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.toTemplatesButton) Button toTemplatesButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_no_active_template, container, false);

        ButterKnife.bind(this, view);

        toTemplatesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TemplateHousingActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
