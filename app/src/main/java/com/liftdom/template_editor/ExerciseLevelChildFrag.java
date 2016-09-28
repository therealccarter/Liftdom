package com.liftdom.template_editor;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExerciseLevelChildFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ExerciseLevelChildFrag extends android.app.Fragment implements SetsLevelChildFrag.setSchemesCallback{

    int fragIdCount2 = 0;

    private OnFragmentInteractionListener mListener;

    // Butterknife
    @BindView(R.id.movementName) Spinner exerciseSpinner;
    @BindView(R.id.addSet) Button addSet;
    @BindView(R.id.removeSet) Button removeSet;


    public ExerciseLevelChildFrag() {
        // Required empty public constructor
    }

    // Callback
    public interface doWCallback{
        String getDoW();
    }
    private doWCallback callback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise_level_child, container, false);

        ButterKnife.bind(this, view);

        // Exercise Spinner
        Spinner exerciseSpinner = (Spinner) view.findViewById(R.id.movementName);

        ArrayAdapter<CharSequence> exerciseAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.exercises1,
                android.R.layout.simple_spinner_dropdown_item);

        exerciseAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        exerciseSpinner.setAdapter(exerciseAdapter);

        addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fragIdCount2;
                FragmentManager fragmentManager = getFragmentManager();
                String fragString2 = Integer.toString(fragIdCount2);
                SetsLevelChildFrag frag1 = new SetsLevelChildFrag();
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.LinearLayoutChild1, frag1, fragString2);
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();
            }
        });

        removeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                FragmentManager fragmentManager = getFragmentManager();
                String fragString2 = Integer.toString(fragIdCount2);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                //Fragment f = getFragmentManager().findFragmentByTag(fragString);
                if(fragIdCount2 != 0){
                    fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(fragString2)).commit();
                    --fragIdCount2;
                }
            }
        });

        callback = (doWCallback) getParentFragment();

        return view;
    }

    public String getDoWValue(){
        String doWSelected = callback.getDoW();
        return doWSelected;
    }

    public String getSpinnerValue(){

        String spinnerText = exerciseSpinner.getSelectedItem().toString();

        return spinnerText;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
