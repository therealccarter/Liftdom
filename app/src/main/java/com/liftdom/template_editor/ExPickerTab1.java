package com.liftdom.template_editor;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.refactor.lib.colordialog.ColorDialog;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.liftdom.liftdom.R;

import static android.R.id.message;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExPickerTab1 extends Fragment {


    public ExPickerTab1() {
        // Required empty public constructor
    }

    //@BindView(R.id.benchPressView) TextView benchPressTextView;
    //@BindView(R.id.curlsView) TextView curlsTextView;
    //@BindView(R.id.rowsView) TextView rowsTextView;
    @BindView(R.id.exerciseListView) ListView exerciseListView;

    int exID;
    int clickCounter = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_tab1, container, false);

        ButterKnife.bind(this, view);

        // TODO: Add sticky headers

        String[] listViewValues = new String[]{
          "Barbell Bench Press", "Dumbell Bench Press", "Barbell Incline Press", "Dumbell Incline Press",
                "Dumbell Flys", "Lat Pulldown", "Barbell Row", "Dumbell Row", "T-bar Rows", "Pullups",
                "Barbell Overhead Press", "Dumbell Overhead Press", "Lateral Raise", "Tricep Extensions",
                "French Press", "Barbell Preacher Curls", "Dumbell Preacher Curls", "Dumbell Curls",
                "Barbell Curls", "Concentration Curls", "Crunches", "Sit-ups", "Leg Raises"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout
                .simple_expandable_list_item_1, android.R.id.text1, listViewValues);

        exerciseListView.setAdapter(adapter);

        exerciseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                int itemPosition = position;
                final String itemValue = (String) exerciseListView.getItemAtPosition(itemPosition);

                ColorDialog dialog = new ColorDialog(getContext());
                dialog.setTitle(itemValue);
                dialog.setContentText("Choose " + itemValue + "?");
                dialog.setColor(Color.parseColor("#D1B91D"));
                dialog.setContentImage(getResources().getDrawable(R.drawable.usertest));


                dialog.setPositiveListener("Yes", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        ExercisePickerController.getInstance().exName = itemValue;
                        String message = ExercisePickerController.getInstance().exName;
                        Intent intent = new Intent();
                        intent.putExtra("MESSAGE", message);
                        getActivity().setResult(2, intent);
                        ExercisePickerController.getInstance().exName = null;
                        getActivity().finish();
                    }
                })
                    .setNegativeListener("No", new ColorDialog.OnNegativeListener() {
                        @Override
                        public void onClick(ColorDialog dialog) {
                            ExercisePickerController.getInstance().exName = null;
                            dialog.dismiss();
                        }
                    }).show();

            }
        });

        return view;
    }

}
