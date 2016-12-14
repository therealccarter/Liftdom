package com.liftdom.template_housing;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateEditorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class TemplateMenuFrag extends Fragment {

    int templateOptionsCheck = 0;



    public TemplateMenuFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.createTemplateLinearLayout) LinearLayout templateOptions;
    @BindView(R.id.button_new_template) LinearLayout newTemplateButton;
    @BindView(R.id.button_from_scratch) Button fromScratch;
    @BindView(R.id.button_my_templates) LinearLayout myTemplatesButton;
    @BindView(R.id.button_premade_templates) Button premadeTemplatesButton;
    @BindView(R.id.text_my_templates) TextView textMyTemplates;
    @BindView(R.id.text_new_template) TextView textNewTemplate;
    @BindView(R.id.new_template_image) ImageView newTemplateImage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_template_menu, container, false);

        ButterKnife.bind(this, view);

        templateOptions.setVisibility(View.GONE);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        textMyTemplates.setTypeface(lobster);
        textNewTemplate.setTypeface(lobster);

        myTemplatesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.templateMenuFragContainer, new MyTemplatesFrag(), "myTemplatesTag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });


        newTemplateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){

                float scale = getResources().getDisplayMetrics().density;
                int dp = (int) (7*scale + 0.5f);

                if (templateOptionsCheck == 0) {
                    templateOptions.setVisibility(View.VISIBLE);

                    newTemplateImage.setImageResource(android.R.color.transparent);
                    newTemplateImage.setBackgroundResource(R.drawable.simple_up_arrow_small);
                    newTemplateImage.setPadding(dp,0,dp,0);
                    newTemplateButton.setBackgroundColor(Color.parseColor("#FF131313"));
                    textMyTemplates.setTextColor(Color.parseColor("#D1B91D"));

                    templateOptionsCheck = 1;
                }else if (templateOptionsCheck == 1) {
                    templateOptions.setVisibility(View.GONE);

                    newTemplateImage.setImageResource(android.R.color.transparent);
                    newTemplateImage.setBackgroundResource(R.drawable.simple_down_arrow_small);
                    newTemplateImage.setPadding(dp,0,dp,0);
                    newTemplateButton.setBackgroundColor(Color.parseColor("#C9000000"));
                    textMyTemplates.setTextColor(Color.parseColor("#D1B91D"));

                    templateOptionsCheck = 0;
                }
            }
        });

        fromScratch.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                String isEdit = "no";
                Intent intent = new Intent(v.getContext(), TemplateEditorActivity.class);
                intent.putExtra("key1", isEdit );
                startActivity(intent);
            }
        });

        premadeTemplatesButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.templateMenuFragContainer, new PremadeTemplatesFrag(),
                        "premadeTemplatesTag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

}
