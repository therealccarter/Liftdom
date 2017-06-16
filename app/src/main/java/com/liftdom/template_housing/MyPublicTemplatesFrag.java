package com.liftdom.template_housing;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
    private DatabaseReference mFeedRef = FirebaseDatabase.getInstance().getReference().child("public_templates")
            .child("my_public_templates");

    @BindView(R.id.recycler_view_saved_templates) RecyclerView mRecyclerView;
    @BindView(R.id.loadingView2) AVLoadingIndicatorView loadingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_public_templates, container, false);

        return view;
    }

    private void setUpFirebaseAdapter(){
        mFirebaseAdapter = new FirebaseRecyclerAdapter<TemplateModelClass, PublicTemplateViewHolder>
                (TemplateModelClass.class, R.layout.public_template_list_item, PublicTemplateViewHolder.class, mFeedRef) {
            @Override
            protected void populateViewHolder(PublicTemplateViewHolder viewHolder, TemplateModelClass model, int position) {
                viewHolder.setTemplateNameView(model.getTemplateName());
                viewHolder.setTimeStampView(model.getDateUpdated());
                viewHolder.setDaysView(model.getDays());
                viewHolder.setCreatedBy(model.getUserName());
                if(model.getUserName2() != null){
                    viewHolder.setEditedBy(model.getUserName2());
                }
                viewHolder.setDescriptionView(model.getDescription());
                viewHolder.setActivity(getActivity());
                viewHolder.setKey(mFirebaseAdapter.getRef(position).getKey());
                viewHolder.setIsMyPublicTemplate(true);

                if(position == 0){
                    AVLoadingIndicatorView loadingView = (AVLoadingIndicatorView) getActivity().findViewById(R.id
                            .loadingView2);
                    if(loadingView != null){
                        loadingView.setVisibility(View.GONE);
                    }
                }
            }
        };

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }


}
