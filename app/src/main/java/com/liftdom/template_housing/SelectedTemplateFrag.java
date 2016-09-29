package com.liftdom.template_housing;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateEditorActivity;
import org.w3c.dom.Text;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelectedTemplateFrag extends Fragment {


    public SelectedTemplateFrag() {
        // Required empty public constructor
    }

    String templateName;

    @BindView(R.id.selectedTemplateTitle) TextView selectedTemplateNameView;
    @BindView(R.id.editThisTemplate) Button editTemplate;
    @BindView(R.id.deleteThisTemplate) Button deleteTemplate;
    @BindView(R.id.setActiveTemplate) Button setAsActiveTemplate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_selected_template, container, false);

        ButterKnife.bind(this, view);

        selectedTemplateNameView.setText(templateName);

        editTemplate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                String isEdit = "yes";
                Intent intent = new Intent(v.getContext(), TemplateEditorActivity.class);
                intent.putExtra("isEdit", isEdit );
                intent.putExtra("templateName", templateName);

                startActivity(intent);
            }
        });

        deleteTemplate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){

                /**
                File dir = getContext().getFilesDir();
                for(int i = 1; i < 8; i++) {
                    String inc = Integer.toString(i);
                    File file = new File(dir, templateName + inc);
                    boolean deleted = file.delete();
                }
                 **/

                // TODO: Delete selected template from Firebase. Also, look into popping backstack

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.templateMenuFragContainer, new MyTemplatesFrag(), "myTemplatesTag");
                fragmentTransaction.commit();
            }
        });

        setAsActiveTemplate.setOnClickListener(new View.OnClickListener(){
            int colorIncrement = 0;
            @Override
            public void onClick(final View v){

                if (colorIncrement == 0) {
                    SharedPreferences sharedPref = getActivity().getSharedPreferences("sharedPrefs", Context
                            .MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("activeTemplateName", templateName);
                    editor.apply();

                    setAsActiveTemplate.setTextColor(Color.parseColor("#D1B91D"));
                    setAsActiveTemplate.setText("Unselect As Active Template");
                    colorIncrement = 1;
                } else if(colorIncrement == 1){
                    SharedPreferences sharedPref = getActivity().getSharedPreferences("sharedPrefs", Context
                            .MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("activeTemplateName", null);
                    editor.apply();

                    setAsActiveTemplate.setTextColor(Color.parseColor("#ffffff"));
                    setAsActiveTemplate.setText("Select As Active Template");
                    colorIncrement = 0;
                }

            }
        });

        return view;
    }

}
