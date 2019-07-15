package com.liftdom.workout_assistor;


import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.support.v7.widget.CardView;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestDayFrag extends Fragment {

    String refKey;
    boolean isReviseWorkout;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public RestDayFrag() {
        // Required empty public constructor
    }


    @BindView(R.id.restDayComplete) Button restDayCompleteButton;
    @BindView(R.id.privateJournal) EditText privateJournal;
    @BindView(R.id.publicComment) EditText publicComment;
    //@BindView(R.id.appodealBannerView) BannerView appodealBannerView;
    @BindView(R.id.workoutInsteadButton) Button workoutOnRestDayButton;
    @BindView(R.id.workoutInsteadHolder) CardView workoutOnRestDayHolder;
    @BindView(R.id.loadingHolder) LinearLayout loadingHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rest_day, container, false);

        ButterKnife.bind(this, view);

        HideKey.initialize(getActivity(), view);

        //Appodeal.setBannerViewId(view.findViewById(R.id.appodealBannerView).getId());
        //Appodeal.show(getActivity(), Appodeal.BANNER_BOTTOM);
        //String appKey = "e05b98bf43240a8687216b4e3106a598ced75a344b6c75f2";
        //Appodeal.initialize(getActivity(), appKey, Appodeal.BANNER);
        //Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);

        workoutOnRestDayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                workoutOnRestDayHolder.setVisibility(View.GONE);
                loadingHolder.setVisibility(View.VISIBLE);

                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user")
                        .child(uid).child("isImperial");

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        boolean isTemplateImperial = dataSnapshot.getValue(Boolean.class);

                        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                        final LocalDate localDate = LocalDate.now();
                        String dateTimeString = fmt.print(localDate);

                        HashMap<String, HashMap<String, List<String>>> runningMap = new HashMap<>();

                        HashMap<String, List<String>> subMap = new HashMap<>();

                        List<String> subList = new ArrayList<>();

                        subList.add("CLICK TO CHOOSE EXERCISE");
                        subList.add("1@1_unchecked");

                        subMap.put("0_key", subList);

                        runningMap.put("1_key", subMap);

                        WorkoutProgressModelClass progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                                false, runningMap, "", "", "", isTemplateImperial,
                                refKey, true, true);

                        DatabaseReference runningRef = FirebaseDatabase.getInstance().getReference().child
                                ("runningAssistor").child(uid).child("assistorModel");

                        runningRef.setValue(progressModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.putExtra("fragID",  2);
                                startActivity(intent);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        restDayCompleteButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // in the future we might add a private journal/media ref here
                String day = LocalDate.now().toString("dd");

                double dayDouble = Double.parseDouble(day);

                if(dayDouble % (double) 3 == 0.01){
                    Appodeal.show(getActivity(), Appodeal.INTERSTITIAL);
                }
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RestDaySavedFrag restDaySavedFrag = new RestDaySavedFrag();
                restDaySavedFrag.privateJournal = privateJournal.getText().toString();
                restDaySavedFrag.publicDescription = publicComment.getText().toString();
                if(isReviseWorkout){
                    restDaySavedFrag.redoRefKey = refKey;
                    restDaySavedFrag.isRevisedWorkout = true;
                }
                LinearLayout exInfoHolder = (LinearLayout) getActivity().findViewById(R.id.exInfoHolder);
                fragmentTransaction.replace(exInfoHolder.getId(), restDaySavedFrag);
                fragmentTransaction.commit();
            }
        });

        return view;

    }


}
