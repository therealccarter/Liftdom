package com.liftdom.charts_stats_tools.exercise_selector;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import androidx.fragment.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.wang.avi.AVLoadingIndicatorView;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomExListFrag extends Fragment {


    public CustomExListFrag() {
        // Required empty public constructor
    }

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference customExRef = FirebaseDatabase.getInstance().getReference().child("customExercises")
            .child(uid);
    private FirebaseRecyclerAdapter firebaseAdapter;
    private LinearLayoutManager linearLayoutManager;

    boolean noCheckbox = false;
    boolean isExclusive = false;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.noChatFoundView) TextView noChatsFoundView;
    @BindView(R.id.addCustomExButton) Button addCustomExButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_custom_ex_list, container, false);

        ButterKnife.bind(this, view);

        //if(!noCheckbox){
        //    addCustomExButton.setVisibility(View.GONE);
        //}

        addCustomExButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), CustomExCreatorDialog.class);
                startActivityForResult(intent, 1);
            }
        });

        customExRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    loadingView.setVisibility(View.GONE);
                    noChatsFoundView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        setUpFirebaseAdapter();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if(requestCode == 1){
                if(resultCode == 2){
                    Snackbar.make(getView(), "Custom exercise upload failed...", Snackbar.LENGTH_SHORT);
                }
            }
        }
    }

    private void setUpFirebaseAdapter(){

        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        FirebaseRecyclerOptions<CustomExModelClass> options = new FirebaseRecyclerOptions
                .Builder<CustomExModelClass>()
                .setQuery(customExRef, CustomExModelClass.class)
                .build();

        firebaseAdapter = new FirebaseRecyclerAdapter<CustomExModelClass, CustomExViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CustomExViewHolder holder, int position, @NonNull CustomExModelClass model) {
                if(position == 0){
                    loadingView.setVisibility(View.GONE);
                    noChatsFoundView.setVisibility(View.GONE);
                }
                holder.setFragActivity(getActivity());
                holder.setExName(model.getExerciseName());
                holder.setRefKey(model.getRefKey());
                holder.setNoCheckbox(noCheckbox);
                holder.setExclusive(isExclusive);
                if(!noCheckbox){
                    if(ExSelectorSingleton.getInstance().customItems.contains(model.getExerciseName())){
                        holder.setIsChecked(true);
                    }else{
                        holder.setIsChecked(false);
                    }
                }
            }

            @Override
            public CustomExViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.custom_ex_list_item,
                        parent, false);

                return new CustomExViewHolder(view);
            }
        };

        firebaseAdapter.startListening();
        mRecyclerView.setAdapter(firebaseAdapter);
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