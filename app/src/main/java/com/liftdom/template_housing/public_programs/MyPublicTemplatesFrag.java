package com.liftdom.template_housing.public_programs;


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
public class MyPublicTemplatesFrag extends Fragment {


    public MyPublicTemplatesFrag() {
        // Required empty public constructor
    }

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private DatabaseReference mFeedRef = FirebaseDatabase.getInstance().getReference().child("publicTemplates")
            .child("myPublic").child(uid);

    @BindView(R.id.recycler_view_saved_templates) RecyclerView mRecyclerView;
    @BindView(R.id.loadingView2) AVLoadingIndicatorView loadingView;
    @BindView(R.id.noProgramsFoundView) TextView noProgramsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_public_templates, container, false);

        ButterKnife.bind(this, view);

        mFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    loadingView.setVisibility(View.GONE);
                    noProgramsView.setVisibility(View.GONE);
                    setUpFirebaseAdapter();
                }else{
                    loadingView.setVisibility(View.GONE);
                    noProgramsView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void setUpFirebaseAdapter(){

        FirebaseRecyclerOptions<TemplateModelClass> options = new FirebaseRecyclerOptions
                .Builder<TemplateModelClass>()
                .setQuery(mFeedRef, TemplateModelClass.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<TemplateModelClass, PublicTemplateViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PublicTemplateViewHolder holder, int position, @NonNull TemplateModelClass model) {
                holder.setTemplateNameView(model.getTemplateName());
                holder.setTimeStampView(model.getDateUpdated());
                holder.setDaysView(model.getDays());
                holder.setCreatedBy(model.getUserName());
                if(model.getUserName2() != null){
                    holder.setEditedBy(model.getUserName2());
                }
                holder.setDescriptionView(model.getDescription());
                holder.setActivity(getActivity());
                holder.setKey(mFirebaseAdapter.getRef(position).getKey());
                holder.setIsMyPublicTemplate(true);
            }

            @Override
            public PublicTemplateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.public_template_list_item,
                        parent, false);

                return new PublicTemplateViewHolder(view);
            }
        };

        loadingView.setVisibility(View.GONE);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mFirebaseAdapter.startListening();
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    public void onStart(){
        super.onStart();
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

}
