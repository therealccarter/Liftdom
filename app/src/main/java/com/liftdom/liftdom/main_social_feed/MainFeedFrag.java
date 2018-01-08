package com.liftdom.liftdom.main_social_feed;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.DatePicker;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.appodeal.ads.Appodeal;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.MainActivitySingleton;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.ReleaseNotesActivity;
import com.liftdom.liftdom.SignInActivity;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutModelClass;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutViewHolder;
import com.liftdom.template_housing.TemplateMenuFrag;
import com.liftdom.user_profile.UserModelClass;
import com.wang.avi.AVLoadingIndicatorView;
import me.toptas.fancyshowcase.FancyShowCaseView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFeedFrag extends Fragment {

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

    bottomNavChanger navChangerCallback;

    public interface bottomNavChanger{
        void setBottomNavIndex(int navIndex);
    }

    private void navChanger(int navIndex){
        navChangerCallback.setBottomNavIndex(navIndex);
    }

    private String uid;
    private DatabaseReference rootRef;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter firebaseAdapter;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.noPostsView) TextView noPostsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_social_feed, container, false);

        ButterKnife.bind(this, view);

        HideKey.initialize(getActivity(), view);

        navChanger(0);
        headerChanger("Home");

        //if(!MainActivitySingleton.getInstance().isBannerViewInitialized){
        //    String appKey = "e05b98bf43240a8687216b4e3106a598ced75a344b6c75f2";
        //    Appodeal.initialize(getActivity(), appKey, Appodeal.BANNER);
        //    Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);
        //    MainActivitySingleton.getInstance().isBannerViewInitialized = true;
        //}else{
        //    //Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);
        //}


        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(getContext(), SignInActivity.class));
        }else{
            //showTutorialChoice();
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            rootRef = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference socialRef = rootRef.child("feed").child(uid);
            socialRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    firstTimeTutorial();
                    if(dataSnapshot.exists()){
                        checkForReleaseNotes();
                        DatabaseReference userRef = rootRef.child("user").child(uid);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                                if(userModelClass.isIsImperial()){
                                    loadingView.setVisibility(View.GONE);
                                    noPostsView.setVisibility(View.GONE);
                                    setUpFirebaseAdapter(socialRef, true);
                                }else{
                                    loadingView.setVisibility(View.GONE);
                                    noPostsView.setVisibility(View.GONE);
                                    setUpFirebaseAdapter(socialRef, false);
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }else{
                        loadingView.setVisibility(View.GONE);
                        noPostsView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        return view;
    }

    private void checkForReleaseNotes(){
        if(!MainActivitySingleton.getInstance().isReleaseCheck){
            DatabaseReference firstTimeRef = FirebaseDatabase.getInstance().getReference().child("firstTime").child
                    (uid).child("isTemplateMenuFirstTime");
            firstTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.exists()){
                        final int currentVersionInt = 115;
                        final String currentVersionString = String.valueOf(currentVersionInt);
                        final DatabaseReference currentVersionRef = FirebaseDatabase.getInstance().getReference().child("versionCheck")
                                .child(uid);
                        currentVersionRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.exists()){
                                    MainActivitySingleton.getInstance().isReleaseCheck = true;
                                    // display release notes for currentVersionString
                                    currentVersionRef.setValue(currentVersionString);
                                    Intent intent = new Intent(getContext(), ReleaseNotesActivity.class);
                                    startActivity(intent);
                                }else{
                                    MainActivitySingleton.getInstance().isReleaseCheck = true;
                                    String databaseVersion = dataSnapshot.getValue(String.class);
                                    int databaseVersionInt = Integer.parseInt(databaseVersion);
                                    if(currentVersionInt > databaseVersionInt){
                                        currentVersionRef.setValue(currentVersionString);
                                        Intent intent = new Intent(getContext(), ReleaseNotesActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void showTutorialChoice(){

        final DatabaseReference firstTime = FirebaseDatabase.getInstance().getReference().child("firstTime")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isFeedFirstTime");
        firstTime.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    // set title
                    builder.setTitle("Take tutorial?");

                    builder.setMessage("Would you like to take the tutorial? It's highly recommended!")
                            .setPositiveButton("Yes, take tutorial", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    firstTime.setValue(null);
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.mainFragHolder, new TemplateMenuFrag());
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatabaseReference firstTimeRef = FirebaseDatabase.getInstance().getReference().child
                                            ("firstTime").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    firstTimeRef.setValue(null);
                                }
                            });

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void firstTimeTutorial(){
        final DatabaseReference firstTimeRef = FirebaseDatabase.getInstance().getReference().child("firstTime")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isFeedFirstTime");
        firstTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    new FancyShowCaseView.Builder(getActivity())
                            .title("This is the Social Feed. It's where you and your friends' completed workouts will " +
                                    "be posted.")
                            .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
                            .build()
                            .show();
                    firstTimeRef.setValue(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpFirebaseAdapter(DatabaseReference databaseReference, final boolean isImperial){

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(10);

        firebaseAdapter = new FirebaseRecyclerAdapter<CompletedWorkoutModelClass, CompletedWorkoutViewHolder>
                (CompletedWorkoutModelClass.class, R.layout.completed_workout_list_item,
                        CompletedWorkoutViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(CompletedWorkoutViewHolder viewHolder,
                                              CompletedWorkoutModelClass model, int position) {
                if(loadingView.getVisibility() == View.VISIBLE){
                    loadingView.setVisibility(View.GONE);
                }
                //viewHolder.setPosition(position);
                viewHolder.setImperialPOV(isImperial);
                viewHolder.setActivity(getActivity());
                viewHolder.setRefKey(model.getRef());
                viewHolder.setUserId(model.getUserId());
                viewHolder.setPostInfo(model.getWorkoutInfoMap(), getActivity(), getContext(),
                        model.isIsImperial());
                viewHolder.setUpProfilePics(model.getUserId());
                viewHolder.setCommentRecycler(model.getRef());
                viewHolder.setUserName(model.getUserName());
                viewHolder.setUserLevel(model.getUserId(), rootRef);
                viewHolder.setPublicDescription(model.getPublicDescription());
                viewHolder.setTimeStamp(model.getDateTime());
                //viewHolder.setReppedCount(model.getRepCount());
                viewHolder.setRepsCounterView(model.getRepCount());
                viewHolder.setIsRepped(model.isHasRepped());
                //viewHolder.setActivity(getActivity());
                if(model.getBonusList() != null){
                    if(!model.getBonusList().isEmpty()){
                        viewHolder.setBonusView(model.getBonusList());
                    }
                }
            }
        };

        recyclerView.setAdapter(firebaseAdapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(firebaseAdapter != null){
            firebaseAdapter.cleanup();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (headerChangeFromFrag) activity;
            navChangerCallback = (bottomNavChanger) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
