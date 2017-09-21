package com.liftdom.liftdom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompleteWorkoutRecyclerAdapter;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutModelClass;

import java.util.ArrayList;

public class WorkoutPostSingleActivity extends AppCompatActivity {

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private DatabaseReference mFeedRef;

    @BindView(R.id.recycler_view_feed) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_post_single);
        this.setFinishOnTouchOutside(true);

        ButterKnife.bind(this);

        if(getIntent().getStringExtra("refKey") != null){
            String refKey = getIntent().getStringExtra("refKey");
            mFeedRef = FirebaseDatabase.getInstance().getReference().child("feed").child(uid).child(refKey);
            setUpRecycler();
        }


    }

    private void setUpRecycler(){
        mFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ArrayList<CompletedWorkoutModelClass> postList = new ArrayList<>();
                    CompletedWorkoutModelClass workoutModelClass = dataSnapshot.getValue(CompletedWorkoutModelClass.class);
                    postList.add(workoutModelClass);

                    mRecyclerView.setHasFixedSize(false);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(WorkoutPostSingleActivity.this);
                    linearLayoutManager.setReverseLayout(true);
                    linearLayoutManager.setStackFromEnd(true);
                    linearLayoutManager.setSmoothScrollbarEnabled(true);
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    CompleteWorkoutRecyclerAdapter adapter = new CompleteWorkoutRecyclerAdapter(postList,
                            getApplicationContext(), WorkoutPostSingleActivity.this, true);
                    mRecyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}