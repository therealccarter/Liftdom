package com.liftdom.misc_activities.followers_following;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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
    @BindView(R.id.titleView) TextView titleView;
    @BindView(R.id.closeButton) Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_followers_following_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ButterKnife.bind(this);

        rootRef = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");

        titleView.setTypeface(lobster);

        SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        String userName = sharedPref.getString("userName", "loading...");

        if(getIntent().getExtras() != null){
            if(getIntent().getStringExtra("type").equals("followers")){
                titleView.setText(R.string.followers);
                xUid = getIntent().getStringExtra("uid");
                DatabaseReference databaseReference = rootRef.child("followers").child(xUid);
                setUpFirebaseAdapter(databaseReference, userName);
            }else if(getIntent().getStringExtra("type").equals("following")){
                titleView.setText(R.string.following);
                xUid = getIntent().getStringExtra("uid");
                DatabaseReference databaseReference = rootRef.child("following").child(xUid);
                setUpFirebaseAdapter(databaseReference, userName);
            }
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpFirebaseAdapter(DatabaseReference databaseReference, final String userName){
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(20);

        FirebaseRecyclerOptions<UserNameIdModelClass> options = new FirebaseRecyclerOptions
                .Builder<UserNameIdModelClass>()
                .setQuery(databaseReference, UserNameIdModelClass.class)
                .build();

        firebaseAdapter = new FirebaseRecyclerAdapter<UserNameIdModelClass, FollowersFollowingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FollowersFollowingViewHolder holder, int position,
                                            @NonNull UserNameIdModelClass model) {
                if(loadingView.getVisibility() == View.VISIBLE){
                    loadingView.setVisibility(View.GONE);
                }
                holder.setFragmentActivity(FollowersFollowingDialogActivity.this);
                holder.setContext(FollowersFollowingDialogActivity.this);
                holder.setxUid(model.getUserId());
                holder.setUserName(model.getUserName());
                if(model.getUserId().equals(uid)){
                    holder.setUid(uid, true);
                }else{
                    holder.setUid(uid, false);
                }

                holder.setYourUserName(userName);
            }

            @Override
            public FollowersFollowingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.followers_following_list_item,
                        parent, false);

                return new FollowersFollowingViewHolder(view);
            }
        };

        loadingView.setVisibility(View.GONE);
        firebaseAdapter.startListening();
        recyclerView.setAdapter(firebaseAdapter);
    }

    @Override
    public void onStart(){
        super.onStart();
        if(firebaseAdapter != null && firebaseAdapter.getItemCount() == 0){
            firebaseAdapter.startListening();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(firebaseAdapter != null){
            firebaseAdapter.stopListening();
        }
    }

}