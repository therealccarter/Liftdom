package com.liftdom.template_editor;


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

/**
 * A simple {@link Fragment} subclass.
 */
public class ExPickerTab1 extends Fragment {


    public ExPickerTab1() {
        // Required empty public constructor
    }

    @BindView(R.id.benchPressButton) Button benchPresButton;
    @BindView(R.id.curlsButton) Button curlsButton;
    @BindView(R.id.rowsButton) Button rowsButton;

    int exID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        ButterKnife.bind(this, view);

        benchPresButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ExercisePickerController.getInstance().exName = "Bench Press";
            }
        });

        curlsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ExercisePickerController.getInstance().exName = "Curls";
            }
        });

        rowsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ExercisePickerController.getInstance().exName = "Rows";
            }
        });

        return view;
    }

}
