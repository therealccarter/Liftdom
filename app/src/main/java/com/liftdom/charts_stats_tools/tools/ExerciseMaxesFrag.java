package com.liftdom.charts_stats_tools.tools;


import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;
import com.liftdom.workout_assistor.ExerciseMaxesModelClass;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseMaxesFrag extends Fragment {


    public ExerciseMaxesFrag() {
        // Required empty public constructor
    }

    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter firebaseAdapter;

    @BindView(R.id.titleView) TextView titleView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.noMaxesFound) TextView noMaxesFound;
    @BindView(R.id.dateRB) RadioButton dateRB;
    @BindView(R.id.weightRB) RadioButton weightRB;
    @BindView(R.id.alphabeticalRB) RadioButton alphabeticalRB;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise_maxes, container, false);

        ButterKnife.bind(this, view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        titleView.setTypeface(lobster);
        dateRB.setChecked(true);

        dateRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setUpDataReferences();
                }
            }
        });

        weightRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setUpDataReferences();
                }
            }
        });

        alphabeticalRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setUpDataReferences();
                }
            }
        });

        setUpDataReferences();

        return view;
    }

    private void setUpDataReferences(){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                    final boolean imperialPOV = userModelClass.isIsImperial();

                    final DatabaseReference maxesRef = FirebaseDatabase.getInstance().getReference().child("maxes")
                            .child(uid);
                    maxesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                setUpFirebaseAdapter(maxesRef, imperialPOV);
                            }else{
                                loadingView.setVisibility(View.GONE);
                                noMaxesFound.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpFirebaseAdapter(DatabaseReference databaseReference, final boolean isImperial){

        if(weightRB.isChecked()){
            linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setSmoothScrollbarEnabled(true);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            loadingView.setVisibility(View.GONE);

            recyclerView.setHasFixedSize(true);

            ArrayList<ExerciseMaxesModelClass> modelClassArrayList = new ArrayList<>();

            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int inc = 0;
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        modelClassArrayList.add(dataSnapshot1.getValue(ExerciseMaxesModelClass.class));

                        inc++;

                        if(inc == dataSnapshot.getChildrenCount()){

                            Collections.sort(modelClassArrayList, new Comparator<ExerciseMaxesModelClass>() {
                                @Override
                                public int compare(ExerciseMaxesModelClass o1, ExerciseMaxesModelClass o2) {
                                    return Double.compare(Double.parseDouble(o1.getMaxValue()),
                                            Double.parseDouble(o2.getMaxValue()));
                                }
                            });

                            MaxesAdapter adapter = new MaxesAdapter(modelClassArrayList, isImperial);

                            adapter.setFragmentActivity(getActivity());

                            recyclerView.setAdapter(adapter);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else{
            linearLayoutManager = new LinearLayoutManager(getActivity());
            linearLayoutManager.setSmoothScrollbarEnabled(true);
            linearLayoutManager.setReverseLayout(true);
            linearLayoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(linearLayoutManager);

            Query query = databaseReference.orderByChild("date");

            if(dateRB.isChecked()){
                query = databaseReference.orderByChild("date");
            }else if(alphabeticalRB.isChecked()){
                query = databaseReference.orderByChild("exerciseName");
                linearLayoutManager.setReverseLayout(false);
                linearLayoutManager.setStackFromEnd(false);
            }

            FirebaseRecyclerOptions<ExerciseMaxesModelClass> options = new FirebaseRecyclerOptions
                    .Builder<ExerciseMaxesModelClass>()
                    .setQuery(query, ExerciseMaxesModelClass.class)
                    .build();

            firebaseAdapter = new FirebaseRecyclerAdapter<ExerciseMaxesModelClass, ExerciseMaxesViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull ExerciseMaxesViewHolder holder, int position,
                                                @NonNull ExerciseMaxesModelClass model) {

                    holder.setDate(model.getDate());
                    holder.setExerciseName(model.getExerciseName());
                    holder.setIsImperial(model.isIsImperial());
                    holder.setIsImperialPOV(isImperial);
                    holder.setMaxValue(model.getMaxValue());

                }

                @Override
                public ExerciseMaxesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                    View view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.exercise_maxes_list_item, parent, false);

                    return new ExerciseMaxesViewHolder(view);
                }
            };

            loadingView.setVisibility(View.GONE);
            firebaseAdapter.startListening();
            recyclerView.setAdapter(firebaseAdapter);
        }

    }

    @Override
    public void onStart(){
        super.onStart();
        if(firebaseAdapter != null && firebaseAdapter.getItemCount() == 0){
            firebaseAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(firebaseAdapter != null){
            firebaseAdapter.stopListening();
        }
    }

}
