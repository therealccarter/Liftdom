package com.liftdom.template_housing;


import android.os.Bundle;
import android.support.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.liftdom.template_editor.TemplateModelClass;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProgramInboxFrag extends Fragment {


    public ProgramInboxFrag() {
        // Required empty public constructor
    }

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private DatabaseReference mFeedRef = FirebaseDatabase.getInstance().getReference().child("templatesInbox")
            .child(uid);

    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.programInboxTitle) TextView programInboxTitle;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.noProgramsFoundView) TextView noProgramsFoundView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_program_inbox, container, false);

        ButterKnife.bind(this, view);

        mFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    programInboxTitle.setVisibility(View.GONE);
                    loadingView.setVisibility(View.GONE);
                    noProgramsFoundView.setVisibility(View.GONE);
                    setUpFirebaseAdapter();
                }else{
                    loadingView.setVisibility(View.GONE);
                    noProgramsFoundView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void setUpFirebaseAdapter(){

        Query query = mFeedRef.orderByChild("dateUpdated");

        FirebaseRecyclerOptions<TemplateModelClass> options = new FirebaseRecyclerOptions
                .Builder<TemplateModelClass>()
                .setQuery(query, TemplateModelClass.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<TemplateModelClass, SavedTemplateViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull SavedTemplateViewHolder holder, int position,
                                            @NonNull TemplateModelClass model) {
                holder.setFromInbox(true);
                holder.setFromSendProgram(false);
                holder.setTemplateNameView(model.getTemplateName());
                holder.setTimeStampView(model.getDateUpdated());
                holder.setDaysView(model.getDays());
                holder.setDescriptionView(model.getDescription());
                holder.setActivity(getActivity());
                holder.setAuthoredBy(model.getUserName());
            }

            @Override
            public SavedTemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.saved_template_list_item,
                        parent, false);

                return new SavedTemplateViewHolder(view);
            }
        };

        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        mFirebaseAdapter.startListening();
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        recyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    public void onStart(){
        super.onStart();
        if(mFirebaseAdapter != null && mFirebaseAdapter.getItemCount() == 0){
            mFirebaseAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mFirebaseAdapter != null){
            mFirebaseAdapter.stopListening();
        }
    }

}
