package com.liftdom.template_editor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetsLevelChildFrag extends android.app.Fragment {

    // Butterknife
    @BindView(R.id.sets) EditText setsEditText;
    @BindView(R.id.reps) EditText repsEditText;
    @BindView(R.id.weight) EditText weightEditText;

    public SetsLevelChildFrag() {
        // Required empty public constructor
    }


    // Callback
    public interface setSchemesCallback{
        String getSpinnerValue();
        String getDoWValue();
    }

    private setSchemesCallback callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sets_level_child, container, false);

        ButterKnife.bind(this, view);

        callback = (setSchemesCallback) getParentFragment();

        return view;
    }

    @Override
    public void onPause(){
        super.onPause();


        String parentSpinnerValue = callback.getSpinnerValue();
        String doWSelected = callback.getDoWValue();
        EditTemplateAssemblerClass.getInstance().setDoW(doWSelected);
        EditTemplateAssemblerClass.getInstance().setExSpinnerValue(parentSpinnerValue, doWSelected);

        String setsString = setsEditText.getText().toString();
        String repsString = repsEditText.getText().toString();
        String weightString = weightEditText.getText().toString();

        String value = null;

        if(!setsString.equals("") && !repsString.equals("") && !weightString.equals("")) {
            value = setsString + " x " + repsString + " @ " +
                    weightString;
        }

        if(value != null) {
            EditTemplateAssemblerClass.getInstance().setSetSchemeValue(value, parentSpinnerValue, doWSelected);
        }
    }


}
