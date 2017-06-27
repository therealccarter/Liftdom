package com.liftdom.liftdom.main_social_feed;


import android.app.Activity;
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
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutModelClass;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutViewHolder;
import com.wang.avi.AVLoadingIndicatorView;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFeedFrag extends Fragment{


    public MainFeedFrag() {
        // Required empty public constructor
    }

    headerChangeFromFrag mCallback;

    public interface headerChangeFromFrag{
        public void changeHeaderTitle(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeaderTitle(title);
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


    private DatabaseReference mFeedRef = FirebaseDatabase.getInstance().getReference().child("feed")
            .child(uid);
    private FirebaseRecyclerAdapter mFirebaseAdapter;

    //@BindView(R.id.loadingView1) AVLoadingIndicatorView loadingView;
    @BindView(R.id.noResultsView) TextView noResultsView;
    @BindView(R.id.recycler_view_feed) RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_feed, container, false);

        BottomNavigation bottomNavigation = (BottomNavigation) getActivity().findViewById(R.id.BottomNavigation);
        bottomNavigation.setSelectedIndex(1, false);

        ButterKnife.bind(this, view);

        headerChanger("Home");

        mFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    setUpFirebaseAdapter();
                }else{
                    AVLoadingIndicatorView loadingView = (AVLoadingIndicatorView) getActivity().findViewById(R.id
                            .loadingView1);
                    loadingView.setVisibility(View.GONE);
                    noResultsView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

    private void setUpFirebaseAdapter(){
        mFirebaseAdapter = new FirebaseRecyclerAdapter<CompletedWorkoutModelClass, CompletedWorkoutViewHolder>
                (CompletedWorkoutModelClass.class, R.layout.completed_workout_list_item, CompletedWorkoutViewHolder.class
                , mFeedRef) {
            @Override
            protected void populateViewHolder(CompletedWorkoutViewHolder viewHolder, CompletedWorkoutModelClass model, int position) {
                viewHolder.setUserName(model.getUserName());
                viewHolder.setUserLevel(model.getUserId());
                viewHolder.setActivity(getActivity());
                viewHolder.setUserId(model.getUserId());
                viewHolder.setPublicDescription(model.getPublicDescription());
                viewHolder.setTimeStamp(model.getDateTime());
                viewHolder.setPostInfo(model.getWorkoutInfoMap(), getActivity());
                viewHolder.setActivity(getActivity());

                if(position == 0){
                    AVLoadingIndicatorView loadingView = (AVLoadingIndicatorView) getActivity().findViewById(R.id
                            .loadingView1);
                    if(loadingView != null){
                        loadingView.setVisibility(View.GONE);
                    }
                }
            }
        };

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        mRecyclerView.setAdapter(mFirebaseAdapter);

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (headerChangeFromFrag) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
