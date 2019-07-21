package com.liftdom.liftdom.main_social_feed.comment_post;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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


        mFeedRef = mRootRef.child("feed").child(uid).child(parentRefKey).child("commentMap");
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

        FirebaseRecyclerOptions<PostCommentModelClass> options = new FirebaseRecyclerOptions
                .Builder<PostCommentModelClass>()
                .setQuery(mFeedRef, PostCommentModelClass.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<PostCommentModelClass, PostCommentViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull PostCommentViewHolder holder, int position, @NonNull PostCommentModelClass model) {
                holder.setComment(model.getCommentText());
                holder.setDateString(model.getDateString());
                holder.setRepNumber(model.getRepNumber());
                holder.setRefKey(model.getRefKey());
                holder.setUsername(model.getUserName());
            }

            @Override
            public PostCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.post_comment_list_item,
                        parent, false);

                return new PostCommentViewHolder(view);
            }
        };

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
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
    public void onStop() {
        super.onStop();
        if(mFirebaseAdapter != null){
            mFirebaseAdapter.stopListening();
        }
    }

}
