package com.liftdom.workout_assistor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.BannerView;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestDayFrag extends Fragment {


    public RestDayFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.restAdviceButton) Button restAdviceButton;
    @BindView(R.id.restDayComplete) Button restDayCompleteButton;
    @BindView(R.id.privateJournal) EditText privateJournal;
    @BindView(R.id.appodealBannerView) BannerView appodealBannerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rest_day, container, false);

        ButterKnife.bind(this, view);

        //Appodeal.setBannerViewId(view.findViewById(R.id.appodealBannerView).getId());
        Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);

        restAdviceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), KnowledgeCenterHolderActivity.class);
                startActivity(intent);
            }
        });

        restDayCompleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // in the future we might add a private journal/media ref here
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RestDaySavedFrag restDaySavedFrag = new RestDaySavedFrag();
                restDaySavedFrag.privateJournal = privateJournal.getText().toString();
                LinearLayout exInfoHolder = (LinearLayout) getActivity().findViewById(R.id.exInfoHolder);
                fragmentTransaction.replace(exInfoHolder.getId(), restDaySavedFrag);
                fragmentTransaction.commit();

            }
        });
        return view;
    }


}
