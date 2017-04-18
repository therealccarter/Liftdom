package com.liftdom.knowledge_center;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisciplinesDetailFrag extends Fragment {


    public DisciplinesDetailFrag() {
        // Required empty public constructor
    }

    headerChangeToFrag mCallback;

    public interface headerChangeToFrag{
        public void changeHeader(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeader(title);
    }

    public String disciplineString = "null";

    @BindView(R.id.disciplineTitle) TextView disciplineTitle;
    @BindView(R.id.disciplineText) TextView disciplineText;
    @BindView(R.id.disciplineImage) ImageView disciplineImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_disciplines_detail, container, false);

        headerChanger("Disciplines - " + disciplineString);

        ButterKnife.bind(this, view);

        //TODO: Eventually we'll have personalized discipline ranks shown here
        // They could even be based on your rep-ranges/exercises used

        disciplineTitle.setText(disciplineString);


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (headerChangeToFrag) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
