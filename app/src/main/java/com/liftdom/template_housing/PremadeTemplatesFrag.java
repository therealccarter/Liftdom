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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.liftdom.liftdom.R;
import com.liftdom.workout_programs.PremadeProgramModelClass;
import com.liftdom.workout_programs.PremadeProgramViewHolder;
import com.wang.avi.AVLoadingIndicatorView;

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

    @BindView(R.id.recycler_view_premade_programs) RecyclerView recyclerView;
    @BindView(R.id.loadingView2) AVLoadingIndicatorView loadingView;
    @BindView(R.id.workoutTypeRB) RadioButton workoutTypeRB;
    @BindView(R.id.experienceLevelRB) RadioButton experienceLevelRB;
    @BindView(R.id.alphabeticalRB) RadioButton alphabeticalRB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_premade_templates, container, false);

        ButterKnife.bind(this, view);

        workoutTypeRB.setChecked(true);

        setUpFirebaseAdapter();

        workoutTypeRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setUpFirebaseAdapter();
                }
            }
        });

        experienceLevelRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setUpFirebaseAdapter();
                }
            }
        });

        alphabeticalRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    setUpFirebaseAdapter();
                }
            }
        });

        return view;
    }

    private void setUpFirebaseAdapter(){
        /**
         * LOL this isn't going to work. Of course it doesn't magically sort the way we want it to.
         * We're going to have to get a list of them and do it manually.
         * Could also do a second row of radio buttons depending on the choice of the first row.
         * Ie, Workout Type > (2nd Row) Bodybuilding Powerlifting Hybrid
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
