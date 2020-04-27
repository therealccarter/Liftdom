package com.liftdom.template_housing;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.charts_stats_tools.DigitsInputFilter;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.InputFilterMinMax;
import com.liftdom.template_editor.TemplateEditorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewTemplateMenuFrag extends Fragment {


    public NewTemplateMenuFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.premadeTemplates) Button premadeTemplates;
    @BindView(R.id.fromScratch) Button fromScratch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_new_template_menu, container, false);

        ButterKnife.bind(this, view);

        fromScratch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                String isEdit = "no";
                Intent intent = new Intent(v.getContext(), TemplateEditorActivity.class);
                intent.putExtra("isEdit", isEdit );
                startActivity(intent);
            }
        });

        premadeTemplates.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.mainFragHolder, new PremadeTemplatesFrag(),
                        "premadeTemplatesTag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    private void setToImperial(){
        //editTest.setText("");
        //editTest.setInputType(InputType.TYPE_CLASS_NUMBER);
        //units.setText(" lbs");
        //editTest.setFilters(new InputFilter[]{new InputFilterMinMax(1, 500)});
    }

    private void setToMetric(){
        //editTest.setText("");
        //editTest.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
        //units.setText(" kgs");
        //editTest.setFilters(new InputFilter[]{new DigitsInputFilter(4, 2, 500)});
    }

}
