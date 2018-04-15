package com.liftdom.template_housing;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
public class HousingSetSchemeFrag extends Fragment {


    public HousingSetSchemeFrag() {
        // Required empty public constructor
    }

    public String setSchemeString = "error";
    boolean differentType = false;
    public boolean isSmallerText;
    public boolean isTemplateImperial;
    public boolean isCurrentUserImperial;

    @BindView(R.id.setSchemeString) TextView setSchemesView;
    @BindView(R.id.pounds) TextView pounds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_housing_set_scheme, container, false);

        ButterKnife.bind(this, view);

        if(savedInstanceState != null){
            setSchemeString = savedInstanceState.getString("set_scheme_string");
            setSchemesView.setText(handleUnitConversion(setSchemeString));
        }

        if(differentType){
            pounds.setVisibility(View.GONE);
        }

        if(isSmallerText){
            setSchemesView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen
                    .sixteen_sp));
        }

         if(isCurrentUserImperial){
             pounds.setText(" lbs");
         }else{
             pounds.setText(" kgs");
         }

        if(isPercentage(setSchemeString)){
            String delims = "[@]";
            String[] tokens = setSchemeString.split(delims);
            String formattedWeight = formatPercentToString(tokens[1]);
            String fullFormattedString = tokens[0] + "@" + formattedWeight;
            String formatted = addSpacesToSetScheme(handleUnitConversion(fullFormattedString));
            setSchemesView.setText(formatted);
        }else{
            String formatted = addSpacesToSetScheme(handleUnitConversion(setSchemeString));
            setSchemesView.setText(formatted);
        }

        return view;
    }

    public String addSpacesToSetScheme(String unFormatted){
        String formatted;

        String delims = "[x,@]";
        String[] tokens = unFormatted.split(delims);

        formatted = tokens[0] + " x " + tokens[1] + " @ " + tokens[2];

        return formatted;
    }

    public String formatPercentToString(String unFormatted){
        String formatted;

        String delims = "[_]";
        String[] tokens = unFormatted.split(delims);

        if(tokens[2].equals("a")){
            formatted = tokens[1] + " % of " + tokens[3];
        }else{
            formatted = unFormatted;
        }

        return formatted;
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

        try{
            char c = tokens1[1].charAt(0);
            String cString = String.valueOf(c);
            if(cString.equals("p")){
                percentage = true;
            }
        }catch (IndexOutOfBoundsException e){
            Log.i("e", "e");
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("set_scheme_string", setSchemeString);

        super.onSaveInstanceState(savedInstanceState);
    }

}
