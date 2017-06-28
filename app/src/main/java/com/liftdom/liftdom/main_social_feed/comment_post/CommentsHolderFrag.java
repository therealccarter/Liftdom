package com.liftdom.liftdom.main_social_feed.comment_post;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CommentsHolderFrag extends Fragment {


    public CommentsHolderFrag() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private DatabaseReference mFeedRef;
    private FirebaseRecyclerAdapter mFirebaseAdapter;

    public String parentRefKey;
    public FragmentActivity mActivity;

    @BindView(R.id.recycler_view_feed) RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comments_holder, container, false);

        ButterKnife.bind(this, view);


        mFeedRef = mRootRef.child("feed").child(uid).child(parentRefKey).child("comments");
        mFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    setUpFirebaseAdapter();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

    private void setUpFirebaseAdapter(){
        mFirebaseAdapter = new FirebaseRecyclerAdapter<PostCommentModelClass, PostCommentViewHolder>
                (PostCommentModelClass.class, R.layout.post_comment_list_item, PostCommentViewHolder.class, mFeedRef) {
            @Override
            protected void populateViewHolder(PostCommentViewHolder viewHolder, PostCommentModelClass model, int position) {
                viewHolder.setComment(model.getCommentText());
                viewHolder.setDateString(model.getDateString());
                viewHolder.setRepNumber(model.getRepNumber());
                viewHolder.setRefKey(model.getRefKey());
                viewHolder.setUsername(model.getUserName());
            }
        };

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

}
