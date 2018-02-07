package com.liftdom.template_housing;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetSchemeSupersetFrag extends Fragment {


    public SetSchemeSupersetFrag() {
        // Required empty public constructor
    }

    public String setSchemeString = "error";
    public boolean isSmallerText;
    public boolean isTemplateImperial;
    public boolean isCurrentUserImperial;


    @BindView(R.id.setSchemeString) TextView setSchemesView;
    @BindView(R.id.pounds) TextView pounds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_scheme_superset, container, false);

        ButterKnife.bind(this, view);

        if(isSmallerText){
            setSchemesView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen
                    .sixteen_sp));
        }

        if(isCurrentUserImperial){
            pounds.setText(" lbs");
        }else{
            pounds.setText(" kgs");
        }

        if(!setSchemeString.equals("error")){
            if(isPercentage(setSchemeString)){
                String delims = "[@]";
                String[] tokens = setSchemeString.split(delims);
                String formattedWeight = formatPercentageWeight(tokens[1]);
                String fullFormattedString = tokens[0] + "@" + formattedWeight;
                setSchemesView.setText(handleUnitConversion(fullFormattedString));
            }else{
                setSchemesView.setText(handleUnitConversion(setSchemeString));
            }
        }

        return view;
    }

    public String formatPercentageWeight(String unFormatted){
        String formatted;

        String delims = "[_]";
        String[] tokens = unFormatted.split(delims);

        if(tokens[2].equals("a")){
            double weight;
            int weight2;

            double percentage = Double.parseDouble(tokens[3])/(double)100;

            weight = Double.parseDouble(tokens[1]) * percentage;

            weight2 = (int) Math.round(weight);

            formatted = String.valueOf(weight2);
        }else{
            formatted = unFormatted;
        }

        return formatted;
    }

    public boolean isPercentage(String setScheme){
        boolean percentage = false;

        String delims1 = "[@]";
        String[] tokens1 = setScheme.split(delims1);

        char c = tokens1[1].charAt(0);
        String cString = String.valueOf(c);
        if(cString.equals("p")){
            percentage = true;
        }


        return percentage;
    }

    private String handleUnitConversion(String oldValue){
        String newString;

        String delims = "[@]";
        String[] tokens = oldValue.split(delims);

        if(!tokens[1].equals("B.W.")){
            String newValue;
            if(isTemplateImperial && !isCurrentUserImperial){
                // the template is imperial, but the user is metric
                double valueDouble = Double.parseDouble(tokens[1]);
                int valueInt = (int) Math.round(valueDouble * 0.45359237);
                newValue = String.valueOf(valueInt);
                newString = tokens[0] + "@" + newValue;
            }else if(!isTemplateImperial && isCurrentUserImperial){
                // the template is metric, but the user is imperial
                double valueDouble = Double.parseDouble(tokens[1]);
                int valueInt = (int) Math.round(valueDouble / 0.45359237);
                newValue = String.valueOf(valueInt);
                newString = tokens[0] + "@" + newValue;
            }else{
                newString = oldValue;
            }
        }else{
            newString = oldValue;
        }

        return newString;
    }

}
