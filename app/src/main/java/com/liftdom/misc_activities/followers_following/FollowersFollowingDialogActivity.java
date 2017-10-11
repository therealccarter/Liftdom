package com.liftdom.misc_activities.followers_following;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import butterknife.BindView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.UserNameIdModelClass;
import com.wang.avi.AVLoadingIndicatorView;

public class FollowersFollowingDialogActivity extends AppCompatActivity {

    private String xUid;
    private String uid;
    private DatabaseReference rootRef;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter firebaseAdapter;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_following_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        rootRef = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(getIntent().getExtras() != null){
            if(getIntent().getStringExtra("type").equals("followers")){
                xUid = getIntent().getStringExtra("xUid");
                DatabaseReference databaseReference = rootRef.child("followers").child(xUid);
                setUpFirebaseAdapter(databaseReference);
            }else if(getIntent().getStringExtra("type").equals("following")){
                xUid = getIntent().getStringExtra("xUid");
                DatabaseReference databaseReference = rootRef.child("following").child(xUid);
                setUpFirebaseAdapter(databaseReference);
            }
        }
    }

    private void setUpFirebaseAdapter(DatabaseReference databaseReference){
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(10);

        firebaseAdapter = new FirebaseRecyclerAdapter<UserNameIdModelClass, FollowersFollowingViewHolder>
                (UserNameIdModelClass.class, R.layout.followers_following_list_item, FollowersFollowingViewHolder.class,
                        databaseReference) {
            @Override
            protected void populateViewHolder(FollowersFollowingViewHolder viewHolder, UserNameIdModelClass model, int position) {
                viewHolder.setxUid(model.getUserId());
                viewHolder.setUserName(model.getUserName());
                viewHolder.setUid(uid);
            }
        };
    }


}