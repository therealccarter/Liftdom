package com.liftdom.template_housing;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.workout_programs.PremadeProgramModelClass;
import com.liftdom.workout_programs.PremadeProgramViewHolder;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PremadeTemplatesFrag extends Fragment {

    public PremadeTemplatesFrag() {
        // Required empty public constructor
    }

    headerChangeFromFrag mCallback;

    public interface headerChangeFromFrag{
        void changeHeaderTitle(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeaderTitle(title);
    }

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private DatabaseReference mPremadesRef = FirebaseDatabase.getInstance().getReference().child(
            "premadePrograms");

    boolean dontRun = false;

    List<PremadeProgramModelClass> premadeList = new ArrayList<>();

    @BindView(R.id.recycler_view_premade_programs) RecyclerView recyclerView;
    @BindView(R.id.loadingView2) AVLoadingIndicatorView loadingView;
    @BindView(R.id.workoutTypeRB) RadioButton workoutTypeRB;
    @BindView(R.id.experienceLevelRB) RadioButton experienceLevelRB;
    @BindView(R.id.alphabeticalRB) RadioButton alphabeticalRB;
    @BindView(R.id.typeLL) LinearLayout typeLL;
    @BindView(R.id.bodybuildingRB) RadioButton bodybuildingRB;
    @BindView(R.id.strengthRB) RadioButton strengthRB;
    @BindView(R.id.hybridOtherRB) RadioButton hybridOtherRB;
    @BindView(R.id.experienceLL) LinearLayout experienceLL;
    @BindView(R.id.beginnerRB) RadioButton beginnerRB;
    @BindView(R.id.intermediateRB) RadioButton intermediateRB;
    @BindView(R.id.advancedRB) RadioButton advancedRB;
    @BindView(R.id.alphabeticalLL) LinearLayout alphabeticalLL;
    @BindView(R.id.ascendingRB) RadioButton ascendingRB;
    @BindView(R.id.descendingRB) RadioButton descendingRB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_premade_templates, container, false);

        ButterKnife.bind(this, view);

        workoutTypeRB.setChecked(true);
        typeLL.setVisibility(View.VISIBLE);
        bodybuildingRB.setChecked(true);
        beginnerRB.setChecked(true);
        ascendingRB.setChecked(true);

        getPremades();

        workoutTypeRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!dontRun){
                    if(isChecked){
                        experienceLevelRB.setChecked(false);
                        alphabeticalRB.setChecked(false);
                        typeLL.setVisibility(View.VISIBLE);
                        experienceLL.setVisibility(View.GONE);
                        alphabeticalLL.setVisibility(View.GONE);
                        setUpRecyclerView();
                    }
                }
            }
        });

        experienceLevelRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!dontRun){
                    if(isChecked){
                        workoutTypeRB.setChecked(false);
                        alphabeticalRB.setChecked(false);
                        typeLL.setVisibility(View.GONE);
                        experienceLL.setVisibility(View.VISIBLE);
                        alphabeticalLL.setVisibility(View.GONE);
                        setUpRecyclerView();
                    }
                }
            }
        });

        alphabeticalRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!dontRun){
                    if(isChecked){
                        workoutTypeRB.setChecked(false);
                        experienceLevelRB.setChecked(false);
                        typeLL.setVisibility(View.GONE);
                        experienceLL.setVisibility(View.GONE);
                        alphabeticalLL.setVisibility(View.VISIBLE);
                        setUpRecyclerView();
                    }
                }
            }
        });

        bodybuildingRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!dontRun) {
                    if (isChecked) {
                        strengthRB.setChecked(false);
                        hybridOtherRB.setChecked(false);
                        setUpRecyclerView();
                    }
                }
            }
        });

        strengthRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!dontRun) {
                    if (isChecked) {
                        bodybuildingRB.setChecked(false);
                        hybridOtherRB.setChecked(false);
                        setUpRecyclerView();
                    }
                }
            }
        });

        hybridOtherRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!dontRun) {
                    if (isChecked) {
                        bodybuildingRB.setChecked(false);
                        strengthRB.setChecked(false);
                        setUpRecyclerView();
                    }
                }
            }
        });

        beginnerRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!dontRun) {
                    if (isChecked) {
                        intermediateRB.setChecked(false);
                        advancedRB.setChecked(false);
                        setUpRecyclerView();
                    }
                }
            }
        });

        intermediateRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!dontRun) {
                    if (isChecked) {
                        beginnerRB.setChecked(false);
                        advancedRB.setChecked(false);
                        setUpRecyclerView();
                    }
                }
            }
        });

        advancedRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!dontRun) {
                    if (isChecked) {
                        beginnerRB.setChecked(false);
                        intermediateRB.setChecked(false);
                        setUpRecyclerView();
                    }
                }
            }
        });

        ascendingRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!dontRun) {
                    if (isChecked) {
                        descendingRB.setChecked(false);
                        setUpRecyclerView();
                    }
                }
            }
        });

        descendingRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!dontRun) {
                    if (isChecked) {
                        ascendingRB.setChecked(false);
                        setUpRecyclerView();
                    }
                }
            }
        });


        return view;
    }

    private void getPremades(){
        mPremadesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int index = 0;
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    PremadeProgramModelClass modelClass =
                            dataSnapshot1.getValue(PremadeProgramModelClass.class);

                    premadeList.add(modelClass);

                    index++;
                    if(index == dataSnapshot.getChildrenCount()){
                        setUpRecyclerView();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void resetRBs(int type){
        dontRun = true;
        if(type == 1){
            workoutTypeRB.setChecked(true);
        }else if(type == 2){
            experienceLevelRB.setChecked(true);
        }else if(type == 3){
            alphabeticalRB.setChecked(true);
        }
        bodybuildingRB.setChecked(true);
        beginnerRB.setChecked(true);
        ascendingRB.setChecked(true);
        dontRun = false;
    }

    /**
     * Still got some problems. You'll see them if you mess with it.
     */

    private List<PremadeProgramModelClass> sortPremadesAlphabetical(List<PremadeProgramModelClass> list){

        Collections.sort(list, new Comparator<PremadeProgramModelClass>() {
            @Override
            public int compare(PremadeProgramModelClass o1, PremadeProgramModelClass o2) {
                return o1.getProgramName().compareTo(o2.getProgramName());
            }
        });

        return list;
    }

    private List<PremadeProgramModelClass> formatList(){
        List<PremadeProgramModelClass> formattedList = new ArrayList<>();

        if(workoutTypeRB.isChecked()){
            List<PremadeProgramModelClass> bbList = new ArrayList<>();
            List<PremadeProgramModelClass> strengthList = new ArrayList<>();
            List<PremadeProgramModelClass> hybridList = new ArrayList<>();
            for(PremadeProgramModelClass modelClass : premadeList){
                if(modelClass.getWorkoutType().equals("Bodybuilding")){
                    bbList.add(modelClass);
                }else if(modelClass.getWorkoutType().equals("Strength")){
                    strengthList.add(modelClass);
                }else if(modelClass.getWorkoutType().equals("Hybrid/Other")){
                    hybridList.add(modelClass);
                }
            }
            bbList = sortPremadesAlphabetical(bbList);
            strengthList = sortPremadesAlphabetical(strengthList);
            hybridList = sortPremadesAlphabetical(hybridList);
            if(bodybuildingRB.isChecked()){
                formattedList.addAll(bbList);
                formattedList.addAll(strengthList);
                formattedList.addAll(hybridList);
            }else if(strengthRB.isChecked()){
                formattedList.addAll(strengthList);
                formattedList.addAll(bbList);
                formattedList.addAll(hybridList);
            }else if(hybridOtherRB.isChecked()){
                formattedList.addAll(hybridList);
                formattedList.addAll(strengthList);
                formattedList.addAll(bbList);
            }else{
                resetRBs(1);
            }
        }else if(experienceLevelRB.isChecked()){
            List<PremadeProgramModelClass> beginnerList = new ArrayList<>();
            List<PremadeProgramModelClass> intermediateList = new ArrayList<>();
            List<PremadeProgramModelClass> advancedList = new ArrayList<>();
            for(PremadeProgramModelClass modelClass : premadeList){
                if(modelClass.getExperienceLevel().equals("Beginner")){
                    beginnerList.add(modelClass);
                }else if(modelClass.getExperienceLevel().equals("Intermediate")){
                    intermediateList.add(modelClass);
                }else if(modelClass.getExperienceLevel().equals("Advanced")){
                    advancedList.add(modelClass);
                }
            }
            beginnerList = sortPremadesAlphabetical(beginnerList);
            intermediateList = sortPremadesAlphabetical(intermediateList);
            advancedList = sortPremadesAlphabetical(advancedList);
            if(beginnerRB.isChecked()){
                formattedList.addAll(beginnerList);
                formattedList.addAll(intermediateList);
                formattedList.addAll(advancedList);
            }else if(intermediateRB.isChecked()){
                formattedList.addAll(intermediateList);
                formattedList.addAll(beginnerList);
                formattedList.addAll(advancedList);
            }else if(advancedRB.isChecked()){
                formattedList.addAll(advancedList);
                formattedList.addAll(intermediateList);
                formattedList.addAll(beginnerList);
            }else{
                resetRBs(2);
            }
        }else if(alphabeticalRB.isChecked()){
            if(ascendingRB.isChecked()){
                formattedList.addAll(premadeList);
                formattedList = sortPremadesAlphabetical(formattedList);
            }else if(descendingRB.isChecked()){
                formattedList.addAll(premadeList);
                formattedList = sortPremadesAlphabetical(formattedList);
                Collections.reverse(formattedList);
            }else{
                resetRBs(3);
            }
        }else{
            resetRBs(1);
        }

        return formattedList;
    }

    private void setUpRecyclerView(){
        List<PremadeProgramModelClass> formattedList;
        if(!premadeList.isEmpty()){
            formattedList = formatList();

            loadingView.setVisibility(View.GONE);

            PremadeProgramRecyclerAdapter adapter = new PremadeProgramRecyclerAdapter(formattedList,
                    getActivity());

            recyclerView.setAdapter(adapter);
            recyclerView.setHasFixedSize(false);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager
                    .VERTICAL, false));
        }else{
            getPremades();
        }
    }

    private void setUpFirebaseAdapter(){
        /**
         * LOL this isn't going to work. Of course it doesn't magically sort the way we want it to.
         * We're going to have to get a list of them and do it manually.
         * Could also do a second row of radio buttons depending on the choice of the first row.
         * Ie, Workout Type > (2nd Row) Bodybuilding Powerlifting Hybrid
         *
         * How do we do it then? Of course we'll get a list of the premade objects. Then somehow
         * we'll put it into a recycler view...we need to look for places we do it manually. Make
         * an adapter?
         */
        loadingView.setVisibility(View.GONE);

        Query query;

        if(workoutTypeRB.isChecked()){
            query = mPremadesRef.orderByChild("workoutType");
        }else if(experienceLevelRB.isChecked()){
            query = mPremadesRef.orderByChild("experienceLevel");
        }else if(alphabeticalRB.isChecked()){
            query = mPremadesRef.orderByChild("programName");
        }else{
            query = mPremadesRef.orderByChild("workoutType");
            dontRun = true;
            workoutTypeRB.setChecked(true);
        }

        FirebaseRecyclerOptions<PremadeProgramModelClass> options = new FirebaseRecyclerOptions
                .Builder<PremadeProgramModelClass>()
                .setQuery(query, PremadeProgramModelClass.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<PremadeProgramModelClass,
                PremadeProgramViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PremadeProgramViewHolder holder,
                                            int position, @NonNull PremadeProgramModelClass model) {

                holder.setActivity(getActivity());
                holder.setDescription(model.getProgramDescription());
                holder.setExperienceLevel(model.getExperienceLevel());
                holder.setTemplateName(model.getProgramName());
                holder.setWorkoutType(model.getWorkoutType());
                holder.setWorkoutCode(model.getWorkoutCode());

            }

            @NonNull
            @Override
            public PremadeProgramViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.premade_program_list_item,
                        parent, false);

                return new PremadeProgramViewHolder(view);
            }
        };

        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mFirebaseAdapter.startListening();
        recyclerView.setAdapter(mFirebaseAdapter);

    }


    @Override
    public void onStart(){
        super.onStart();

        headerChanger("Premade Programs");


        if(mFirebaseAdapter != null && mFirebaseAdapter.getItemCount() == 0){
            mFirebaseAdapter.startListening();
        }
    }

    @Override
    public void onStop(){
        super.onStop();


        if(mFirebaseAdapter != null){
            mFirebaseAdapter.stopListening();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (headerChangeFromFrag) activity;
        } catch (ClassCastException e) {
            //throw new ClassCastException(activity.toString()
            //        + " must implement OnHeadlineSelectedListener");
        }
    }

}
