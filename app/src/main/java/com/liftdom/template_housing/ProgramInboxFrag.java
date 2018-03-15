package com.liftdom.template_housing;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
        mFirebaseAdapter = new FirebaseRecyclerAdapter<TemplateModelClass, SavedTemplateViewHolder>
                (TemplateModelClass.class, R.layout.saved_template_list_item, SavedTemplateViewHolder.class, query) {
            @Override
            protected void populateViewHolder(SavedTemplateViewHolder viewHolder, TemplateModelClass model, int position) {
                viewHolder.setFromInbox(true);
                viewHolder.setFromSendProgram(false);
                viewHolder.setTemplateNameView(model.getTemplateName());
                viewHolder.setTimeStampView(model.getDateUpdated());
                viewHolder.setDaysView(model.getDays());
                viewHolder.setDescriptionView(model.getDescription());
                viewHolder.setActivity(getActivity());
                viewHolder.setAuthoredBy(model.getUserName());
            }
        };


        recyclerView.setHasFixedSize(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        recyclerView.setAdapter(mFirebaseAdapter);
    }

}
